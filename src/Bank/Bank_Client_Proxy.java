/*Anas Guaba, Clarissa Garcia, and Nathan Schaefer
 * Bank_Client_Proxy Class
 * <p>
 *
 * @author Nathan Schaefer
 *
 * Bank_Client_Proxy is the class that is used to receive messages back directly from the other objects
 * and rely them to the servers that will call the requested object's methods. It also includes the client sockets
 * and the ObjectInput and Object output streams to relay array object messages to the servers. The client also
 * includes its client type and its own port number. BankClient in particular has an agent and an acution house
 * that it houses as its own personal clients.
 */
package Bank;

import Agent.Agent;
import Auction_House.Auction_House;
import Misc.Command;
import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class Bank_Client_Proxy extends Thread {
    private Socket clientSocket = null;
    private ObjectInputStream clientInput = null;
    private ObjectOutputStream clientOutput = null;
    private Agent agent;
    private Auction_House auctionHouse;
    private String clientType;
    private int key;
    private int portNumber;
    private boolean run;


    /**
     * This is the constructor used when the connecting client is an auction house client.
     * @param auctionHouse - the auction house that is a client
     * @param key - the bidding key of the auction house
     * @param clientType - the type of client trying to connect
     * @param portNumber - the port number of the bank server trying to connect
     */
    public Bank_Client_Proxy(Auction_House auctionHouse, int key, String clientType, int portNumber) {
        this.auctionHouse = auctionHouse;
        this.clientType = clientType;
        this.key = key;
        this.portNumber = portNumber;
        this.run = true;
        start();
    }

    /**
     * This is the constructor used when the connection client is an agent client
     * @param agent - the agent that is a client
     * @param key - the bidding key of the agent
     * @param clientType - the type of client trying to connect
     * @param portNumber - the port number of the bank server trying to connect
     */
    public Bank_Client_Proxy(Agent agent, int key, String clientType, int portNumber) {
        this.agent = agent;
        this.key = key;
        this.clientType = clientType;
        this.portNumber = portNumber;
        this.run = true;
        start();
    }

    /**
     * The run method of the Bank Client Proxy thread is used to send a message
     * to its server to give information about the port number it’s connecting to,
     * where it’s connecting from, and the client type. Since messages are written
     * in arrays that have the first object as the enum Command, a switch is then
     * used on the first object to determine what commands to call on the server.
     * The other objects in the array are used for the paramaters of the methods that
     * will soon be called. Bank client handles messages from agent and auction house and writes
     * to its output to send to auction house Server and agent server.
     */
    public void run() {
        try {
            clientSocket = new Socket(getServerIP(), portNumber);
            clientOutput = new ObjectOutputStream(clientSocket.getOutputStream());
            clientOutput.writeObject(clientType + " " + clientSocket.getPort());
            if (clientType.contains("Agent")){
                clientOutput.writeObject(new Object[] {Command.CreateBankAccount, agent.agentName, 2000.0, "Agent"});
            }
            else if (clientType.contains("AuctionHouse")) {
                clientOutput.writeObject(new Object[] {Command.AddAuctionHouseID, auctionHouse.auctionHouseID, auctionHouse.portNumber});
                clientOutput.writeObject(new Object[] {Command.CreateBankAccount, String.valueOf(key), 0.0, "AuctionHouse"});
            }
            while (run && clientSocket.isConnected()) {
                if (clientInput == null) {
                    clientInput = new ObjectInputStream(clientSocket.getInputStream());
                }
                Object[] message = (Object[]) clientInput.readObject();
                Command command = (Command) message[0];
                switch (command) {
                    case SetListHouses:
                        agent.createHouseList((LinkedList<Integer>) message[1], (ConcurrentHashMap<Integer, Integer>) message[2]);
                        break;
                    case SetAgentKey:
                        Object[] bankInfo = (Object [])message[1];
                        agent.setBiddingKey((Integer) bankInfo[2]);
                        agent.setAgentDisplayValues((int)bankInfo[0],(double)bankInfo[1]);
                        break;
                    case SetAuctionHouseKey:
                        auctionHouse.setKey((Integer) message[1]);
                        break;
                    case CheckAgentFunds:
                        auctionHouse.setHasFunds((boolean) message[1]);
                        synchronized (auctionHouse) {
                            auctionHouse.notifyAll();
                        }
                        break;
                    case SetBalance:
                        if (agent != null) {
                            agent.changeBalance((Double) message[1]);
                        }
                        else if (auctionHouse != null) {
                            auctionHouse.setBalance((Double) message[1]);
                        }
                        break;
                    case BlockFunds:
                        synchronized (auctionHouse) {
                            auctionHouse.notifyAll();
                        }
                        break;
                    case UnlockFunds:
                        synchronized (auctionHouse) {
                            auctionHouse.notifyAll();
                        }
                        break;
                }
                Thread.sleep(0);
            }
            clientSocket.close();
        } catch (IOException e) {
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes the specified object to ObjectOutputStream.
     * @param arr of objects
     * @throws IOException exception
     */
    public void writeClientOutput(Object[] arr) throws IOException {
        clientOutput.writeObject(arr);
    }

    /**
     * This method is used by the bank's client to connect to the bank's server's IP.
     * It uses a scanner to scan the Configuration file and return the IP address that has been
     * entered for the bank server.
     * @return - ip address for the bank server
     * @throws FileNotFoundException
     */
    private String getServerIP() throws FileNotFoundException {
        Scanner in = new Scanner(new File("resources/Configuration"));
        while (in.hasNextLine()) {
            Scanner scanner = new Scanner(in.nextLine());
            if (scanner.hasNext() && scanner.next().equals("BankIP")) {
                return scanner.next();
            }
        }
        return null;
    }
}
