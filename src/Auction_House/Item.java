/**
 * Anas Guaba, Clarissa Garcia, and Nathan Schaefer
 * Item Class
 * <p>
 *
 * @author Nathan Schaefer
 * <p>
 * The item class is used to hold all of the information necessary for items to be utilized in the program.
 * this includes the item's auction house ID, auction house secret key, secret biddery key, item ID, description,
 * minimum bid amount, current bid amount, time remaining, a boolean on whether the current auction is active, and
 * a serial version UID.
 */
package Auction_House;

import java.io.Serializable;

/**
 * This class Item implements Serializable because, in between passing the objects over clients and servers,
 * the items need to be able to convert into bytes. The item holds all of the information necessary for items to be
 * utilized in the program.
 * this includes the item's auction house ID, auction house secret key, secret bidder key, item ID, description,
 * minimum bid amount, current bid amount, time remaining, a boolean on whether the current auction is active, and
 * a serial version UID
 */

public class Item implements Serializable {

    private int auctionHouseID;
    private int auctionHouseSecretKey;
    private int secretBidderKey;
    private String itemID;
    private String description;
    private double minimumBidAmount;
    private double currentBidAmount;
    private long bidTimeRemaining;
    private boolean auctionActive;
    private static final long serialVersionUID = 1L;

    /**
     * The item constructor is in charge of creating a new item with the auctionHouseID passed in, the itemID,
     * the item description, the minimum bid amount of the item, and the current bid amount.
     *
     * @param auctionHouseID   - the auction house ID in which the item belongs in
     * @param itemID           - the ID of the item
     * @param description      - the descriptive name of what the item is
     * @param minimumBidAmount - the randomized amount set to be the minimum bid of the item
     * @param currentBidAmount - the current bid amount, which is usually 0.01 greater than the minimum bid
     */
    public Item(int auctionHouseID, String itemID, String description, double minimumBidAmount,
                double currentBidAmount) {
        this.auctionHouseID = auctionHouseID;
        this.auctionHouseSecretKey = 0;
        this.itemID = itemID;
        this.description = description;
        this.minimumBidAmount = minimumBidAmount;
        this.currentBidAmount = currentBidAmount;
        this.bidTimeRemaining = 0;
        this.auctionActive = true;
    }

    /**
     * The getAuctionHouseID method is a simple method which returns the auction house ID that the current
     * item resides in.
     *
     * @return - item's auction house's ID
     */
    public int getAuctionHouseID() {
        return auctionHouseID;
    }

    /**
     * the getItemID reutrns the item ID of the current item
     *
     * @return - the current item's ID
     */
    public String getItemID() {
        return itemID;
    }

    /**
     * The getDescription method is a method used to retrieve the descriptive name of the current item
     *
     * @return - descriptive name of the current item
     */
    public String getDescription() {
        return description;
    }

    /**
     * The getMinimumAmount method is a method used to retrieve the minimum amount bid of the current item
     *
     * @return - minimum bid amount of the current item
     */
    public double getMinimumBidAmount() {
        return minimumBidAmount;
    }

    /**
     * The getCurrentBidAmount method is a method used to retrieve the current amount bid of the current item
     *
     * @return - current bid amount of the current item
     */
    public double getCurrentBidAmount() {
        return currentBidAmount;
    }

    /**
     * The getBidTimeRemaining method is a method used to retrieve the amount of time remaining for the current item
     *
     * @return - amount of time remaining for the current item
     */
    public long getBidTimeRemaining() {
        return bidTimeRemaining;
    }

    /**
     * setBidAmount is the method used to set the current bid amount of the current item
     *
     * @param amount - the amount of money that is to be set for the item's current bid
     */
    public void setBidAmount(double amount) {
        this.currentBidAmount = amount;
    }

    /**
     * setSecretBiddingKey is a key that is used to set the current bidding key of the agent in the lead
     * for winning the current item
     *
     * @param secretBidderKey - the bidding key of the agent that is leading the bid
     */
    public void setSecretBidderKey(int secretBidderKey) {
        this.secretBidderKey = secretBidderKey;
    }

    /**
     * The method getSecretBidderKey is a method used when retrieving the key of the agent leading in the item bid
     *
     * @return - bidding key of the agent leading in the item bid
     */
    public int getSecretBidderKey() {
        return secretBidderKey;
    }

    /**
     * the method startBidTime is a method used to start the timer after an agent bids on an item.
     */
    public void startBidTime() {
        this.bidTimeRemaining = System.currentTimeMillis() + 30000;
    }

    /**
     * The setAuctionActive method is used when determining whether the current auction is active or not
     *
     * @param messageSent - the boolean that determines whether the current auction is active or not
     */
    public void setAuctionActive(boolean messageSent) {
        this.auctionActive = messageSent;
    }

    /**
     * The getAuctionActive method returns a boolean on whether the current auction house that the item resides
     * in is active or not
     *
     * @return - returns a boolean on whether the current auction house that the item resides in is active or not
     */
    public boolean getAuctionActive() {
        return auctionActive;
    }

    /**
     * The setAuctionHouseSecretKey is a method that is used to set the current item's auction house's secret key.
     * @param key - the key of the auction house that the current item belongs in.
     */
    public void setAuctionHouseSecretKey(int key) {
        this.auctionHouseSecretKey = key;
    }

    /**
     * The getAuctionHouseSecretKey is a method used to retrieve the key of the auction house the item belongs in
     * @return - the key of the auction house the item belongs in
     */
    public int getAuctionHouseSecretKey() {
        return auctionHouseSecretKey;
    }

    /**
     * When an item is asked to be turned into a string, it prints out its auction house id, its item id, its
     * descriptive item name, the minimum bid amount, the current bid amount, and the time remaining.
     * @return - returns a string with the above named components.
     */
    @Override
    public String toString() {
        return getAuctionHouseID() + " " + getItemID() + " " + getDescription() + " " + getMinimumBidAmount() + " " + getCurrentBidAmount() + " " + getBidTimeRemaining();
    }
}
