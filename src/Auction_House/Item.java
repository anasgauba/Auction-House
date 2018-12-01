package Auction_House;

import java.io.Serializable;

public class Item implements Serializable {

    private int auctionHouseID;
    private int secretBidderKey;
    private String itemID;
    private String description;
    private double minimumBidAmount;
    private double currentBidAmount;
    private long bidTimeRemaining;
    private boolean auctionActive;
    private static final long serialVersionUID = 1L;

    public Item(int auctionHouseID, String itemID, String description, double minimumBidAmount, double currentBidAmount) {
        this.auctionHouseID = auctionHouseID;
        this.itemID = itemID;
        this.description = description;
        this.minimumBidAmount = minimumBidAmount;
        this.currentBidAmount = currentBidAmount;
        this.bidTimeRemaining = 0;
        this.auctionActive = true;
    }

    public int getAuctionHouseID() {

        return auctionHouseID;
    }

    public String getItemID() {

        return itemID;
    }

    public String getDescription() {

        return description;
    }

    public double getMinimumBidAmount() {

        return minimumBidAmount;
    }

    public double getCurrentBidAmount() {

        return currentBidAmount;
    }

    public long getBidTimeRemaining(){

        return bidTimeRemaining;
    }

    public void setBidAmount(double amount) {

        this.currentBidAmount = amount;
    }

    public void setSecretBidderKey(int secretBidderKey) {

        this.secretBidderKey = secretBidderKey;
    }

    public int getSecretBidderKey() {

        return secretBidderKey;
    }

    public void startBidTime() {

        this.bidTimeRemaining = System.currentTimeMillis() + 30000;
    }

    public void setAuctionActive(boolean messageSent) {

        this.auctionActive = messageSent;
    }

    public boolean getAuctionActive() {

        return auctionActive;
    }

    @Override
    public String toString() {
        return getAuctionHouseID() + " " + getItemID() + " " + getDescription() + " " + getMinimumBidAmount() + " " + getCurrentBidAmount() + " " + getBidTimeRemaining();
    }
}
