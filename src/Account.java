import java.util.Random;

/**
 * @version date: 11/16/2018
 * @author Anas Farooq Gauba
 */
public class Account {
    private double balance;
    private int biddingKey;
    private int accountId;

    //upon creation of account return unique ID for the client
    public Account(int initialBalance){
        balance = initialBalance;
    }

    public double getBalance() {
        return balance;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int id) {
        accountId = id;
    }

    public int getBiddingKey() {
        return biddingKey;
    }

    public int generateSecretKey() {
        String secretKey = "";
        Random random = new Random();
        int max = 9000;
        int min = 1000;
        secretKey = Integer.toString(random.nextInt(max-min) + min) + accountId;
        return biddingKey = Integer.parseInt(secretKey);
    }

    //for testing purposes.
    public static void main(String[]args) {
        int i = 0;
        while (i < 10) {
            Account acc = new Account(10);
            acc.setAccountId(i);
            i++;
            System.out.println("Acc id is "+acc.getAccountId());
            System.out.println(acc.generateSecretKey());
        }
    }
}
