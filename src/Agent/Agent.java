package Agent;

import Auction_House.Auction_House_Client_Proxy;
import Auction_House.Item;
import Bank.Bank_Client_Proxy;
import Misc.Command;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class Agent extends Thread{
    LinkedList<String> names;
    public String agentName;
    private int secretBiddingKey;
    private int currentAuctionHouse;

    private Agent_Display agentDisplay;
    //Stage agentStage = new Stage();

    Agent_Server_Proxy agent_server_proxy;
    Bank_Client_Proxy bankClient;
    int portNumber;
    Boolean houseListDone = false;
    ConcurrentHashMap<Integer, Auction_House_Client_Proxy> clients;


    public Agent (int portNumber, LinkedList nameList){
        this.names = nameList;
        createName();
        this.portNumber = portNumber;
        this.clients = new ConcurrentHashMap();
        this.agent_server_proxy = new Agent_Server_Proxy(this, portNumber);
        this.bankClient = new Bank_Client_Proxy(this, 1, "Agent " + this.portNumber, 7277);
        start();
        agentDisplay = new Agent_Display(this);
        agentDisplay.drawGUI(new Stage());
    }

    public void createName(){
        Random random = new Random();
        String createdName =names.get(random.nextInt(names.size() - 1)) + " " + names.get(random.nextInt(names.size() - 1));
        this.agentName=createdName;
        System.out.println(this.agentName);
    }

    //sets bidding key returned by the bank to the current agent
    public void setBiddingKey(int bidKeyReturned){
        secretBiddingKey=bidKeyReturned;
    }
    //create bank account and receive unique account number
    public void getBankAccountNumber() throws IOException {
        Object[] message = {Command.CreateBankAccount};
        clients.get(12340).clientOutput.writeObject(message);
        bankClient.clientOutput.writeObject(message);
        //how is the information being received back to agent?
    }
//    //Return a list of auction houses
//    public void getListActiveAuctions() throws IOException {
//        Object[] message = {Misc.Command.GetListHouses};
//        clients.get(12340).clientOutput.writeObject(message);
//        bankClient.clientOutput.writeObject(message);
//    }
    //retrieves auction items from auction house passed in
    public void getAuctionHouseItems(){
    }

    //uses the secret key to make a bid and received back acceptance, rejection, pass (higher bid in place)
    //and winner.
    public void placeBid(){

    }

    public void setAgentDisplayValues(int accountID , double accountBalance){
        System.out.println("In display, account ID is "+accountID);
        System.out.println("In display, balance is "+accountBalance);

        synchronized (this) {
            while (agentDisplay == null) {
                try {
                    System.out.println("strtubg wait");
                    wait();
                    System.out.println("waitt");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    //e.printStackTrace();
                }
            }
        }
        System.out.println("we dib sleeepin");
        System.out.println("Does balance exist? "+agentDisplay.agentBalanceTextField);
        System.out.println("Does account exist? "+agentDisplay.agentAccountTextField);
        agentDisplay.setAccountBalance(accountBalance);
        agentDisplay.setAccountID(accountID);

    }

    //notifies the bank to transfer the blocked funds to the auction house when winning a bid.
    public void transferWinningBidFunds() throws IOException {
        Object[] message = {Command.TransferBlockedFunds};
        clients.get(12340).clientOutput.writeObject(message);
        bankClient.clientOutput.writeObject(message);
    }

    //terminates and closes the agent's bank account when no bidding action is in progress
    public void closeStagnantBankAccount() throws IOException {
        Object[] message = {Command.CloseBankAccount, this.secretBiddingKey};
        clients.get(12340).clientOutput.writeObject(message);
        bankClient.clientOutput.writeObject(message);
    }

    public void sendBid(String itemID, double bidAmount) throws IOException {
        System.out.println("Bid amount " + bidAmount);
        clients.get(currentAuctionHouse).clientOutput.writeObject(new Object[] {Command.SendBid, secretBiddingKey, itemID, bidAmount});


    }

    public void setCurrentAuctionHouse(int auctionHouseID) {
        System.out.println("current AH id " + auctionHouseID);
        currentAuctionHouse = auctionHouseID;
    }

    public void printDetermination(Command theDetermination){
        switch (theDetermination) {
            case WinMessage:
                agentDisplay.newLine+=" You won the bid!\n";
                Platform.runLater(() -> agentDisplay.setNewNotificationMessage());
                break;
            case BidOvertaken:
                agentDisplay.newLine+=" Your bid has been overtaken\n";
                Platform.runLater(() -> agentDisplay.setNewNotificationMessage());
                break;
            case RejectResponse:
                agentDisplay.newLine+=" You have lost the bid. Sorry!\n";
                Platform.runLater(() -> agentDisplay.setNewNotificationMessage());
                break;
        }

    }


    LinkedList<Agent_Display.tableItem> timeList = new LinkedList();
    LinkedList<Item> itemList = new LinkedList<>();
    public void createItemList(LinkedList<Item> itemList) {
        if (timeList != null) {
            System.out.println("Clearing time list");
            timeList.clear();
        }
        if (!this.itemList.isEmpty()) {
            System.out.println("Clearing item list");
            this.itemList.clear();
        }
        for (int i = 0; i < itemList.size(); i++) {
            Item tempItem = itemList.get(i);
            Agent_Display.tableItem temp = new Agent_Display.tableItem(tempItem);
            agentDisplay.listofTableItems.add(temp);
            timeList.add(temp);
            System.out.println("adding: " + itemList.get(i).getBidTimeRemaining());
            this.itemList.add(itemList.get(i));
            System.out.println("adding: 2 " + this.itemList.get(i).getBidTimeRemaining());

        }
        agentDisplay.table.setItems(agentDisplay.listofTableItems);
    }

    public void refreshItems() throws IOException {
        agentDisplay.listofTableItems.clear();
        clients.get(currentAuctionHouse).clientOutput.writeObject(new Object[] {Command.GetListItems});
    }

    public synchronized void refreshTimes() {
        for (int i = 0; i < timeList.size(); i++) {
            long secondsRemaining = TimeUnit.MILLISECONDS.toSeconds(itemList.get(i).getBidTimeRemaining() - System.currentTimeMillis());
            //System.out.println("time in agnet: " + Long.valueOf(timeList.get(i).getItemTime()));
            //System.out.println("? " + secondsRemaining);
            if (Long.valueOf(timeList.get(i).getItemTime()) > 0) {
                System.out.println("time in agnet: " + Long.valueOf(timeList.get(i).getItemTime()));
                timeList.get(i).setItemTime(Long.toString(secondsRemaining));
            }
        }


    }

    public void createHouseList(LinkedList<Integer> auctionHouseList, ConcurrentHashMap<Integer, Integer> auctionHousePorts) {
        System.out.println("in auction house list creator " + auctionHouseList);
        for (int i = 0; i < auctionHouseList.size(); i++) {
            String tempID =  Integer.toString(auctionHouseList.get(i));
            agentDisplay.options.add(tempID);

            if (clients.get(auctionHouseList.get(i)) == null) {
                System.out.println("starting new client! " + auctionHouseList.get(i) + auctionHousePorts.get(auctionHouseList.get(i)));
                System.out.println("starting new client! " + auctionHouseList.get(i) + auctionHousePorts);
                Auction_House_Client_Proxy clientProxy = new Auction_House_Client_Proxy(this, secretBiddingKey, "Agent " + portNumber, auctionHousePorts.get(auctionHouseList.get(i))); //key would be auction house id, to do

                clients.put(auctionHouseList.get(i), clientProxy);

                synchronized (this) {
                    while (clients.get(auctionHouseList.get(i)).clientOutput == null) {
                        try {
                            wait();
                            System.out.println("Client created for gent");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        agentDisplay.getHouseListDone = true;
        synchronized (agentDisplay) {
            agentDisplay.notifyAll();
        }
    }

    public void getHouseList() throws IOException {
        bankClient.clientOutput.writeObject(new Object[] {Command.GetListHouses});
    }

    public void debug() throws IOException {
        Object[] message = {Command.BlockFunds, "test2"};
        clients.get(12340).clientOutput.writeObject(message);
        bankClient.clientOutput.writeObject(message);

        Object[] message2 = {Command.GetListItems};
        clients.get(12340).clientOutput.writeObject(message2);

        getHouseList();

    }

}
