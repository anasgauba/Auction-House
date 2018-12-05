/*Anas Guaba, Clarissa Garcia, and Nathan Schaefer
 * Auction House Server Proxy Class
 * <p>
 *
 * @author Nathan Schaefer
 *
 * Auction House Server Proxy is the class that is used to receive messages  from auction house client and relate
 * the information received back to the original object who requested the information that was originally
 * asked by the other objects.
 */

package Auction_House;
import Misc.Command;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The Auction House server proxy class holds the auction house object it will manipulate and the socket
 * components necessary to connect the server to the clients that attempt to communicate with it.
 */
public class Auction_House_Server_Proxy extends Thread{
    private ServerSocket serverSocket;
    private Socket acceptSocket;
    private Auction_House auctionHouse;
    private int portNumber;
    boolean run;

    /**
     * The auction house server  is created by the constructor by being passed in the auction house object holding
     * all of the methods and the port number in which the auction house server will be hosted at
     * @param auctionHouse - the auction house object holding all of the methods
     * @param portNumber - port number in which the auction house server will be hosted at
     */
    public Auction_House_Server_Proxy(Auction_House auctionHouse, int portNumber) {
        this.run = true;
        this.auctionHouse = auctionHouse;
        this.portNumber = portNumber;
        start();
        startAuctionHouseServer();
    }

    /**
     * The startAuctionHouseServer method starts the server by creating the server socket using
     * the port that was inputted to start the auction house server on. While the thread is running,
     * it attempts to continue accepting the socket and calls the startAuctionHouseThread method, which
     * will read in objects and interpret the commands to make on Auction House object.
     */
    public void startAuctionHouseServer() {
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

    /**
     * The startAuctionHouseThread method is in charge of receiving messages from the other objects
     * and calling methods in bank to fulfill the requests for the information. It then sends the
     * messages back to the clients of the objects who originally requested that information. it uses the
     * server socket passed in to connect the object input and output streams and then waits for commands passed
     * into it from other objects to fulfill the requests. The switch statement is made on the command, which is
     * always found on the first object of the array and the methods are called depending on whcih command has been
     * passed in.
     * @param clientSocket
     */
    public void startAuctionHouseThread(Socket clientSocket) {
        new Thread(() -> {
            try {
                ObjectOutputStream serverOutput = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream serverInput = new ObjectInputStream(clientSocket.getInputStream());
                String temp = (String) serverInput.readObject();
                if (temp.contains("Agent")) {
                    auctionHouse.startAuctionHouseClient(temp);
                }
                while (run && clientSocket.isConnected()) {
                    Object[] message = (Object[]) serverInput.readObject();
                    Command command = (Command) message[0];
                    switch (command) {
                        case GetListItems:
                            Object[] tempArray = {Command.SetListItems, auctionHouse.getItemList()};
                            serverOutput.writeObject(tempArray);
                            break;
                        case SendBid:
                            Object[] bidDetermination = auctionHouse.sendBid((Integer) message[1], (String)message[2], (Double) message[3]);
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
