import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Bank_Server_Proxy {

    private ServerSocket serverSocket;
    private Socket acceptSocket;

    private Bank bank;

    boolean auctionHouseClient;
    boolean run;

    public Bank_Server_Proxy(Bank bank) {
        this.auctionHouseClient = true;
        this.run = true;
        this.bank = bank;
        startBankServer();
    }


    public void startBankServer() {
        System.out.println("starting bank thread");
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(7277);
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
                    bank.startBankClient(temp);
                }


                while (run && clientSocket.isConnected()) {

                    Object[] message = (Object[]) serverInput.readObject();
                    Command command = (Command) message[0];

                    //this switch handles incoming messages.  NOT from client, but from what ever
                    //button the user on the gui will press or from the agent thread
                    //Clients switch will be for INCOMING messages from Server or the ser
                    switch (command) {

                        case BlockFunds:
                            System.out.println("test in bank serv! " + message[1] + command);
                            serverOutput.writeObject(message);
                            break;

                        case AddAuctionHouseID:
                            System.out.println("adding to list! " + message[1]);
                            bank.auctionHouseIDList.add((Integer) message[1]);
                            bank.auctionHousePorts.put(message[1], message[2]);
                            break;

                        case GetListHouses:
                            serverOutput.writeObject(new Object[] {Command.SetListHouses, bank.auctionHouseIDList, bank.auctionHousePorts});
                            break;
                        case CreateBankAccount:
//                        Object[] tempArray = bank.createAccount((String)message[1],(Double)message[2]);
//                        serverOutput.writeObject(new Object[]{Command.SetKey,tempArray[2]});
                    }

                    Thread.sleep(0);
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
