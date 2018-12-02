package Bank;

import Misc.Command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Bank_Server_Proxy {

    private ServerSocket serverSocket;
    private Socket acceptSocket;

    private Bank bank;

    boolean auctionHouseClient;
    int portNumber;
    boolean run;

    public Bank_Server_Proxy(Bank bank, int portNumber) {
        this.auctionHouseClient = true;
        this.portNumber = portNumber;
        this.bank = bank;
        this.run = true;
        startBankServer();
    }


    public void startBankServer() {
        System.out.println("starting bank thread");
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

    public void startBankThread(Socket clientSocket) {
        new Thread(() -> {
            try {
                ObjectOutputStream serverOutput;
                ObjectInputStream serverInput;
                serverOutput = new ObjectOutputStream(clientSocket.getOutputStream());
                serverInput = new ObjectInputStream(clientSocket.getInputStream());

                String temp = (String) serverInput.readObject();
                if (temp.contains("AuctionHouse")) {
                    //bank.startBankClient(temp);
                }


                while (run && clientSocket.isConnected()) {

                    Object[] message = (Object[]) serverInput.readObject();
                    Command command = (Command) message[0];

                    //this switch handles incoming messages.  NOT from client, but from what ever
                    //button the user on the gui will press or from the agent thread
                    //Clients switch will be for INCOMING messages from Server or the ser
                    switch (command) {

                        case AddAuctionHouseID:
                            System.out.println("adding to list! " + message[1]);
                            bank.auctionHouseIDList.add((Integer) message[1]);
                            bank.auctionHousePorts.put(message[1], message[2]);
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
                            System.out.println("Current balance2 " +currBal);
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
                    }

                    serverOutput.reset();
                }

            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
