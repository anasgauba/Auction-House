/*Anas Guaba, Clarissa Garcia, and Nathan Schaefer
 * Agent_Client_Proxy Cass
 * <p>
 *
 * @author Nathan Schaefer
 *
 * Agent Client Proxy is the class that is used to receive messages back directly from the other objects
 * and rely them to the servers that will call the requested object's methods. It also includes the client sockets
 * and the ObjectInput and Object output streams to relay array object messages to the servers. The client also
 * includes its client type and its own port number.
 */
package Agent;

import Misc.Command;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *Agent Client Proxy is the class that is used to receive messages back directly from the other objects
 * and rely them to the servers that will call the requested object's methods. It also includes the client sockets
 * and the ObjectInput and Object output streams to relay array object messages to the servers. The client also
 * includes its client type and its own port number.
 */
public class Agent_Client_Proxy extends Thread {
    private Socket clientSocket;
    public ObjectInputStream clientInput;
    public ObjectOutputStream clientOutput;
    String clientType;
    int portNumber;
    boolean run;

    /**
     * The agentClientProxy constructor is used to inform the agent's server of its new
     * client type and its port number
     * @param key - the agent's secret bidding key
     * @param clientType - the type of client the server will receive
     * @param portNumber - the port number that agent client will connect to
     */
    public Agent_Client_Proxy(int key, String clientType, int portNumber) {
        this.clientType = clientType;
        this.portNumber = portNumber;
        this.run = true;
        start();
    }

    /**
     * The run method of the Agent Client Proxy thread is used to send a message
     * to its server to give information about the port number it’s connecting to,
     * where it’s connecting from, and the client type. Since messages are written
     * in arrays that have the first object as the enum Command, a switch is then
     * used on the first object to determine what commands to call on the server.
     * The other objects in the array are used for the paramaters of the methods that
     * will soon be called. Agent client handles messages from auction house and writes
     * to its output to send to agent Server.
     */
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
                    case WinMessage:
                        break;
                    case BidOvertaken:
                        clientOutput.writeObject(new Object[] {Command.BidOvertaken});
                        break;
                    case RefreshTimes:
                        clientOutput.writeObject(new Object[] {Command.RefreshTimes});
                }
                Thread.sleep(0);
            }
            clientSocket.close();
        } catch (InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
        }
    }

    /**
     * This method is used by the agent's client to connect to the agent server's IP.
     * It uses a scanner to scan the Configuration file and return the IP address that has been
     * entered for the Agent's server.
     * @return - IP address for the agent's server
     * @throws FileNotFoundException
     */
    private String getServerIP() throws FileNotFoundException {
        Scanner in = new Scanner(new File("resources/Configuration"));
        while (in.hasNextLine()) {
            Scanner scanner = new Scanner(in.nextLine());
            if (scanner.hasNext() && scanner.next().equals("AgentIP")) {
                return scanner.next();
            }
        }
        return null;
    }
}
