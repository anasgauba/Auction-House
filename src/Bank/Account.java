/**
 * Anas Guaba, Clarissa Garcia, and Nathan Schaefer
 * Auction House Class
 * <p>
 *
 * @author Anas Farooq Guaba
 * @version date: 11/16/2018
 * Account Class
 * Account class is used to hold all of the information necessary for clients requesting an account.*/
package Bank;

import java.util.Random;

/**
 * The account object has a balance, a locked balanced for locked funds, a secret key for the account,
 * an account ID for the specific account, and an account name.
 */
public class Account {
    protected double balance;
    protected double lockBalance;
    private int secretKey;
    private int accountId;
    private String accountName;

    /**
     * This constructor is used to create a new account. When passed in a name and an intial balance, a new
     * account is initialized with the balance passed in, a locked balance of zero, and an account name.
     * @param name - name of the client who is creating an account
     * @param initialBalance - initial balance of the client who is creating an account
     */
    //upon creation of account return unique ID for the client
    public Account(String name, double initialBalance){
        this.balance = initialBalance;
        this.lockBalance = 0;
        this.accountName = name;
    }

    /**
     * The getAccountID method is used to fetch the current account's ID
     * @return - current account's ID
     */
    public int getAccountId() {
        return accountId;
    }

    /**
     * The setAccountId method is used to set the current account's ID
     * @param id - the id in which the account is to be set
     */
    public void setAccountId(int id) {
        accountId = id;
    }

    /**
     * The method getSecretKey is used to get the current account's ID
     * @return - the secret key of the current account
     */
    public int getSecretKey() {
        return secretKey;
    }

    /**
     * The setSecretKey method is used to set the secret key of an account when passed in to the new secret key
     * @param key - the secret key of the new account to be set
     */
    public void setSecretKey(int key) {
        secretKey = key;
    }

    /**
     * The generateSecretKey method is used to create a secret key between 1000 and 10000.
     * @return - the newly-generated secret key
     */
    public String generateSecretKey() {
        String key = "";
        Random random = new Random();
        int max = 10000;
        int min = 1000;
        key = Integer.toString(random.nextInt(max-min) + min);
        return key;
    }
}
