package Bank;

import Auction_House.Auction_House_Client_Proxy;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @version date: 11/16/2018
 * @author Anas Farooq Gauba
 */
public class Bank {
    private HashMap<Integer, Account> list;
    protected LinkedList<Integer> auctionHouseIDList;
    protected ConcurrentHashMap auctionHousePorts;
    //    private int secretKey;
    private int accountID = 1;

    int bankID;
    Bank_Server_Proxy bank_server_proxy;
    ConcurrentHashMap<Integer, Auction_House_Client_Proxy> clients;

    public Bank() {
        this.list = new HashMap<>();
        this.auctionHouseIDList = new LinkedList<>();
        this.auctionHousePorts = new ConcurrentHashMap();
        this.bankID = 1;
        clients = new ConcurrentHashMap<>();
        Bank_Server_Proxy bank_server_proxy = new Bank_Server_Proxy(this);
    }

    //Opens Bank.Bank Bank.Account for Agent.Agent or AuctionHouse
    //return the biddingKey that is created when an account is created
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
//            account = new Bank.Account(name, initialBalance);
            secretKey = account.generateSecretKey() + accountID;
            key = Integer.parseInt(secretKey);
            account.setSecretKey(key);
            account.setAccountId(accountID);
//            secretKey = account.generateSecretKey();
            list.put(account.getSecretKey(), account);
            accountID++;
            System.out.println("Bank.Account " +account.getAccountId() +" created " +
                    "with secretKey "+account.getSecretKey());
        }
        return new Object[]{account.getAccountId(), initialBalance, account.getSecretKey()};
    }

    //returns balance based on agent's string
    public synchronized double getBalance(int agentSecretKey) {
        if (list.get(agentSecretKey) != null) {
            Account account = list.get(agentSecretKey);
            System.out.println("Current acc balance of "+ agentSecretKey +" " +
                    "is " +
                    account.balance);
            return account.balance;
        }
        return 0;
    }

    //returns whether there is sufficient funds in the account to buy the object that is being bid on
    //look up the account of the agent, check if price of object execeeds account, returns true or false
    public boolean abilityToBuy(int secretKey, double priceOfObject){
        Account account = list.get(secretKey);
        if (account.balance >= priceOfObject) {
            return true;
        }
        return false;
    }
    //locks balance to prevent proper funding
    public void lockBalance(int key, double amount){
        Account account = list.get(key);
        account.balance -= amount;
        account.lockBalance += amount;

        System.out.println("agents balance: " + account.balance + " lock amount " + amount);
        System.out.println("agents lock balance: " + account.lockBalance + " lock amount " + amount);
    }
    //unlocks balance to release proper funding
    public void unlockBalance(int key, double amount){
        Account account = list.get(key);
        account.lockBalance -= amount;
        account.balance += amount;

        System.out.println("agents balance afer unlock: " + account.balance + " lock amount " + amount);
        System.out.println("agents lock balance after unlock: " + account.lockBalance + " lock amount " + amount);

    }

    //reduces the account that is looked up by the amount of money passed in
    public synchronized void withdraw(int secretKey,double moneyToReduce) throws
            Exception {
        if (list.containsKey(secretKey)) {
            Account account = list.get(secretKey);
            if (moneyToReduce > account.balance) {
                throw new Exception("Insufficient funds");
            }
            else {
                account.balance -= moneyToReduce;
                System.out.println("withdrawn for Bank.Account " + account
                        .getAccountId() +" "+moneyToReduce);
            }
        }
    }
    //adds the account that is looked up by the amount of money passed in

    public synchronized void deposit(int secretKey,double moneyToGive) throws
            Exception {
        if (list.containsKey(secretKey)) {
            Account account = list.get(secretKey);
            account.balance += moneyToGive;
            System.out.println("DEPOSITED for Bank.Account " + account.getAccountId()
                    +" " +moneyToGive);
        }
        else {
            throw new Exception("No such account");
        }
    }

    //closes account that was given to the client
    public synchronized void closeAccount(int idOfClient){
        Account account = list.get(idOfClient);
        list.remove(idOfClient);
        account.setSecretKey(0);
//        secretKey = 0;
        account.balance = 0;
        System.out.println("Bank.Account closed of " +idOfClient);
    }

    //for testing purposes.
    public static void main(String[] args) throws Exception {
        Bank b1 = new Bank();
//        Bank.Bank b2 = new Bank.Bank();
        Object[] key, key2, key6;
        for (int i=0; i < 1000; i++) {
            key6 = b1.createAccount("Agent "+ i, i+10.0);
        }
        System.out.println();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println();

        key = b1.createAccount("xyz", 10.0);
        key2 = b1.createAccount("BOBB", 20.0);
//        key2 = b2.createAccount ("Bob", 20);

//        b1.getBalance((int)key[2]);
//        b1.getBalance((int)key2[2]);
//
//        b1.deposit((int)key[2], 100);
//        b1.deposit((int)key2[2], 50);
//
//        b1.getBalance((int)key[2]);
//        b1.getBalance((int)key2[2]);
//
//        b1.withdraw((int)key[2], 90);
//        b1.withdraw((int)key2[2], 20);
//
//        b1.getBalance((int)key[2]);
//        b1.getBalance((int)key2[2]);

        System.out.println();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println();


//        b1.closeAccount(key[2]);

//        key = b1.createAccount("xyz", 20);
//        b1.getBalance(key[2]);
//        b1.lockBalance(key[2], 10);
//        b1.getBalance(key[2]);
        //here it will unlock the balance, since the amount was locked before
        // for this account.
//        b1.unlockBalance(key[2]);
//        b1.getBalance(key[2]);

//        b1.getBalance(key2[2]);
        // here it will not unlock since for this account, there was no money
        // locked and hence, it will not change the balance for this account.
//        b1.unlockBalance(key2[2]);
//        b1.getBalance(key2[2]);

        System.out.println("list of accounts "+b1.list.size());
    }

    /*
    public void addAuctionHouseID(int auctionHouseID, int portNumber) {

        System.out.println("Adding AH to the list " + auctionHouseID);
        auctionHouseIDList.add(auctionHouseID);
        auctionHousePorts.put(auctionHouseID, )
    }
    */

    public void startBankClient(String data) {
        System.out.println("Starting Bank.Bank client: " + data);
        String[] clientInfoTokens = data.split("\\s");
        Auction_House_Client_Proxy proxyClient = new Auction_House_Client_Proxy(
                12340, clientInfoTokens[0], Integer.valueOf(clientInfoTokens[1]));
        clients.put(12340, proxyClient);
    }

    public void debug() throws IOException {
    }
}
