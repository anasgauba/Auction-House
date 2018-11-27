import javafx.stage.Stage;

import java.util.concurrent.ConcurrentHashMap;

public class Agent {
    private String agentName;
    private int secretBiddingKey;

    private Agent_Display agentDisplay;
    //Stage agentStage = new Stage();

    Agent_Server_Proxy agent_server_proxy;
    Client_Proxy bankClient;
    int portNumber;
    ConcurrentHashMap<Integer, Client_Proxy> clients;


    public Agent (int portNumber){
        this.portNumber = portNumber;
        this.clients = new ConcurrentHashMap();
        this.agent_server_proxy = new Agent_Server_Proxy(this, portNumber);
        this.bankClient = new Client_Proxy(1, "Agent " + this.portNumber, 7277);
        secretBiddingKey = 12340; //do function call for client to get bank key
        createClientConnections();
        //agentDisplay = new Agent_Display();
        //agentDisplay.drawGUI(new Stage());
    }

    //sets bidding key returned by the bank to the current agent
    public void setBiddingKey(int bidKeyReturned){
        secretBiddingKey=bidKeyReturned;
    }
    //create bank account and receive unique account number
    public void getBankAccountNumber(){
//        Enum_Commands.Command createAccount = Enum_Commands.Command.CreateBankAccount;
        //clientProxy.clientOutput.println("sending message: "+createAccount);

    }
    //Return a list of auction houses
    public void getListActiveAuctions(){
//        Enum_Commands.Command getListHouse = Enum_Commands.Command.GetListHouses;
        //clientProxy.clientOutput.println("sending message: "+getListHouse);


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
    public void closeStagnantBankAccount(){
//        Enum_Commands.Command closeAccount = Enum_Commands.Command.CloseBankAccount;
        //clientProxy.clientOutput.println("sending message: "+closeAccount);

    }

    //need method that gets ah servers from bank, and then creates multiple clients to each one.
    //loop each client creation here.
    private void createClientConnections() {
        //for all lists in the server hash map create a new client
        //we also need a list of clients. Here is one example of one agent.
        //In order to do this, we would need to message the bank proxy command to get all AH ids + ports

        Client_Proxy clientProxy = new Client_Proxy(12340, "Agent " + portNumber, 6666); //key would be auction house id, to do
        clients.put(12340, clientProxy);

    }

    public void debug() {
        clients.get(12340).clientOutput.println("Agent to AH message");
        bankClient.clientOutput.println("Agent to Bank message");
    }

}
