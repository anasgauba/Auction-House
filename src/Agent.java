import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Agent extends Thread {
    private String agentName;
    private int secretBiddingKey;

    boolean run;
    Thread thread;

    private Agent_Display agentDisplay;
    private Socket clientSocket;
    private BufferedReader input;
    private PrintStream output;
//    Stage agentStage = new Stage();

    public Agent (){
        agentDisplay = new Agent_Display();
        agentDisplay.drawGUI(new Stage());
        this.run = true;
        this.thread = new Thread(this);
        this.thread.start();
    }

    public void run(){
        try {
            clientSocket = new Socket ("localhost" , 7777);
            output = new PrintStream(clientSocket.getOutputStream());
            output.println("Hello server!");

            while (run && clientSocket.isConnected()) {
                input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String message = input.readLine();
                System.out.println(message);
                Thread.sleep(1000);
                output.println("second message!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
