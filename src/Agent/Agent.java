package Agent;

import Auction_House.Auction_House_Client_Proxy;
import Auction_House.Item;
import Bank.Bank_Client_Proxy;
import Misc.Command;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Agent {
    LinkedList<String> names;
    public String agentName;
    private int secretBiddingKey;
    private int currentAuctionHouse;

    private Agent_Display agentDisplay;
    //Stage agentStage = new Stage();

    Agent_Server_Proxy agent_server_proxy;
    Bank_Client_Proxy bankClient;
    int portNumber;
    ConcurrentHashMap<Integer, Auction_House_Client_Proxy> clients;


    public Agent (int portNumber, LinkedList nameList){
        this.names = nameList;
        createName();
        this.portNumber = portNumber;
        this.clients = new ConcurrentHashMap();
        this.agent_server_proxy = new Agent_Server_Proxy(this, portNumber);
        this.bankClient = new Bank_Client_Proxy(this, 1, "Agent " + this.portNumber, 7277);
        secretBiddingKey = 12340; //do function call for client to get bank key
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
        clients.get(currentAuctionHouse).clientOutput.writeObject(new Object[] {Command.SendBid, itemID, bidAmount});
    }

    public void setCurrentAuctionHouse(int auctionHouseID) {
        System.out.println("current AH id " + auctionHouseID);
        currentAuctionHouse = auctionHouseID;
    }



    public void createItemList(LinkedList<Item> itemList) {
        for (int i = 0; i < itemList.size(); i++) {
            Item tempItem = itemList.get(i);
            agentDisplay.listofTableItems.add(new Agent_Display.tableItem(new Item(tempItem.getAuctionHouseID(), tempItem.getItemID(),
                    tempItem.getDescription(), tempItem.getMinimumBidAmount(), tempItem.getCurrentBidAmount())));
        }
        agentDisplay.table.setItems(agentDisplay.listofTableItems);
    }

    public void createHouseList(LinkedList<Integer> auctionHouseList, ConcurrentHashMap<Integer, Integer> auctionHousePorts) {
        System.out.println("in auction house list creator " + auctionHouseList);
        for (int i = 0; i < auctionHouseList.size(); i++) {
            String tempID =  Integer.toString(auctionHouseList.get(i));
            agentDisplay.options.add(tempID);

            if (clients.get(auctionHouseList.get(i)) == null) {
                System.out.println("starting new client! " + auctionHouseList.get(i) + auctionHousePorts.get(auctionHouseList.get(i)));
                System.out.println("starting new client! " + auctionHouseList.get(i) + auctionHousePorts);
                Auction_House_Client_Proxy clientProxy = new Auction_House_Client_Proxy(this, auctionHouseList.get(i), "Agent " + portNumber, auctionHousePorts.get(auctionHouseList.get(i))); //key would be auction house id, to do

                clients.put(auctionHouseList.get(i), clientProxy);
            }
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
