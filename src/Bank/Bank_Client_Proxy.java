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
    public ObjectInputStream clientInput = null;
    public ObjectOutputStream clientOutput = null;

    Agent agent;
    Auction_House auctionHouse;
    String clientType;
    int key;

    int portNumber;
    boolean run;

    public Bank_Client_Proxy(Auction_House auctionHouse, int key, String clientType, int portNumber) {
        this.auctionHouse = auctionHouse;
        this.clientType = clientType;
        this.key = key;
        this.portNumber = portNumber;
        this.run = true;
        start();
    }

    public Bank_Client_Proxy(Agent agent, int key, String clientType, int portNumber) {
        this.agent = agent;
        this.key = key;
        this.clientType = clientType;
        this.portNumber = portNumber;
        this.run = true;
        start();
    }

    public void run() {

        try {
            clientSocket = new Socket(getServerIP(), portNumber);
            clientOutput = new ObjectOutputStream(clientSocket.getOutputStream());
            clientOutput.writeObject(clientType + " " + clientSocket.getPort());

             //This puts a command from bank's client to bank server to make bank call the method
//          s  System.out.println(this.agent.agentName);
//            System.out.println("HHHHHHHHHHHHH"+clientType);
            if (clientType.contains("Agent")){
                System.out.println("FLAG");
//                int randomBalance = new Random().nextInt(10000)+1000;
//                System.out.println("Random balance: "+randomBalance);
                clientOutput.writeObject(new Object[] {Command.CreateBankAccount, agent.agentName, 2000.0, "Agent"});
//                clientOutput.writeObject(new Object[]{Command.CreateBankAccount,"Sam", 3333.0, "Agent"});

            }

            else if (clientType.contains("AuctionHouse")) {
                System.out.println("Bank.Bank client for AH key: " + key);
                clientOutput.writeObject(new Object[] {Command.AddAuctionHouseID, auctionHouse.auctionHouseID, auctionHouse.portNumber});
                System.out.println("ah debug " + Command.AddAuctionHouseID + " " + auctionHouse.auctionHouseID + " " + auctionHouse.portNumber);
                clientOutput.writeObject(new Object[] {Command.CreateBankAccount, String.valueOf(key), 0.0, "AuctionHouse"});

            }




            while (run && clientSocket.isConnected()) {

                if (clientInput == null) {
                    clientInput = new ObjectInputStream(clientSocket.getInputStream());
                }

                Object[] message = (Object[]) clientInput.readObject();
                Command command = (Command) message[0];

                switch (command) {

                    case BlockFunds:
                        System.out.println("test! in bank client! " + message[1]);
                        break;

                    case SetListHouses:
                        agent.createHouseList((LinkedList<Integer>) message[1], (ConcurrentHashMap<Integer, Integer>) message[2]);
                        break;

                    case SetAgentKey:
                        Object[] bankInfo = (Object [])message[1];
                        System.out.println("Message in bank client is: "+message);
                        agent.setBiddingKey((Integer) bankInfo[2]);
                        System.out.println("Account ID is "+bankInfo[0]);
                        System.out.println("Initial balance is "+bankInfo[1]);
                        System.out.println("Secret key is "+bankInfo[2]);
                        agent.setAgentDisplayValues((int)bankInfo[0],(double)bankInfo[1]);
//                        System.out.println("Agent.Agent key" +message[1]);
                        break;

                    case SetAuctionHouseKey:
                        auctionHouse.setKey((Integer) message[1]);
                        System.out.println("AH key: " + message[1]);
                        break;

                    case CheckAgentFunds:
                        auctionHouse.setHasFunds((boolean) message[1]);
                        synchronized (auctionHouse) {
                            auctionHouse.notifyAll();
                        }
                        break;
                    case SetBalance:
                        agent.changeBalance((Double) message[1]);
                        break;
                }
                //System.out.println("message received: " + message);
                Thread.sleep(0);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

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
