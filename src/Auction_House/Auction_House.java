/**
 * Anas Guaba, Clarissa Garcia, and Nathan Schaefer
 * Auction House Class
 * <p>
 * Auction house thread is in charge of hosting a list of items. It constantly monitors items which have been won
 * (timer reaches 0) and to send notifications to the correct clients. It also houses the current local display,
 * the port number that the auction house will be hosted at, the port number that the bank will be hosted at,
 * and a list of adjectives and nouns to create randomly-generated items.
 */
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

/**Auction House extends Thread because it is needed to constantly monitor items which have been won
 * (timer reaches 0) and to send notifications to the correct clients. It also houses the current local display,
 * the port number that the auction house will be hosted at, the port number that the bank will be hosted at,
 *  and a list of adjectives and nouns to create randomly-generated items.
 *
 */
public class Auction_House extends Thread {
    Auction_House_Display display;
    LinkedList<Item> itemList;
    LinkedList<String> nouns;
    LinkedList<String> adjectives;

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

    /**
     * The Auction house, upon creation, is passed in multiple paramaters in order to set the current local display,
     * the port number that the auction house will be hosted at, the port number that the bank will be hosted at,
     * and a list of adjectives and nouns to create randomly-generated items.
     * @param display - a local auction house display which auction house will manipulate
     * @param portNumber - the port number in which the current auction house will be hosted at
     * @param bankPortNumber - the port number in which the bank will be hosted at.
     * @param nouns - a list of nouns to create a list of item nouns to combine with adjectives
     * @param adjectives - a list of adjectives to create a list of item adjectives to combine with nouns
     * @throws IOException
     */
    public Auction_House(Auction_House_Display display, int portNumber, int bankPortNumber, LinkedList nouns, LinkedList adjectives) throws IOException {
        this.display = display;
        this.portNumber = portNumber;
        this.bankPortNumber = bankPortNumber;
        this.auctionHouseBalance = 0.0;
        this.auctionHouseID = new Random().nextInt(1000000000);
        this.itemList = new LinkedList<>();
        this.nouns = nouns;
        this.adjectives = adjectives;
        createItems(new Random().nextInt(25 - 5) + 5);
        this.auction_house_server_proxy = new Auction_House_Server_Proxy(this, portNumber);
        this.clients = new ConcurrentHashMap();
        this.bankClient = new Bank_Client_Proxy(this, auctionHouseID, "AuctionHouse " + portNumber, bankPortNumber); //bank
        this.run = true;
        this.hasFunds = false;
        start();
    }

    /**
     * The run method in auction house is responsible for settin the auction house ID, the list of remaining items, and
     * the auction house balance in the GUI. It then, while running, constantly checks the item list for items that have
     * been won and handles them accordingly.
     */
    public void run() {
        Platform.runLater(() -> display.auctionHouseID.setText(String.valueOf(auctionHouseID)));
        Platform.runLater(() -> display.itemsRemainingLabel.setText(String.valueOf(itemList.size())));
        Platform.runLater(() -> display.auctionHouseBalance.setText("0.00"));
        while (run) {
            bidSuccessfulCheck();
        }
    }

    /** The setKey method is used upon creation of a bank account. When a bank account is created, the bank
     * will return back a secret key which this method will use to set the current auction house to the
     * secret key passed in.
     *
     * @param secretKey - the secret key of the auction house that is returned upon creation of a bank account
     */
    public void setKey(int secretKey) {
        this.secretKey = secretKey;
        for (int i = 0; i < itemList.size(); i++) {
            itemList.get(i).setAuctionHouseSecretKey(secretKey);
        }
    }

    /**
     * The bidSuccessfulCheck method loops through the item list to check if an object has been successfully bid on.
     * For each item which timer is at zero, it notifies the agent that it has won the item.
     */
    public void bidSuccessfulCheck() {
        for (int i = 0; i < itemList.size(); i++) {
            long secondsRemaining = TimeUnit.MILLISECONDS.toSeconds(itemList.get(i).getBidTimeRemaining() - System.currentTimeMillis());
            if (secondsRemaining == 0 && itemList.get(i).getAuctionActive()) {
                try {
                    Item tempItem = itemList.get(i);
                    tempItem.setAuctionActive(false);
                    if (clients.get(tempItem.getSecretBidderKey()) != null) {
                        clients.get(tempItem.getSecretBidderKey()).clientOutput.writeObject(new Object[]{Command.WinMessage, tempItem}); //send msg to agent that won
                    }
                    removeItemFromAuction(tempItem);
                    Iterator it = clients.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        clients.get(pair.getKey()).clientOutput.writeObject(new Object[]{Command.RefreshTimes});
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**The method sendBidk uses the agent's secret key to make a bid on the item passed in with the chosen bid amount.
     * It first finds the item and sets the timer if it has not been initialized. Otherwise, it updates the current
     * ticking time. It checks that the bidder hsas .01 over the current bid and locks in the bidder's secret key
     * if that's the case. It then checks funds, locks funds if a legit bid has been made, updates the time if it
     * has not started yet, and rejects the response if the bid does not fall in any of the above categories.
     * @param agentSecretKey - the key of the agent that has made the bid on the item
     * @param itemID - the ID of the item that is being bid on
     * @param bidAmount - the bidding amount requested by the bidder
     * @return - a message array with the leading command being the reject response.
     * @throws IOException
     */
    public synchronized Object[] sendBid(int agentSecretKey, String itemID, double bidAmount) throws IOException {
        for (int i = 0; i < itemList.size(); i++) {
            if (itemID.equals(itemList.get(i).getItemID())) {
                Item item = itemList.get(i);
                //Checking the Funds
                bankClient.clientOutput.writeObject(new Object[]{Command.CheckAgentFunds, agentSecretKey, bidAmount});
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (hasFunds && item.getSecretBidderKey() != agentSecretKey) {
                    //Lock Balance if legitamate bid has been made
                    bankClient.clientOutput.writeObject(new Object[]{Command.BlockFunds, agentSecretKey, bidAmount});
                    synchronized (this) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //If the item has not started yet
                    if (item.getBidTimeRemaining() == 0 && bidAmount >= item.getCurrentBidAmount()) {
                        item.startBidTime();
                        item.setSecretBidderKey(agentSecretKey);
                        item.setBidAmount(bidAmount);
                        Iterator it = clients.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry) it.next();
                            clients.get(pair.getKey()).clientOutput.writeObject(new Object[]{Command.RefreshTimes});
                        }
                        return new Object[]{Command.AcceptResponse, item};
                    }
                    //If the item time is still going
                    else if (item.getBidTimeRemaining() > 0 && bidAmount > item.getCurrentBidAmount()) {
                        bankClient.clientOutput.writeObject(new Object[]{Command.UnlockFunds, item.getSecretBidderKey(), item.getCurrentBidAmount()});
                        synchronized (this) {
                            try {
                                wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        item.startBidTime();
                        Iterator it = clients.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry) it.next();
                            clients.get(pair.getKey()).clientOutput.writeObject(new Object[]{Command.RefreshTimes});
                        }
                        if (clients.get(item.getSecretBidderKey()) != null) {
                            clients.get(item.getSecretBidderKey()).clientOutput.writeObject(new Object[]{Command.BidOvertaken, item});
                        }
                        item.setSecretBidderKey(agentSecretKey);
                        item.setBidAmount(bidAmount);
                        return new Object[]{Command.AcceptResponse, item};
                    }
                }
                //In the case that the bid is rejected
                if (!hasFunds) {
                    return new Object[]{Command.RejectResponse, null};
                }
            }
        }
        return new Object[]{Command.RejectResponse, null};
    }

    /**
     * This method createItems, when called, will create a random list of items with their own minimum bid amount,
     * current bid amount to be displayed and random name. Each item is then added to the itemList which will later
     * have the table view set to the newly-created item list.
     * @param amountOfItems - Amount of random items to be created
     */
    private void createItems(int amountOfItems) {
        String itemID = "0000";
        Random random = new Random();
        for (int i = 0; i < amountOfItems; i++) {
            int tempItemID = Integer.valueOf(itemID);
            tempItemID++;
            itemID = String.format("%04d", tempItemID);
            String tempItem = adjectives.get(random.nextInt(adjectives.size() - 1)) + " " + nouns.get(random.nextInt(nouns.size() - 1));
            double maxNum = 200;
            double minNum = 10;
            double minimumBidAmount = Math.floor(random.nextDouble() * (maxNum - minNum) * 100) / 100;
            double currentBidAmount = minimumBidAmount;
            itemList.add(new Item(this.auctionHouseID, itemID, tempItem, minimumBidAmount, currentBidAmount));
        }
    }

    /**
     * This method removeItemFromAuction is to remove an item that has already expired. When passed in, the
     * method. It notifies the agent that has bid the highest amount that it has won the item and removes the
     * item from the list. If the list of items is now empty, then the auction house account is closed.
     * @param item - the item that will be removed
     * @throws IOException
     */
    private void removeItemFromAuction(Item item) throws IOException {
        if (clients.get(item.getSecretBidderKey()) != null) {
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            bankClient.clientOutput.writeObject(new Object[]{Command.TransferBlockedFunds, item.getSecretBidderKey(), secretKey, item.getCurrentBidAmount()});
        }
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getItemID().equals(item.getItemID())) {
                itemList.remove(i);
                try {
                    bankClient.clientOutput.writeObject(new Object[]{Command.GetBalance, secretKey});
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> display.itemsRemainingLabel.setText(String.valueOf(itemList.size())));
            }
        }
        if (itemList.isEmpty()) {
            bankClient.clientOutput.writeObject(new Object[]{Command.CloseBankAccount, secretKey});
            bankClient.clientOutput.writeObject(new Object[]{Command.CloseAuctionHouseID, auctionHouseID});
            Iterator it = clients.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                clients.get(pair.getKey()).clientOutput.writeObject(new Object[]{Command.RefreshTimes});
            }
            run = false;
        }
    }

    /**
     * This simple method is used to return the list of items that an auction house contains.
     * It returns a linked list of items.
     * @return - the linked list of items of the auction house requested
     */
    public LinkedList<Item> getItemList() {
        return itemList;
    }

    /** The setHasFunds method is used to process the ability of an agent to purchase an item or not. If
     * it is able to or not, the boolean is passed into this method so the auction house can know whether
     * to process the request or not for the agent.
     * @param hasFunds - a boolean that determines whether the agent can purchase an item or not
     */
    public void setHasFunds(boolean hasFunds) {
        this.hasFunds = hasFunds;
    }

    /**
     * The method setBalance is used to set the display balance in the auction house GUI to the correct amount.
     * Based on the amount passed in, the auction house display syncs its balance to the one passed in.
     * @param balance - the amount of money requested for the auction house balance to be set in the display
     */
    public synchronized void setBalance(double balance) {
        auctionHouseBalance = balance;
        Platform.runLater(() -> display.auctionHouseBalance.setText(String.valueOf(auctionHouseBalance)));
    }

    /**  The setPortNumber method is used to set the current auction house's port number. Based on the port that is
     * passed in from auction house GUI, the auction house's port is set.
     *
     * @param portNumber - the port number requested for the auction house to be set at.
     */
    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    /**The stopAuctionHouse method creates the two bank message arrays
     * with the necessary information to  close the auction house's bank account and remove its ID
     * from the bank's records as a list of active auction houses. It also refreshes the times in agent
     * to update the display correctly and remove the auction house from the list.
     *
     * @throws IOException
     */
    public void stopAuctionHouse() throws IOException {
        bankClient.clientOutput.writeObject(new Object[]{Command.CloseBankAccount, secretKey});
        bankClient.clientOutput.writeObject(new Object[]{Command.CloseAuctionHouseID, auctionHouseID});
        Iterator it = clients.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            clients.get(pair.getKey()).clientOutput.writeObject(new Object[]{Command.RefreshTimes});
        }
        run = false;
    }

    /**
     * Upon closing of an agent account, this removeAgent method will remove the agent using its ID from the
     * current auction house's list of clients.
     * @param agentID - the agent's ID belonging to the agent that will be removed.
     */
    public void removeAgent(Integer agentID) {
        clients.remove(agentID);
    }

    /** This method parses the string passed in into the correct paramaters to create a new auction house client.
     * It then adds the client to the list of clients for the current auction house for future reference.
     *
     * @param data - list of data in string format to be parsed and made into a new agent client
     */
    public void startAuctionHouseClient(String data) {
        String[] clientInfoTokens = data.split("\\s");
        Agent_Client_Proxy proxyClient = new Agent_Client_Proxy(Integer.valueOf(clientInfoTokens[3]), clientInfoTokens[0], Integer.valueOf(clientInfoTokens[1]));
        clients.put(Integer.valueOf(clientInfoTokens[3]), proxyClient);
    }
}