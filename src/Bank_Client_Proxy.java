import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class Bank_Client_Proxy extends Thread {

    private Socket clientSocket = null;
    public ObjectInputStream clientInput = null;
    public ObjectOutputStream clientOutput = null;

    Agent agent;
    String clientType;
    int portNumber;
    boolean run;

    public Bank_Client_Proxy(int key, String clientType, int portNumber) {
        this.clientType = clientType;
        this.portNumber = portNumber;
        this.run = true;
        start();
    }

    public Bank_Client_Proxy(Agent agent, int key, String clientType, int portNumber) {
        this.agent = agent;
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

                switch (command) {

                    case BlockFunds:
                        System.out.println("test! in bank client! " + message[1]);
                        break;

                    case SetListHouses:
                        agent.createHouseList((LinkedList<Integer>) message[1], (ConcurrentHashMap<Integer, Integer>) message[2]);
                        break;


                }
                //System.out.println("message received: " + message);
                Thread.sleep(0);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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
