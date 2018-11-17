import java.util.concurrent.ConcurrentHashMap;

/**
 * @version date: 11/16/2018
 * @author Anas Farooq Gauba
 */
public class Bank {
    private ConcurrentHashMap<Integer, Account> list;
    private int secretKey;

    public Bank() {
        this.list = new ConcurrentHashMap<>();
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
            System.out.println("Account created with secretKey " +secretKey);
        }
        return secretKey;
    }

    //returns balance based on agent's string
    public double getBalance(int agentSecretKey) {
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
    private void takeMoney(int amountOfMoneyToReduce){
    }
    //adds the account that is looked up by the amount of money passed in

    private void giveMoney(int amountOfMoneyToGive){

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
        int key;
        key = b1.createAccount(1, 10);
        b2.createAccount (2, 20);

        System.out.println(b1.toString());

        b1.closeAccount(key);
        System.out.println(b1.toString());

        key = b1.createAccount(1, 100);
        System.out.println(b1.toString());
    }
}
