package Bank;

import java.util.Random;

/**
 * @version date: 11/16/2018
 * @author Anas Farooq Gauba
 */
public class Account {
    protected double balance;
    protected double lockBalance;
    private int secretKey;
    private int accountId;
    private String accountName;

    //upon creation of account return unique ID for the client
    public Account(String name, Double initialBalance){
        this.balance = initialBalance;
        this.lockBalance = 0;
        this.accountName = name;
    }
    public double getLock() {
        return lockBalance;
    }

    public void lock(double moneyToLock) {
        lockBalance += moneyToLock;
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
            Account acc = new Account("Robert", 10.0);
            acc.setAccountId(i);
            i++;
            System.out.println("Acc id is "+acc.getAccountId());
            System.out.println(acc.generateSecretKey());
        }
    }
}
