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



    //sets bidding key returned by the bank to the ucrrent agent
    public void setBiddingKey(int bidKeyReturned){
        secretBiddingKey=bidKeyReturned;
    }
    public void getBankAccountNumber(){
        //create bank account and receive unique account number
    }
    //Return a list of auction houses
    public void getListActiveAuctions(){

        auctionHouseProxy.output.println("sending message!");

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
    }

    //terminates and closes the agent's bank account when no bidding action is in progress

    public void closeStagnantBankAccount(){
    }

}
