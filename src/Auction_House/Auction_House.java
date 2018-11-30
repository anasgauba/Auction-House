package Auction_House;

import Agent.Agent_Client_Proxy;
import Bank.Bank_Client_Proxy;
import Bank.Bank_Server_Proxy;
import Misc.Command;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class Auction_House extends Thread {

    LinkedList<Item> itemList;
    LinkedList<String> nouns;
    LinkedList<String> adjectives;
    Boolean DEBUG = false;

    public int auctionHouseID;
    public int portNumber;
    int secretKey;
    boolean run;
    Auction_House_Server_Proxy auction_house_server_proxy;
    Bank_Server_Proxy bank_server_proxy;
    ConcurrentHashMap<Integer, Agent_Client_Proxy> clients;
    Bank_Client_Proxy bankClient;

    public Auction_House(int portNumber, LinkedList nouns, LinkedList adjectives) throws IOException {
        this.portNumber = portNumber;
        this.auctionHouseID = new Random().nextInt(1000000000);
        this.itemList = new LinkedList<>();
        this.nouns = nouns;
        this.adjectives = adjectives;
        createItems(10);
        this.auction_house_server_proxy = new Auction_House_Server_Proxy(this, portNumber);
        this.clients = new ConcurrentHashMap();
        this.bankClient = new Bank_Client_Proxy(this, auctionHouseID,"AuctionHouse " + portNumber,7277); //bank
        this.run = true;
        start();

        //temp debug
        itemList.get(0).startBidTime();
    }

    public void run () {
        //itemList.get(0).startBidTime();
        //itemList.get(0).setCurrentBidder(12340);
        while (run) {
            bidSuccessfulCheck();
        }
    }

    //sets bidding key returned by the bank to the current agent
    public void setKey(int secretKey){
        this.secretKey = secretKey;
    }

    //upon creation, registers with bank by opening account with zero balance
    public void createAccount() {
//        Enum_Commands.Misc.Command createAccount = Enum_Commands.Misc.Command.CreateBankAccount;

    }

    //closes account at termination
    public void closeAccount() {
//        Enum_Commands.Misc.Command closeAccount = Enum_Commands.Misc.Command.CloseBankAccount;


    }

    //recives bid and acknowledges with a reject or accept response
    public void validateBid() {
//        Enum_Commands.Misc.Command acceptResponse = Enum_Commands.Misc.Command.AcceptResponse;
//        Enum_Commands.Misc.Command rejectResponse = Enum_Commands.Misc.Command.RejectResponse;
    }

    //When bid is accepted, bank is requested to block the funds
    public void acceptedBid() {
//        Enum_Commands.Misc.Command blockFunds = Enum_Commands.Misc.Command.BlockFunds;
    }

    //when a bid is overtaken, pass notification is sent to the agent and the funds are unblocked from the bank
    public void bidOvertaken() {
//        Enum_Commands.Misc.Command bidOvertaken = Enum_Commands.Misc.Command.BidOvertaken;
    }

    public void bidSuccessfulCheck() {
        for (int i = 0; i < itemList.size(); i++) {
            long secondsRemaining = TimeUnit.MILLISECONDS.toSeconds(itemList.get(i).getBidTimeRemaining() - System.currentTimeMillis());
            if (secondsRemaining < 1 && secondsRemaining >= 0) {
                //System.out.println("Times up!");
                Object[] message = {Command.WinMessage};
                /*try {
                    clients.get(itemList.get(i).getSecretBidderKey()).clientOutput.writeObject(message); //send msg to agent that won
                    //System.out.println("12340 won!");
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            }
            if (secondsRemaining > 0) {
                //System.out.println(secondsRemaining);
            }
        }
    }

    //when winning a bid, agent receives "winner" notification and auction house waits for
    //the blocked funds to be transferred into its account
    public void wonAuction() {
//        Enum_Commands.Misc.Command wonAuction = Enum_Commands.Misc.Command.WinMessage;
    }

    public void sendBid(String itemID, double bidAmount) {


        for (int i = 0; i < itemList.size(); i++) {
            if (itemID.equals(itemList.get(i).getItemID())) {
                //do item logic here, initialize time if hasnt been intitialized yet,
                //update current bid time if going,
                //check if they have .01 over the current bid, if so lock in bidder secret key,
                //add secret key to parameters


            }
        }
    }

    private void createItems(int amountOfItems) {
        String itemID = "0000";
        Random random = new Random();
        for (int i = 0; i < amountOfItems; i++) {
            int tempItemID = Integer.valueOf(itemID);
            tempItemID++;
            itemID = String.format("%04d", tempItemID);
            String tempItem = adjectives.get(random.nextInt(adjectives.size() - 1)) + " " + nouns.get(random.nextInt(nouns.size() - 1));
            //double minimumBidAmount = random.nextInt(200) + 5;
            //minimumBidAmount += random.nextFloat(99.99) / 100;
            double maxNum = 200;
            double minNum = 10;
            double minimumBidAmount = Math.floor(random.nextDouble() * (maxNum - minNum) * 100) / 100;
            double currentBidAmount = minimumBidAmount;

            itemList.add(new Item(this.auctionHouseID, itemID, tempItem, minimumBidAmount, currentBidAmount));
        }

        if (DEBUG) {
            for (int i = 0; i < amountOfItems; i++) {
                System.out.println(itemList.get(i));
            }
        }
    }

    public LinkedList<Item> getItemList() {

        return itemList;
    }

    public void startAuctionHouseClient(String data) {
        System.out.println("Starting AH client: " + data);
        String[] clientInfoTokens = data.split("\\s");
        Agent_Client_Proxy proxyClient = new Agent_Client_Proxy(Integer.valueOf(clientInfoTokens[3]), clientInfoTokens[0], Integer.valueOf(clientInfoTokens[1]));
        clients.put(Integer.valueOf(clientInfoTokens[3]), proxyClient);
        System.out.println("AH clients " + clients);
    }

    public void debug() throws IOException {
        Object[] message = {Command.BlockFunds, "test2"};
        clients.get(12340).clientOutput.writeObject(message);
        bankClient.clientOutput.writeObject(message);
    }
}