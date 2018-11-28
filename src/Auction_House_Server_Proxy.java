import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Auction_House_Server_Proxy extends Thread{

    private ServerSocket serverSocket;
    private Socket acceptSocket;

    private Auction_House auctionHouse;

    boolean run;

    public Auction_House_Server_Proxy(Auction_House auctionHouse) {
        this.run = true;
        this.auctionHouse = auctionHouse;
        start();
        startAuctionHouseServer();
    }


    public void startAuctionHouseServer() {
        System.out.println("Starting AH thread");
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(6666);
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (true) {
                try {
                    acceptSocket = serverSocket.accept();
                    startAuctionHouseThread(acceptSocket);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void startAuctionHouseThread(Socket clientSocket) {
        new Thread(() -> {
            try {

                ObjectOutputStream serverOutput = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream serverInput = new ObjectInputStream(clientSocket.getInputStream());

                String temp = (String) serverInput.readObject();
                System.out.println("received in AH: " + temp);

                if (temp.contains("Agent")) {
                    auctionHouse.startAuctionHouseClient(temp);
                }

                while (run && clientSocket.isConnected()) {

                    Object[] message = (Object[]) serverInput.readObject();
                    Command command = (Command) message[0];

                    //this switch handles incoming messages.  NOT from client, but from what ever
                    //button the user on the gui will press or from the agent thread
                    //Clients switch will be for INCOMING messages from Server or the ser
                    switch (command) {
                        case BlockFunds:
                            System.out.println("test in ah serv! " + message[1] + command);
                            serverOutput.writeObject(message);
                            break;
                    }

                    Thread.sleep(0);
                }

            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
