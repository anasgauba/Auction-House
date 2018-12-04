package Auction_House;

import Agent.Agent_Client_Proxy;
import Bank.Bank_Client_Proxy;
import Bank.Bank_Server_Proxy;
import Misc.Command;
import javafx.application.Platform;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class Auction_House extends Thread {

    Auction_House_Display display;
    LinkedList<Item> itemList;
    LinkedList<String> nouns;
    LinkedList<String> adjectives;
    Boolean DEBUG = false;

    public int auctionHouseID;
    public int portNumber;
    public int bankPortNumber;
    public double auctionHouseBalance;
    int secretKey;
    boolean run;
    boolean hasFunds;
    Auction_House_Server_Proxy auction_house_server_proxy;
    Bank_Server_Proxy bank_server_proxy;
    ConcurrentHashMap<Integer, Agent_Client_Proxy> clients;
    Bank_Client_Proxy bankClient;

    public Auction_House(Auction_House_Display display, int portNumber, int bankPortNumber, LinkedList nouns, LinkedList adjectives) throws IOException {
        this.display = display;
        this.portNumber = portNumber;
        this.bankPortNumber = bankPortNumber;
        this.auctionHouseBalance = 0.0;
        this.auctionHouseID = new Random().nextInt(1000000000);
        this.itemList = new LinkedList<>();
        this.nouns = nouns;
        this.adjectives = adjectives;
        createItems(2);
        this.auction_house_server_proxy = new Auction_House_Server_Proxy(this, portNumber);
        this.clients = new ConcurrentHashMap();
        this.bankClient = new Bank_Client_Proxy(this, auctionHouseID,"AuctionHouse " + portNumber, bankPortNumber); //bank
        this.run = true;
        this.hasFunds = false;
        start();

        //temp debug
        //itemList.get(0).startBidTime();
    }

    public void run () {
        //itemList.get(0).startBidTime();
        //itemList.get(0).setCurrentBidder(12340);

        Platform.runLater(()-> display.auctionHouseID.setText(String.valueOf(auctionHouseID)));
        Platform.runLater(()-> display.itemsRemainingLabel.setText(String.valueOf(itemList.size())));
        Platform.runLater(()-> display.auctionHouseBalance.setText("0.00"));

        while (run) {
            bidSuccessfulCheck();
        }

        System.out.println("going to close AH");
    }

    //sets bidding key returned by the bank to the current agent
    public void setKey(int secretKey){
        this.secretKey = secretKey;
        for (int i = 0; i < itemList.size(); i++) {
            itemList.get(i).setAuctionHouseSecretKey(secretKey);
        }
    }

    //closes account at termination
    public void closeAccount() {
//        Enum_Commands.Misc.Command closeAccount = Enum_Commands.Misc.Command.CloseBankAccount;
    }

    public void bidSuccessfulCheck() {
        for (int i = 0; i < itemList.size(); i++) {
            long secondsRemaining = TimeUnit.MILLISECONDS.toSeconds(itemList.get(i).getBidTimeRemaining() - System.currentTimeMillis());
            if (secondsRemaining == 0 && itemList.get(i).getAuctionActive()) {
                System.out.println("Times up!");
                try {
                    Item tempItem = itemList.get(i);
                    tempItem.setAuctionActive(false);
                    clients.get(tempItem.getSecretBidderKey()).clientOutput.writeObject(new Object[] {Command.WinMessage, tempItem}); //send msg to agent that won
                    removeItemFromAuction(tempItem);
                    Iterator it = clients.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        clients.get(pair.getKey()).clientOutput.writeObject(new Object[] {Command.RefreshTimes/*, tempItem*/});
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public synchronized Object[] sendBid(int agentSecretKey, String itemID, double bidAmount) throws IOException {

        System.out.println("item looking for " + itemID);

        for (int i = 0; i < itemList.size(); i++) {
            if (itemID.equals(itemList.get(i).getItemID())) {
                Item item = itemList.get(i);
                //System.out.println("agents secret key : " + agentSecretKey);
                //do item logic here, initialize time if hasnt been intitialized yet,
                //update current bid time if going,
                //check if they have .01 over the current bid, if so lock in bidder secret key,
                //add secret key to parameters


                //check funds
                bankClient.clientOutput.writeObject(new Object[]{Command.CheckAgentFunds, agentSecretKey, bidAmount});
                synchronized (this) {
                    try {
                        wait();
                        System.out.println("after wait for check funds");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }



                System.out.println("has funds? " + hasFunds);
                if (hasFunds && item.getSecretBidderKey() != agentSecretKey) {
                    System.out.println("in has funds and sees that keys not bidder");

                    //lock balance
                    bankClient.clientOutput.writeObject(new Object[] {Command.BlockFunds, agentSecretKey, bidAmount});
                    synchronized (this) {
                        try {
                            wait();
                            System.out.println("after wait for lock funds");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }


                    //if item hasnt started yet:
                    System.out.println("current time in ah " + item.getBidTimeRemaining() + " item " + item.getDescription());
                    if (item.getBidTimeRemaining() == 0 && bidAmount >= item.getCurrentBidAmount()) {

                        item.startBidTime();
                        item.setSecretBidderKey(agentSecretKey);
                        item.setBidAmount(bidAmount);
                        System.out.println("secret key " + agentSecretKey + " " + clients);

                        Iterator it = clients.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry) it.next();
                            clients.get(pair.getKey()).clientOutput.writeObject(new Object[] {Command.RefreshTimes});
                        }
                        //clients.get(agentSecretKey).clientOutput.writeObject(new Object[] {Command.RefreshTimes});

                        System.out.println("time: " + (item.getBidTimeRemaining() - System.currentTimeMillis()));
                        System.out.println("I see the debug, time 0, item no started");


                        return new Object[] {Command.AcceptResponse, item};
                    }


                    //if item is going
                    else if (item.getBidTimeRemaining() > 0 && bidAmount > item.getCurrentBidAmount()) {


                        bankClient.clientOutput.writeObject(new Object[] {Command.UnlockFunds, item.getSecretBidderKey(), item.getCurrentBidAmount()});
                        synchronized (this) {
                            try {
                                wait();
                                System.out.println("after wait for unlock funds");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }




                        item.startBidTime();


                        Iterator it = clients.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry) it.next();
                            clients.get(pair.getKey()).clientOutput.writeObject(new Object[] {Command.RefreshTimes});
                            //bankClient.clientOutput.writeObject(new Object[] {Command.CheckAgentFunds, agentSecretKey, 1.0});
                        }

                        clients.get(item.getSecretBidderKey()).clientOutput.writeObject(new Object[] {Command.BidOvertaken, item});

                        item.setSecretBidderKey(agentSecretKey);
                        item.setBidAmount(bidAmount);


                        return new Object[] {Command.AcceptResponse, item};
                    }



                }

                System.out.println(itemList);




                //case reject bid
                if (!hasFunds) {

                    return new Object[] {Command.RejectResponse, null};
                }
            }
        }
        return new Object[] {Command.RejectResponse, null}; //it should never reach this
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

    private void removeItemFromAuction(Item item) throws IOException {

        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("removing item: " + item);
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getItemID().equals(item.getItemID())) {
                itemList.remove(i);
                try {
                    bankClient.clientOutput.writeObject(new Object[] {Command.GetBalance, secretKey});
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Platform.runLater(()-> display.itemsRemainingLabel.setText(String.valueOf(itemList.size())));
            }
        }


        if (itemList.isEmpty()) {
            System.out.println("closing AH account");
            bankClient.clientOutput.writeObject(new Object[] {Command.CloseBankAccount, secretKey});
            bankClient.clientOutput.writeObject(new Object[] {Command.CloseAuctionHouseID, auctionHouseID});

            Iterator it = clients.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                clients.get(pair.getKey()).clientOutput.writeObject(new Object[] {Command.RefreshTimes});
                //bankClient.clientOutput.writeObject(new Object[] {Command.CheckAgentFunds, agentSecretKey, 1.0});
            }

            run = false;
        }

        System.out.println("The item list after removal: " + itemList);
    }

    public LinkedList<Item> getItemList() {

        System.out.println("in ah " + itemList);
        return itemList;
    }

    public void setHasFunds(boolean hasFunds) {

        this.hasFunds = hasFunds;
    }

    public synchronized void setBalance(double balance) {
        System.out.println("setting funds");
        auctionHouseBalance = balance;
        Platform.runLater(()-> display.auctionHouseBalance.setText(String.valueOf(auctionHouseBalance)));

    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public void stopAuctionHouse() throws IOException {
        System.out.println("closing AH account");
        bankClient.clientOutput.writeObject(new Object[] {Command.CloseBankAccount, secretKey});
        bankClient.clientOutput.writeObject(new Object[] {Command.CloseAuctionHouseID, auctionHouseID});

        Iterator it = clients.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            clients.get(pair.getKey()).clientOutput.writeObject(new Object[] {Command.RefreshTimes});
            //bankClient.clientOutput.writeObject(new Object[] {Command.CheckAgentFunds, agentSecretKey, 1.0});
        }

        run = false;
    }

    public void startAuctionHouseClient(String data) {
        System.out.println("Starting AH client: " + data);
        String[] clientInfoTokens = data.split("\\s");
        Agent_Client_Proxy proxyClient = new Agent_Client_Proxy(Integer.valueOf(clientInfoTokens[3]), clientInfoTokens[0], Integer.valueOf(clientInfoTokens[1]));
        clients.put(Integer.valueOf(clientInfoTokens[3]), proxyClient);
        System.out.println("AH clients " + clients);
    }

    public void debug() throws IOException {
    }
}