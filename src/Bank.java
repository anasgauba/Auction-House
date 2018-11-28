import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @version date: 11/16/2018
 * @author Anas Farooq Gauba
 */
public class Bank {
    private HashMap<Integer, Account> list;
    private int secretKey;

    int bankID;
    Bank_Server_Proxy bank_server_proxy;
    ConcurrentHashMap<Integer, Auction_House_Client_Proxy> clients;

    public Bank() {
        this.list = new HashMap<>();
        this.bankID = 1;
        clients = new ConcurrentHashMap();
        Bank_Server_Proxy bank_server_proxy = new Bank_Server_Proxy(this);
    }

    //Opens Bank Account for Agent or AuctionHouse
    //return the biddingKey that is created when an account is created
    public int createAccount(int id, int initialBalance) throws Exception {
        if (list.containsKey(secretKey)) {
            throw new Exception("Account already exists");
        }
        else {
            Account account = new Account(initialBalance);
            account.setAccountId(id);
            secretKey = account.generateSecretKey();
            list.put(secretKey, account);
            System.out.println("Account " +account.getAccountId() +" created " +
                    "with secretKey "
                    +secretKey);
        }
        return secretKey;
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
    public boolean abilityToBuy(int secretKey, int priceOfObject){
        Account account = list.get(secretKey);
        if (account.balance >= priceOfObject) {
            return true;
        }
        return false;
    }
    //locks balance to prevent proper funding
    private void lockBalance(){

    }
    //unlocks balance to release proper funding
    private void unlockBalance(){

    }

    //reduces the account that is looked up by the amount of money passed in
    public synchronized void withdraw(int secretKey,int moneyToReduce) throws Exception {
        if (list.containsKey(secretKey)) {
            Account account = list.get(secretKey);
            if (moneyToReduce > account.balance) {
                throw new Exception("Insufficient funds");
            }
            else {
                account.balance -= moneyToReduce;
                System.out.println("withdrawn for Account " + account
                        .getAccountId() +" "+moneyToReduce);
            }
        }
    }
    //adds the account that is looked up by the amount of money passed in

    public synchronized void deposit(int secretKey,int moneyToGive) throws Exception {
        if (list.containsKey(secretKey)) {
            Account account = list.get(secretKey);
            account.balance += moneyToGive;
            System.out.println("DEPOSITED for Account " + account.getAccountId()
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
        secretKey = 0;
        account.balance = 0;
        System.out.println("Account closed of " +idOfClient);
    }
    public String toString() {
        return secretKey +" >>>>> " + getBalance(secretKey);
    }
    //for testing purposes.
    public static void main(String[] args) throws Exception {
        Bank b1 = new Bank();
        Bank b2 = new Bank();
        int key, key2;
        key = b1.createAccount(1, 10);
        key2 = b2.createAccount (2, 20);

        b1.getBalance(key);
        b2.getBalance(key2);

        b1.deposit(key, 100);
        b2.deposit(key2, 50);

        b1.getBalance(key);
        b2.getBalance(key2);

        b1.withdraw(key, 105);
        b2.withdraw(key2, 20);

        b1.getBalance(key);
        b2.getBalance(key2);

        System.out.println();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println();


        b1.closeAccount(key);
        System.out.println(b1.toString());

        key = b1.createAccount(1, 100);
        System.out.println(b1.toString());
    }

    public void startBankClient(String data) {
        System.out.println("Starting Bank client: " + data);
        String[] clientInfoTokens = data.split("\\s");
        Auction_House_Client_Proxy proxyClient = new Auction_House_Client_Proxy(12340, clientInfoTokens[0], Integer.valueOf(clientInfoTokens[1]));
        clients.put(12340, proxyClient);
    }

    public void debug() throws IOException {
            Object[] message = {Command.BlockFunds, "test2"};
            clients.get(12340).clientOutput.writeObject(message);
    }
}
