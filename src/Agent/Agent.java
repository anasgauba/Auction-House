/**Anas Guaba, Clarissa Garcia, and Nathan Schaefer
 * Agent Class
 *
 * @author Clarissa Garcia
 * Agent is the class in charge of manipulating the data displayed in Agent_Display class. It holds
 * the internal logic for creating its own name, setting its bidding key, balance, and list of auction house clients,
 * sending bid messages and information to auction houses, creating the items, keeping track of time shown in the GUI,
 * and closing its own account.
 */
package Agent;
import Auction_House.Auction_House_Client_Proxy;
import Auction_House.Item;
import Bank.Bank_Client_Proxy;
import Misc.Command;
import javafx.application.Platform;
import javafx.stage.Stage;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**The Agent class has been made into a Thread because it is closely tied to the GUI and needs to have the
 * GUI exist first before it manipulates data within it. It also includes a list of
 */
public class Agent extends Thread{
    private LinkedList<String> names;
    public String agentName;
    private int secretBiddingKey;
    private int currentAuctionHouse;
    private int accountID;
    private double accountBalance;

    public Agent_Display agentDisplay;
    Agent_Server_Proxy agent_server_proxy;
    Bank_Client_Proxy bankClient;
    int portNumber;
    int bankPortNumber;
    long timeOffSet;
    public Boolean houseListDone = false;
    public ConcurrentHashMap<Integer, Auction_House_Client_Proxy> clients;

    /**The Agent constructor is used to, upon creation of agent, create a name, set a port number
     * to one passed in, a bank port number to one passed in, a time offset of 0 to begin the bidding times,
     * a hashmap of clients used to connect to future auction houses, an agent server, a bank client to connect
     * to an existing bank, and a local display to change.
     * @param portNumber - the port passed in that will be used to house agent
     * @param bankPortNumber - the port passed in that has been used to house bank
     * @param nameList - a list of names that were read in to randomly choose from
     */
    public Agent (int portNumber, int bankPortNumber, LinkedList nameList){
        this.names = nameList;
        createName();
        this.portNumber = portNumber;
        this.bankPortNumber = bankPortNumber;
        this.timeOffSet = 0;
        this.clients = new ConcurrentHashMap();
        this.agent_server_proxy = new Agent_Server_Proxy(this, portNumber);
        this.bankClient = new Bank_Client_Proxy(this, 1, "Agent " + this.portNumber, this.bankPortNumber);
        start();
        agentDisplay = new Agent_Display(this);
        agentDisplay.drawGUI(new Stage());
    }

    /**createName is a method that simply chooses two names from a pre-exisitng list of names randomly chosen
     * from a file. It then sets the current agent to that name.
     */
    public void createName(){
        Random random = new Random();
        String createdName =names.get(random.nextInt(names.size() - 1)) + " " + names.get(random.nextInt(names.size() - 1));
        this.agentName=createdName;
    }

    /**
     * The setBiddingKey method sets the current bidding key returned from the bank upon account creation
     * and sets it to the current agent.
     * @param bidKeyReturned - the bidding key returned after account creating in bank
     */
    public void setBiddingKey(int bidKeyReturned){
        secretBiddingKey=bidKeyReturned;
    }

    /**
     * This method setAgentDisplayValues sets the values for the accountID and the accountBalance.
     * When called, it first checks to see if the agent display exists. When it receives notification of
     * agent display existing it sets the values in the display to reflect the numbers passed in.
     * @param accountID
     * @param accountBalance
     */
    public void setAgentDisplayValues(int accountID , double accountBalance){
        this.accountID = accountID;
        this.accountBalance = accountBalance;
        synchronized (this) {
            while (agentDisplay == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        agentDisplay.setAccountBalance(accountBalance);
        agentDisplay.setAccountID(accountID);
    }

    /**The method changeBalance is used to change the display to the correct balance for an agent that has
     * won a bid on an object and purchased it. When passed a balance, it sets the display to be that of the
     * balance passed in.
     * @param accountBalance - the new balance that has been passed in after purchase
     */
    public void changeBalance(double accountBalance) {
        this.accountBalance = accountBalance;
        agentDisplay.setAccountBalance(accountBalance);
    }

    /**The method sendBid is in charge of sending a bid along with the message array that contains the secret bidding
     * key of the agent, the item's ID and the bid amount to the auction house  of the item to make the
     * determination of win or loss.
     * @param itemID - the ID of the item that was bid on
     * @param bidAmount - the amount of money that was bid on the current object
     * @throws IOException
     */
    public void sendBid(String itemID, double bidAmount) throws IOException {
        clients.get(currentAuctionHouse).clientOutput.writeObject(new Object[] {Command.SendBid, secretBiddingKey, itemID, bidAmount});
    }

    /** This method is called to un-null the current auction house choice in display and fetch the list of items
     * later. This method simply sets the current auction house to be the one of the ID that is passed in.
     *
     * @param auctionHouseID - the ID of the auction house passed in
     */
    public void setCurrentAuctionHouse(int auctionHouseID) {
        currentAuctionHouse = auctionHouseID;
    }

    /**
     * printDetermination is a method that is called in agent display to show the notification message of an item
     * either being won or lost. As soon as an item is bid, a user will receive notification of either being accepted
     * or rejected based on funds. Upon winning an item, a message is sent to bank to get the new balance of the agent.
     * Upon rejection or having insufficient funds, the agent user is notified that the agent has not won the item.
     * @param theDetermination - the comand that has been passed into agent signaling failure or success of a bid
     * @param item - the item that has either been won or lost.
     * @throws IOException
     */
    public void printDetermination(Command theDetermination, Item item) throws IOException {
        switch (theDetermination) {
            case WinMessage:
                agentDisplay.newLine+="You won the item: " + item.getDescription() + " for amount: " + item.getCurrentBidAmount() + "\n";
                Platform.runLater(() -> agentDisplay.setNewNotificationMessage());
                bankClient.writeClientOutput(new Object[] {Command.TransferBlockedFunds,
                        secretBiddingKey, item.getAuctionHouseSecretKey(), item.getCurrentBidAmount()});
                sound("BidWon");
                clients.get(item.getAuctionHouseID()).clientOutput.writeObject(new Object[] {Command.FundsTransferred});
                break;
            case BidOvertaken:
                agentDisplay.newLine+="Your bid has been passed on item: " + item.getDescription() + " In Auction House: " + item.getAuctionHouseID() + "\n";
                Platform.runLater(() -> agentDisplay.setNewNotificationMessage());
                bankClient.writeClientOutput(new Object[] {Command
                        .GetBalance, secretBiddingKey});
                sound("BidPassed");
                break;
            case RejectResponse:
                agentDisplay.newLine+="You do not have sufficient funds or you're the current bidder\n";
                Platform.runLater(() -> agentDisplay.setNewNotificationMessage());
                sound("BidReject");
                break;
            case AcceptResponse:
                agentDisplay.newLine+="You are now the current bidder on item: " + item.getDescription() + " for amount: " + item.getCurrentBidAmount() + "\n";
                Platform.runLater(() -> agentDisplay.setNewNotificationMessage());
                bankClient.writeClientOutput(new Object[] {Command
                        .GetBalance, secretBiddingKey});
                break;
        }
    }


    LinkedList<Agent_Display.TableItem> timeList = new LinkedList();
    LinkedList<Item> itemList = new LinkedList<>();

    /**createItemList is a method called to set the items in the display correctly. It first clears
     * the time list and the item list before looping over how many items are on the table, making sure
     * to create a new table item to add to the item list before setting the table view to the items that were added.
     * @param itemList
     */
    public void createItemList(LinkedList<Item> itemList) {
        if (timeList != null) {
            timeList.clear();
        }
        if (!this.itemList.isEmpty()) {
            this.itemList.clear();
        }
        for (int i = 0; i < itemList.size(); i++) {
            Item tempItem = itemList.get(i);
            Agent_Display.TableItem temp = new Agent_Display.TableItem(tempItem);
            agentDisplay.listofTableItems.add(temp);
            timeList.add(temp);
            this.itemList.add(itemList.get(i));
        }
        agentDisplay.table.setItems(agentDisplay.listofTableItems);
    }

    /**The method refreshTimes is used to update the list of items on the Agent's GUI. It clears the list of items
     * that the tableView depends on before requesting the list of items again from bank. Auction house client
     * will later set the items in agent so that they can be displayed in agentDisplay.
     */

    public void refreshItems() throws IOException {
        agentDisplay.listofTableItems.clear();
        clients.get(currentAuctionHouse).clientOutput.writeObject(new Object[] {Command.GetListItems});
    }

    /**The method refreshTimes is a method constantly called in the animation timer in AgentGUI to
     * correctly display the countdown of each object. It grabs the time remaining on the object
     * being bid on in auction house and compares the current time in agent to display the correct
     * time in agent GUI.
     * */
    public synchronized void refreshTimes() {
        for (int i = 0; i < timeList.size(); i++) {
            long secondsRemaining = TimeUnit.MILLISECONDS.toSeconds(itemList.get(i).getBidTimeRemaining() - (System.currentTimeMillis() - getTimeOffSet()));
            if (Long.valueOf(timeList.get(i).getItemTime()) > 0) {
                timeList.get(i).setItemTime(Long.toString(secondsRemaining));
            }
        }
    }

    /**The method createHouseList takes in a list of auction houses and the a hashmap of the auction houses and their ports.
     * It runs through the list of auction houses and creates client proxies for them. It then runs through the hashmap,
     * initializing each new client requested. It also uses the notify all method in display to notify all threads
     * that the display currently exists.
     * @param auctionHouseList - a list that carries a list of auction house IDs
     * @param auctionHousePorts- a map that carries the list of auction houses to their ports
     */
    public void createHouseList(LinkedList<Integer> auctionHouseList, ConcurrentHashMap<Integer, Integer> auctionHousePorts) {
        for (int i = 0; i < auctionHouseList.size(); i++) {
            String tempID =  Integer.toString(auctionHouseList.get(i));
            agentDisplay.options.add(tempID);
            if (clients.get(auctionHouseList.get(i)) == null) {
                Auction_House_Client_Proxy clientProxy = new Auction_House_Client_Proxy(this, secretBiddingKey,
                        "Agent " + portNumber, auctionHousePorts.get(auctionHouseList.get(i)));
                clients.put(auctionHouseList.get(i), clientProxy);
                synchronized (this) {
                    while (clients.get(auctionHouseList.get(i)).clientOutput == null) {
                        try {
                            wait();
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

    /** The getHouseList method, when called from agent GUI to display the current auction
     * houses and components, sends an object array message to bank's client to later be transferred to
     * bank server and request the list of auction houses. Bank client later receives this message and
     * and processes the list by setting the auction house list in agent.
     */

    public void getHouseList() throws IOException {
        bankClient.writeClientOutput(new Object[] {Command.GetListHouses});
    }

    /**setTimeOffset is a method that, when called, sets the agent's time to be in sync with the current auction
     * house item when compared to the auction house that the item belongs in*/
    public void setTimeOffSet(Long timeOffSet) {
        this.timeOffSet = System.currentTimeMillis() - timeOffSet;
    }

    /**This message is used in order to refresh the times of the GUI. It is called when the refreshTimes method is called
     *in order to calculate the time remaining on the countdown clock of a bid when comparing the current agent to an
     *auction house item. */
    private Long getTimeOffSet() {
        return timeOffSet;
    }

    /**This command, when called by the Agent's GUI, is called when the request to close a bank account is requested.
    * It adds a message array along with the secret bidding key of the agent so that it can close the corresponding
    * agent's account. The message array is then sent to bank clients output so that it can send the message to bank
    * server, and finally, bank. The boolean that the method returns is to check if there are any currently-running
     * bids. If there are, the method returns the boolean so that the account does not close.*/
    public boolean closeAccount() throws IOException {
        boolean closeAccount = true;
        for (int i = 0; i<timeList.size(); i++){
            if (Long.parseLong(timeList.get(i).getItemTime()) > 0 && itemList.get(i).getSecretBidderKey() == secretBiddingKey){
                closeAccount = false;
            }
        }
        if (!closeAccount){
            return closeAccount;
        }
        else if (closeAccount){
            bankClient.writeClientOutput(new Object[] {Command.CloseBankAccount,
                    secretBiddingKey});
            Iterator it = clients.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                clients.get(pair.getKey()).clientOutput.writeObject(new Object[] {Command.CloseAgentAccount, secretBiddingKey});
            }
        }
        return true;
    }

    /***/

    /**The sound function, based on a string that is passed in, is called in order to notify the user
     * of a bid that has either been won, passed, or rejected, depending on the command passed in by agent's server.
     * The appropriate sound is played depending on the notification given back to agent.
     * @param type - a string passed in that details the notification sound requested
     */
    private void sound(String type) {
        File soundFile = null;
        if (type.equals("BidWon")) {
            try {
                soundFile = new File("resources/Sounds/BidWon.wav");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        else if (type.equals("BidPassed")) {
            try {
                soundFile = new File("resources/Sounds/BidPassed.wav");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        else if (type.equals("BidReject")) {
            try {
                soundFile = new File("resources/Sounds/BidReject.wav");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
