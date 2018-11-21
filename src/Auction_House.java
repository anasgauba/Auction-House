import java.util.LinkedList;
import java.util.Random;

public class Auction_House extends Thread {

    String auctionHouseID;
    LinkedList<Item> itemList;
    LinkedList<String> nouns;
    LinkedList<String> adjectives;


    Boolean DEBUG = true;

    Agent_Proxy agent_proxy;
    Auction_House_Proxy auction_house_proxy;


    public Auction_House(String auctionHouseID, LinkedList nouns, LinkedList adjectives) {
        this.auctionHouseID = auctionHouseID;
        this.itemList = new LinkedList<>();
        this.nouns = nouns;
        this.adjectives = adjectives;
        //this.agent_proxy = new Agent_Proxy();
        createItems(10);
        this.auction_house_proxy = new Auction_House_Proxy();
    }

    //upon creation, registers with bank by opening account with zero balance
    public void createAccount() {
        Enum_Commands.Command createAccount = Enum_Commands.Command.CreateBankAccount;

    }

    //closes account at termination
    public void closeAccount() {
        Enum_Commands.Command closeAccount = Enum_Commands.Command.CloseBankAccount;


    }

    //recives bid and acknowledges with a reject or accept response
    public void validateBid() {
        Enum_Commands.Command acceptResponse = Enum_Commands.Command.AcceptResponse;
        Enum_Commands.Command rejectResponse = Enum_Commands.Command.RejectResponse;




    }

    //When bid is accepted, bank is requested to block the funds
    public void acceptedBid() {
        Enum_Commands.Command blockFunds = Enum_Commands.Command.BlockFunds;

        //block the funds

    }

    //when a bid is overtaken, pass notification is sent to the agent and the funds are unblocked from the bank
    public void bidOvertaken() {
        Enum_Commands.Command bidOvertaken = Enum_Commands.Command.BidOvertaken;

    }

    public void bidSuccessful() {
        //a bid is successful if not overtaken in 30 seconds
        //
    }

    //when winning a bid, agent receives "winner" notification and auction house waits for
    //the blocked funds to be transferred into its account
    public void wonAuction() {
        Enum_Commands.Command wonAuction = Enum_Commands.Command.WinMessage;



    }


    private void createItems(int amountOfItems) {
        String itemID = "0000";
        Random random = new Random();
        for (int i = 0; i < amountOfItems; i++) {
            int tempItemID = Integer.valueOf(itemID);
            tempItemID++;
            itemID = String.format("%04d", tempItemID);
            String tempItem = adjectives.get(random.nextInt(adjectives.size() - 1)) + " " + nouns.get(random.nextInt(nouns.size() - 1));
            //double minimumBidAmount = random.nextInt(200) + 5;
            //minimumBidAmount += random.nextFloat(99.99) / 100;
            double maxNum = 200;
            double minNum = 10;
            double minimumBidAmount = Math.floor(random.nextDouble() * (maxNum - minNum) * 100) / 100;
            double currentBidAmount = minimumBidAmount;

            itemList.add(new Item(this.auctionHouseID, itemID, tempItem, minimumBidAmount, currentBidAmount));
        }

        if (DEBUG) {
            for (int i = 0; i < amountOfItems; i++) {
                System.out.println(itemList.get(i));
            }
        }
    }

    public void debug() {
        auction_house_proxy.agentClientOutput.println("Test from ah client to agent server");
        auction_house_proxy.bankClientOutput.println("Test from ah client to bank server");
    }
}