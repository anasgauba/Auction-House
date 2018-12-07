
/**
 * Anas Guaba, Clarissa Garcia, and Nathan Schaefer
 *  Bank  Class
 *
 *  @version date: 11/16/2018
 *  @author Anas Farooq Gauba
 *
 * The Bank class is in charge of creating an account, fetching the balance of an account, locking funds
 * if an client bids on an object, checkng whether a client has sufficient funds to bid, unlocking funds
 * if a bid is surpassed, deposit money between agent and auction house, and close an account. The Bank class
 * incudes a local display it can manipulate,a hashmap of new accounts,
 * a list of auction house IDs, a map of auction house ports, a hashmap of clients, and a new bank server
 *  proxy.
 */

package Bank;

import Auction_House.Auction_House_Client_Proxy;
import javafx.application.Platform;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

/*The Bank class incudes a local display it can manipulate,a hashmap of new accounts,
 * a list of auction house IDs, a map of auction house ports, a hashmap of clients, and a new bank server
 *  proxy.*
 */
public class Bank {
    private Bank_Display display;
    private HashMap<Integer, Account> list;
    protected LinkedList<Integer> auctionHouseIDList;
    protected ConcurrentHashMap auctionHousePorts;
    private int accountID = 1;
    private ConcurrentHashMap<Integer, Auction_House_Client_Proxy> clients;

    /**
     * The Bank constructor is used to initialize the local display, a hashmap of new accounts,
     * a list of auction house IDs, a map of auction house ports, a hashmap of clients, and a new bank server
     * proxy.
     * @param display
     * @param portNumber
     */
    public Bank(Bank_Display display, int portNumber) {
        this.display = display;
        this.list = new HashMap<>();
        this.auctionHouseIDList = new LinkedList<>();
        this.auctionHousePorts = new ConcurrentHashMap();
        clients = new ConcurrentHashMap<>();
        Bank_Server_Proxy bank_server_proxy = new Bank_Server_Proxy(this, portNumber);
    }

    /**
     * This method sets up an account based on a name of a client and an initial balance. It checks whether a bank
     * account already exists and if it does not, a bank account is created with a generated secret key. The
     * account is also added to the list of accounts that bank keeps track of.
     * @param name - name of the client requesting a new account
     * @param initialBalance - initial balance that the new account will have
     * @return - message array containing the account ID, initial balance, and secret key of the client
     * @throws Exception
     */
    public Object[] createAccount(String name, double initialBalance) throws
            Exception {
        Account account = new Account(name, initialBalance);
        String secretKey;
        int key;
        if (list.containsKey(account.getSecretKey())) {
            System.out.println(account.getSecretKey());
            throw new Exception("Bank.Account already exists");
        }
        else {
            secretKey = account.generateSecretKey() + accountID;
            key = Integer.parseInt(secretKey);
            account.setSecretKey(key);
            account.setAccountId(accountID);
            list.put(account.getSecretKey(), account);
            Platform.runLater(()-> display.numAccounts.setText(String.valueOf
                    (list.size())));
            accountID++;
        }
        return new Object[]{account.getAccountId(), initialBalance, account.getSecretKey()};
    }

    /**
     * this method, getBalance, looks up the account balance of an agent that is passed in.
     * Using the agent's secret key, it looks up the balance of the account and returns the
     * agent's balance.
     * @param agentSecretKey - the secret bidding key of the agent passed in
     * @return - the account balance the agent currently holds
     */
    public synchronized double getBalance(int agentSecretKey) {
        if (list.get(agentSecretKey) != null) {
            Account account = list.get(agentSecretKey);
            return account.balance;
        }
        return 0;
    }

    /**
     * This simple method determines whether the agent is able to bid on the item with the funds that they
     * currently have. The agent is looked up with its key and depending on whether there are enough funds,
     * it returns a boolean.
     * @param secretKey - the key of the agent that is bidding on an object
     * @param priceOfObject - the price of the object that is being bid on
     * @return - the true or false determination of whether an item can buy an object with its current funds
     */
    public boolean abilityToBuy(int secretKey, double priceOfObject){
        Account account = list.get(secretKey);
        if (account.balance >= priceOfObject) {
            return true;
        }
        return false;
    }

    /**
     * The lockBalance function is used after an agent has bid on an item and has the funds for the item.
     * The bank subtracts the amount the item is worth from the agent's account and to their locked balance
     * to later be released or given upon determination of a bid being accepted or rejected.
     * @param key - the agent's key in which funds will be locked
     * @param amount - the amount the item is worth that the bank account will lock.
     */
    public void lockBalance(int key, double amount){
        Account account = list.get(key);
        account.balance -= amount;
        account.lockBalance += amount;

        System.out.println("Account " +account.getAccountId()+" lock balance " +
                "is " + account.lockBalance);
        System.out.println();
    }

    /**unlockBalance method, when called, releases the locked funds from the agent's account to its actual
     * account balance when the agent has been determined to have been overbid by another agent.
     *
     * @param key - the key of the agent that has been outbid
     * @param amount - the item's value that the agent passed in has been overbid on
     */
    public void unlockBalance(int key, double amount){
        Account account = list.get(key);
        if (account != null) {
            account.lockBalance -= amount;
            account.balance += amount;

            System.out.println("Account " +account.getAccountId()+" current " +
                    "balance after unlock is " + account.balance);
            System.out.println();
        }
    }


    /**
     * The deposit function is used to give money from an agent that has won a bid to an
     * auction house that was selling the item. It retrieves both accounts locally based
     * on their IDs before transferring the locked funds from agent's account to
     * auction house's account.
     * @param agentSecretKey - the secret key of the agent that has won the bid
     * @param auctionHouseSecretKey - the secret key of the auction house selling the item
     * @param moneyToGive - the money to transfer between the two accounts
     * @throws Exception
     */
    public synchronized void deposit(int agentSecretKey, int auctionHouseSecretKey, double moneyToGive) throws Exception {
        Account agentAcount = list.get(agentSecretKey);
        Account auctionHouseAccount = list.get(auctionHouseSecretKey);
        if (agentAcount != null && auctionHouseAccount != null) {

            System.out.println("-------------------------------------------");
            System.out.println();
            System.out.println("Agent "+agentAcount.getAccountId()+" lock " +
                    "balance before giving to auction house "+
                    auctionHouseAccount.getAccountId()+" is "+agentAcount.lockBalance);
            System.out.println("Auction house "+ auctionHouseAccount
                    .getAccountId()+" balance before getting money from agent is "
                    + auctionHouseAccount.balance);
            System.out.println();
            System.out.println("-------------------------------------------");

            agentAcount.lockBalance -= moneyToGive;
            auctionHouseAccount.balance += moneyToGive;

            System.out.println();
            System.out.println("Amount "+moneyToGive+" from agent account " +
                    ""+agentAcount.getAccountId() + " is transferred to auction house " +
                    "account " +auctionHouseAccount.getAccountId() +", now " +
                    "auction house has balance of "+auctionHouseAccount.balance);
            System.out.println();
        }
        //this is for if agent forcefully closes out of client, then bank
        // will still give auction house funds. (project assumption)
        //SHOULD never reach this statement. 
        else if (agentAcount == null && auctionHouseAccount != null) {
            auctionHouseAccount.balance += moneyToGive;
        }
    }

    /** This method closeAccount is used whenever an Auction House or Agent requests to close
     * their bank account. The ID of the client to be removed is taken from the hashmap that
     * links the ids of the clients to their account.
     *
     * @param idOfClient - the ID of the client who requested to close their bank account
     */
    public synchronized void closeAccount(int idOfClient){
        list.remove(idOfClient);
        Platform.runLater(()-> display.numAccounts.setText(String.valueOf
                (list.size())));
    }
}
