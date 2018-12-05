/*Anas Guaba, Clarissa Garcia, and Nathan Schaefer
 * Auction_House_Client_Proxy Class
 * <p>
 *
 * @author Nathan Schaefer
 *
 * Auction_House_Client_Proxy is the class that is used to receive messages back directly from the other objects
 * and rely them to the servers that will call the requested object's methods. It also includes the client sockets
 * and the ObjectInput and Object output streams to relay array object messages to the servers. The client also
 * includes its client type and its own port number. AuctionHouseClient in particular has an agent that it houses
 * as its own personal client
 */

package Auction_House;
import Agent.Agent;
import Misc.Command;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Auction_House_Client_Proxy is the class that is used to receive messages back directly from the other objects
 ** and rely them to the servers that will call the requested object's methods. It also includes the client sockets
 *and the ObjectInput and Object output streams to relay array object messages to the servers. The client also
 * includes its client type and its own port number. AuctionHouseClient in particular has an agent that it houses
 * as its own personal client
 */
public class Auction_House_Client_Proxy extends Thread {
    private Socket clientSocket;
    public ObjectInputStream clientInput;
    public ObjectOutputStream clientOutput;
    Agent agent;
    String clientType;
    int portNumber;
    int key;
    boolean run;

    /**
     * This constructor is used in creation of a bank client
     * @param key - the key of the bank
     * @param clientType - the type of client trying to connect
     * @param portNumber- the port number that auction house server will be hosted at
     */
    public Auction_House_Client_Proxy(int key, String clientType, int portNumber){
        this.clientType = clientType;
        this.portNumber = portNumber;
        this.run = true;
        start();
    }

    /**
     * This constructor is used in creation of an agent client
     * @param agent - the agent
     * @param key - the agent's secret bidding key
     * @param clientType - the type of client that will try to connect
     * @param portNumber - the port number that auction house server will be hosted at
     */
    public Auction_House_Client_Proxy(Agent agent, int key, String clientType, int portNumber) {
        this.agent = agent;
        this.key = key;
        this.clientType = clientType;
        this.portNumber = portNumber;
        this.run = true;
        start();
    }

    /**
     * The run method of the Auction House Client Proxy thread is used to send a message
     * to its server to give information about the port number it’s connecting to,
     * where it’s connecting from, and the client type. Since messages are written
     * in arrays that have the first object as the enum Command, a switch is then
     * used on the first object to determine what commands to call on the server.
     * The other objects in the array are used for the paramaters of the methods that
     * will soon be called. Auction House client handles messages from agent and writes
     * to its output to send to auction house Server.
     */
    public void run() {
        try {
            clientSocket = new Socket(getServerIP(), portNumber);
            clientOutput = new ObjectOutputStream(clientSocket.getOutputStream());
            clientOutput.writeObject(clientType + " " + clientSocket.getPort() + " " + key);
            clientOutput.writeObject(new Object[] {Command.TimeOffSet});
            synchronized (agent) {
                agent.notifyAll();
            }
            while (run && clientSocket.isConnected()) {
                if (clientInput == null) {
                    clientInput = new ObjectInputStream(clientSocket.getInputStream());
                }
                Object[] message = (Object[]) clientInput.readObject();
                Command command = (Command) message[0];
                switch (command) {
                    case SetListItems:
                        agent.createItemList((LinkedList<Item>) message[1]);
                        break;
                    case SendBid:
                        agent.printDetermination((Command)message[1], (Item) message[2]);
                        break;
                    case TimeOffSet:
                        agent.setTimeOffSet((Long) message[1]);
                        break;
                }
            }
            clientSocket.close();
    } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
        }
    }

    /**
     * This method is used by the auction house's client to connect to the auction house's server's IP.
     *It uses a scanner to scan the Configuration file and return the IP address that has been
     * entered for the auction house's server.
     * @return - IP address of the auction house server's
     * @throws FileNotFoundException
     */
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
