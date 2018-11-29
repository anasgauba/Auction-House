import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Agent {
    LinkedList<String> names;
    private String agentName;
    private int secretBiddingKey;

    private Agent_Display agentDisplay;
    //Stage agentStage = new Stage();

    Agent_Server_Proxy agent_server_proxy;
    Bank_Client_Proxy bankClient;
    int portNumber;
    ConcurrentHashMap<Integer, Auction_House_Client_Proxy> clients;


    public Agent (int portNumber, LinkedList nameList){
        this.portNumber = portNumber;
        this.clients = new ConcurrentHashMap();
        this.agent_server_proxy = new Agent_Server_Proxy(this, portNumber);
        this.bankClient = new Bank_Client_Proxy(1, "Agent " + this.portNumber, 7277);
        secretBiddingKey = 12340; //do function call for client to get bank key
        this.names = nameList;
        createName();
        createClientConnections();
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
    //Return a list of auction houses
    public void getListActiveAuctions() throws IOException {
        Object[] message = {Command.GetListHouses};
        clients.get(12340).clientOutput.writeObject(message);
        bankClient.clientOutput.writeObject(message);
    }
    //retrieves auction items from auction house passed in
    public void getAuctionHouseItems(){
    }

    //uses the secret key to make a bid and received back acceptance, rejection, pass (higher bid in place)
    //and winner.
    public void placeBid(){
    }

    //notifies the bank to transfer the blocked funds to the auction house when winning a bid.
    public void transferWinningBidFunds(){
//        Enum_Commands.Command transferFunds = Enum_Commands.Command.TransferBlockedFunds;
        //clientProxy.clientOutput.println("sending message: "+transferFunds);
    }

    //terminates and closes the agent's bank account when no bidding action is in progress
    public void closeStagnantBankAccount() throws IOException {
        Object[] message = {Command.CloseBankAccount};
        clients.get(12340).clientOutput.writeObject(message);
        bankClient.clientOutput.writeObject(message);
    }

    public void createItemList(LinkedList<Item> itemList) {
        for (int i = 0; i < itemList.size(); i++) {
            Item tempItem = itemList.get(i);
            agentDisplay.listofTableItems.add(new Agent_Display.tableItem(new Item(tempItem.getAuctionHouseID(), tempItem.getItemID(),
                    tempItem.getDescription(), tempItem.getMinimumBidAmount(), tempItem.getCurrentBidAmount())));
        }
        agentDisplay.table.setItems(agentDisplay.listofTableItems);
    }

    //need method that gets ah servers from bank, and then creates multiple clients to each one.
    //loop each client creation here.
    private void createClientConnections() {
        //for all lists in the server hash map create a new client
        //we also need a list of clients. Here is one example of one agent.
        //In order to do this, we would need to message the bank proxy command to get all AH ids + ports

        Auction_House_Client_Proxy clientProxy = new Auction_House_Client_Proxy(this,12340, "Agent " + portNumber, 6666); //key would be auction house id, to do
        clients.put(12340, clientProxy);

    }

    public void debug() throws IOException {
        Object[] message = {Command.BlockFunds, "test2"};
        clients.get(12340).clientOutput.writeObject(message);
        bankClient.clientOutput.writeObject(message);

        Object[] message2 = {Command.GetListItems};
        clients.get(12340).clientOutput.writeObject(message2);

    }

}
