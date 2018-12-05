/*Anas Guaba, Clarissa Garcia, and Nathan Schaefer
 * Agent_Server_Proxy Class
 * <p>
 *
 * @author Nathan Schaefer
 *
 * Agent Server is the class that is used to receive messages  from agent client and relate the information received
  * back to the original object who requested the information that was originally asked by the other objects.
 */


package Agent;
import Auction_House.Item;
import Misc.Command;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The Agent server proxy class holds the auction house object it will manipulate and the socket
 * components necessary to connect the server to the clients that attempt to communicate with it.
 */
public class Agent_Server_Proxy extends Thread {
    private ServerSocket serverSocket;
    private Socket acceptSocket;
    private Agent agent;
    int portNumber;
    boolean run;

    //add port number to constructor so we can create multiple agents.

    /**
     * The agent server  is created by the constructor by being passed in the agent object holding
     * all of the methods and the port number in which the agent server will be hosted at
     * @param agent
     * @param portNumber
     */
    public Agent_Server_Proxy(Agent agent, int portNumber) {
        this.run = true;
        this.agent = agent;
        this.portNumber = portNumber;
        start();
        startAgentServer();
    }

    /**
     * The startAgentServer method starts the server by creating the server socket using
     * the port that was inputted to start the auction house server on. While the thread is running,
     * it attempts to continue accepting the socket and calls the startagentThread method, which
     * will read in objects and interpret the commands to make on agent object.
     */
    public void startAgentServer() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(portNumber);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (true) {
                try {
                    acceptSocket = serverSocket.accept();
                    startAgentThread(acceptSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * The startagentThread method is in charge of receiving messages from the other objects
     * and calling methods in bank to fulfill the requests for the information. It then sends the
     *  messages back to the clients of the objects who originally requested that information. it uses the
     *  server socket passed in to connect the object input and output streams and then waits for commands passed
     *  into it from other objects to fulfill the requests. The switch statement is made on the command, which is
     *  always found on the first object of the array and the methods are called depending on whcih command has been
     *  passed in.
     * @param clientSocket
     */
    public void startAgentThread(Socket clientSocket) {
        new Thread(() -> {
            try {
                ObjectOutputStream serverOutput;
                ObjectInputStream serverInput;
                serverOutput = new ObjectOutputStream(clientSocket.getOutputStream());
                serverInput = new ObjectInputStream(clientSocket.getInputStream());
                String temp = (String) serverInput.readObject();
                while (run && clientSocket.isConnected()) {
                    Object[] message = (Object[]) serverInput.readObject();
                    Command command = (Command) message[0];
                    switch (command) {
                        case RefreshTimes:
                            agent.refreshItems();
                            break;
                        case BidOvertaken:
                            agent.printDetermination((Command) message[0], (Item) message[1]);
                            break;
                        case WinMessage:
                            agent.printDetermination((Command) message[0], (Item) message[1]);
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
