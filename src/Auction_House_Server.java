import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class Auction_House_Server extends Thread{

    String auctionHouseID;
    LinkedList<Item> itemList;
    LinkedList<String> nouns;
    LinkedList<String> adjectives;

    Boolean DEBUG = false;


    public Auction_House_Server(String auctionHouseID, LinkedList nouns, LinkedList adjectives) {
        this.auctionHouseID = auctionHouseID;
        this.itemList = new LinkedList<>();
        this.nouns = nouns;
        this.adjectives = adjectives;
        createItems(10);
    }
















    //upon creation, registers with bank by opening account with zero balance
    public void createAccount(){

    }

    //closes account at termination
    public void closeAccount(){

    }

    //recives bid and acknowledges with a reject or accept response
    public void validateBid(){

    }

    //When bid is accepted, bank is requested to block the funds
    public void acceptedBid(){
        //block the funds

    }
    //when a bid is overtaken, pass notification is sent to the agent and the funds are unblocked from the bank
    public void bidOvertaken(){
    }

    public void bidSuccessful(){
        //a bid is successful if not overtaken in 30 seconds
        //
    }
    //when winning a bid, agent receives "winner" notification and auction house waits for
    //the blocked funds to be transferred into its account
    public void wonAuction(){

    }





    private void createItems(int amountOfItems) {
        String itemID = "0000";
        Random random = new Random();
        for (int i = 0; i < amountOfItems; i++) {
            int tempItemID = Integer.valueOf(itemID);
            tempItemID++;
            itemID = String.format("%04d", tempItemID);
            String tempItem = adjectives.get(random.nextInt(adjectives.size() - 1)) + " " +  nouns.get(random.nextInt(nouns.size() - 1));
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





}