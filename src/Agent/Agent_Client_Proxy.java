package Agent;

import Misc.Command;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Agent_Client_Proxy extends Thread {

    private Socket clientSocket;
    public ObjectInputStream clientInput;
    public ObjectOutputStream clientOutput;

    String clientType;
    int portNumber;
    boolean run;

    public Agent_Client_Proxy(int key, String clientType, int portNumber) {
        this.clientType = clientType;
        this.portNumber = portNumber;
        this.run = true;
        start();
    }

    public void run() {

        try {
            clientSocket = new Socket(getServerIP(), portNumber);
            clientOutput = new ObjectOutputStream(clientSocket.getOutputStream());
            clientOutput.writeObject(clientType + " " + clientSocket.getPort());

            while (run && clientSocket.isConnected()) {

                if (clientInput == null) {
                    clientInput = new ObjectInputStream(clientSocket.getInputStream());
                }

                Object[] message = (Object[]) clientInput.readObject();
                Command command = (Command) message[0];
                System.out.println("THE COMMAND THAT AGENT CLIENT HAS RECEIVED IS:  "+command);

                switch (command) {

                    case WinMessage:
                        break;

                    case BidOvertaken:

                        clientOutput.writeObject(new Object[] {Command.BidOvertaken});

                        break;

                    case RefreshTimes:
                        clientOutput.writeObject(new Object[] {Command.RefreshTimes});
                }
                //System.out.println("message received: " + message);
                Thread.sleep(0);
            }

        } catch (InterruptedException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getServerIP() throws FileNotFoundException {

        Scanner in = new Scanner(new File("resources/Configuration"));

        while (in.hasNextLine()) {
            Scanner scanner = new Scanner(in.nextLine());

            if (scanner.hasNext() && scanner.next().equals("AuctionHouseServerIP")) {
                return scanner.next();
            }
        }
        return null;
    }
}
