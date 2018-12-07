/*Anas Guaba, Clarissa Garcia, and Nathan Schaefer
 * Bank Server Proxy Class
 * <p>
 *
 * @author Nathan Schaefer
 *
 * Bank Server Proxy is the class that is used to receive messages  from bank client and relate
 * the information received back to the original object who requested the information that was originally
 * asked by the other objects.
 */

package Bank;
import Misc.Command;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The bank Server proxy class holds the bank object, the auction houseclient boolean and the socket
 * components necessary to connect the server to the clients that attempt to communicate with it.
 */
public class Bank_Server_Proxy {
    private ServerSocket serverSocket;
    private Socket acceptSocket;
    private Bank bank;
    private boolean auctionHouseClient;
    private int portNumber;
    private boolean run;

    /**
     * The bank server constructor is created by being passed in the bank object holding
     * all of the methods and the port number in which the bank server will be hosted at
     * @param bank - bank object holding all of the methods server will manipulate
     * @param portNumber - port number in which the bank server will be hosted at
     */
    public Bank_Server_Proxy(Bank bank, int portNumber) {
        this.auctionHouseClient = true;
        this.portNumber = portNumber;
        this.bank = bank;
        this.run = true;
        startBankServer();
    }

    /**
     * The startBankServer method starts the server by creating the server socket using
     * the port that was inputted to start the bank server on. While the thread is running,
     * it attempts to continue accepting the socket and calls the startBankThread method, which
     * will read in objects and interpret the commands to make on Bank object.
     */
    public void startBankServer() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(portNumber);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (true) {
                try {
                    acceptSocket = serverSocket.accept();
                    startBankThread(acceptSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * The startBankThread method is in charge of receiving messages from the other objects
     * and calling methods in bank to fulfill the requests for the information. It then sends the
     * messages back to the clients of the objects who originally requested that information. it uses the
     * server socket passed in to connect the object input and output streams and then waits for commands passed
     * into it from other objects to fulfill the requests. The switch statement is made on the command, which is
     * always found on the first object of the array and the methods are called depending on whcih command has been
     * passed in.
     * @param clientSocket   - the socket created according to the port passed in to host the bank server
     */
    public void startBankThread(Socket clientSocket) {
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
                        case AddAuctionHouseID:
                            bank.auctionHouseIDList.add((Integer) message[1]);
                            bank.auctionHousePorts.put(message[1], message[2]);
                            break;
                        case CloseAuctionHouseID:
                            for (int i = 0; i < bank.auctionHouseIDList.size(); i++) {
                                if (bank.auctionHouseIDList.get(i).equals(message[1])) {
                                    bank.auctionHouseIDList.remove(i);
                                }
                            }
                            bank.auctionHousePorts.remove(message[1]);
                            break;
                        case GetListHouses:
                            serverOutput.writeObject(new Object[] {Command.SetListHouses, bank.auctionHouseIDList, bank.auctionHousePorts});
                            break;
                        case CreateBankAccount:
                            Object[] tempArray = bank.createAccount((String) message[1],(Double) message[2]);
                            if (message[3].equals("Agent")) {
                                serverOutput.writeObject(new Object[]{Command.SetAgentKey, tempArray});
                            }
                            else {
                                serverOutput.writeObject(new Object[]{Command.SetAuctionHouseKey, tempArray[2]});
                            }
                        break;
                        case CheckAgentFunds:
                            boolean tempResponse = bank.abilityToBuy((Integer) message[1], (Double) message[2]);
                            serverOutput.writeObject(new Object[] {Command.CheckAgentFunds, tempResponse});
                            break;
                        case GetBalance:
                            double currBal = bank.getBalance((Integer)
                                    message[1]);
                            serverOutput.writeObject(new Object[] {Command
                                    .SetBalance, currBal});
                            break;
                        case BlockFunds:
                            bank.lockBalance((Integer) message[1], (Double) message[2]);
                            serverOutput.writeObject(new Object[] {Command.BlockFunds});
                            break;
                        case UnlockFunds:
                            bank.unlockBalance((Integer) message[1], (Double) message[2]);
                            serverOutput.writeObject(new Object[] {Command.UnlockFunds});
                            break;
                        case TransferBlockedFunds:
                            bank.deposit((Integer) message[1], (Integer) message[2], (Double) message[3]);
                            break;
                        case CloseBankAccount:
                            bank.closeAccount((Integer) message[1]);
                            break;
                    }
                    if (!clientSocket.isClosed()) {
                        serverOutput.reset();
                    }
                }
                clientSocket.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
