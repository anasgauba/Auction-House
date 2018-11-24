public class Item {

    private int auctionHouseID;
    private String itemID;
    private String description;
    private double minimumBidAmount;
    private double currentBidAmount;


    public Item(int auctionHouseID, String itemID, String description, double minimumBidAmount, double currentBidAmount) {
        this.auctionHouseID = auctionHouseID;
        this.itemID = itemID;
        this.description = description;
        this.minimumBidAmount = minimumBidAmount;
        this.currentBidAmount = currentBidAmount;
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

    @Override
    public String toString() {
        return getAuctionHouseID() + " " + getItemID() + " " + getDescription() + " " + getMinimumBidAmount() + " " + getCurrentBidAmount();
    }
}
