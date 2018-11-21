import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Agent extends Thread {
    private String agentName;
    private int secretBiddingKey;

    private Agent_Display agentDisplay;
//    Stage agentStage = new Stage();

    Auction_House_Proxy auctionHouseProxy;


    public Agent (){
        //agentDisplay = new Agent_Display();
        //agentDisplay.drawGUI(new Stage());
        this.auctionHouseProxy = new Auction_House_Proxy();
    }

    //sets bidding key returned by the bank to the current agent
    public void setBiddingKey(int bidKeyReturned){
        secretBiddingKey=bidKeyReturned;
    }
    //create bank account and receive unique account number
    public void getBankAccountNumber(){
        Enum_Commands.Command createAccount = Enum_Commands.Command.CreateBankAccount;
        auctionHouseProxy.output.println("sending message: "+createAccount);

    }
    //Return a list of auction houses
    public void getListActiveAuctions(){
        Enum_Commands.Command getListHouse = Enum_Commands.Command.GetListHouses;
        auctionHouseProxy.output.println("sending message: "+getListHouse);

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
        Enum_Commands.Command transferFunds = Enum_Commands.Command.TransferBlockedFunds;
        auctionHouseProxy.output.println("sending message: "+transferFunds);
    }

    //terminates and closes the agent's bank account when no bidding action is in progress
    public void closeStagnantBankAccount(){
        Enum_Commands.Command closeAccount = Enum_Commands.Command.CloseBankAccount;
        auctionHouseProxy.output.println("sending message: "+closeAccount);



    }

}
