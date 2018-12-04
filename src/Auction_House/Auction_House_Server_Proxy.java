package Auction_House;

import Misc.Command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Auction_House_Server_Proxy extends Thread{

    private ServerSocket serverSocket;
    private Socket acceptSocket;

    private Auction_House auctionHouse;
    private int portNumber;
    boolean run;

    public Auction_House_Server_Proxy(Auction_House auctionHouse, int portNumber) {
        this.run = true;
        this.auctionHouse = auctionHouse;
        this.portNumber = portNumber;
        start();
        startAuctionHouseServer();
    }


    public void startAuctionHouseServer() {
        System.out.println("Starting AH thread " + portNumber);
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(portNumber);
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

                        case GetListItems:
                            Object[] tempArray = {Command.SetListItems, auctionHouse.getItemList()};
                            serverOutput.writeObject(tempArray);
                            System.out.println("servers array returning " + tempArray[1]);
                            break;

                        case SendBid:
                            //call auction house method
                            Object[] bidDetermination = auctionHouse.sendBid((Integer) message[1], (String)message[2], (Double) message[3]);
                            System.out.println("bid determination is "+bidDetermination);
                            Object[] tempArray2 = {Command.SendBid, bidDetermination[0], bidDetermination[1]};
                            serverOutput.writeObject(tempArray2);
                            break;

                        case TimeOffSet:
                            serverOutput.writeObject(new Object[] {Command.TimeOffSet, System.currentTimeMillis()});
                            break;

                        case FundsTransferred:
                            synchronized (auctionHouse) {
                                auctionHouse.notifyAll();
                            }
                            break;

                        case CloseAgentAccount:

                            auctionHouse.removeAgent((Integer) message[1]);
                            break;
                    }

                    serverOutput.reset();
                }

                clientSocket.close();

            } catch (IOException e) {
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
