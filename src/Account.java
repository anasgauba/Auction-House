import java.util.Random;

/**
 * @version date: 11/16/2018
 * @author Anas Farooq Gauba
 */
public class Account {
    protected double balance;
    private int secretKey;
    private int accountId;
    private String accountName;
    private double lock;

    //upon creation of account return unique ID for the client
    public Account(String name, int initialBalance){
        this.balance = initialBalance;
        this.accountName = name;
    }
    public double getLock() {
        return lock;
    }
    public void lock(int moneyToLock) {
        lock = moneyToLock;
    }
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int id) {
        accountId = id;
    }

    public int getSecretKey() {
        return secretKey;
    }
    public void setSecretKey(int key) {
        secretKey = key;
    }

    public String generateSecretKey() {
        String key = "";
        Random random = new Random();
        int max = 10000;
        int min = 1000;
        key = Integer.toString(random.nextInt(max-min) + min);
        return key;
    }

    //for testing purposes.
    public static void main(String[]args) {
        int i = 0;
        while (i < 10) {
            Account acc = new Account("Robert", 10);
            acc.setAccountId(i);
            i++;
            System.out.println("Acc id is "+acc.getAccountId());
            System.out.println(acc.generateSecretKey());
        }
    }
}
