import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Scanner;

public class Auction_House_Client_Proxy extends Thread {

    Agent agent;

    private Socket clientSocket;
    public ObjectInputStream clientInput;
    public ObjectOutputStream clientOutput;

    String clientType;
    int portNumber;
    boolean run;

    public Auction_House_Client_Proxy(Agent agent, int key, String clientType, int portNumber) {
        this.agent = agent;
        this.clientType = clientType;
        this.portNumber = portNumber;
        this.run = true;
        start();
    }

    public Auction_House_Client_Proxy(int key, String clientType, int portNumber) {
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
                        System.out.println("test! in ah client! " + message[1]);
                        break;
                    case SetListItems:
                        agent.createItemList((LinkedList<Item>) message[1]);
                        break;
                }

            }
            Thread.sleep(0);


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
