import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Auction_House extends Thread {

    LinkedList<Item> itemList;
    LinkedList<String> nouns;
    LinkedList<String> adjectives;
    Boolean DEBUG = true;

    int auctionHouseID;
    int portNumber;
    Auction_House_Server_Proxy auction_house_server_proxy;
    Bank_Server_Proxy bank_server_proxy;
    ConcurrentHashMap<Integer, Agent_Client_Proxy> clients;
    Bank_Client_Proxy bankClient;

    public Auction_House(int auctionHouseID, int portNumber, LinkedList nouns, LinkedList adjectives) {
        this.auctionHouseID = auctionHouseID;
        this.itemList = new LinkedList<>();
        this.nouns = nouns;
        this.adjectives = adjectives;
        createItems(10);
        this.auction_house_server_proxy = new Auction_House_Server_Proxy(this);
        this.clients = new ConcurrentHashMap();
        this.bankClient = new Bank_Client_Proxy(auctionHouseID,"AuctionHouse " + portNumber,7277); //bank
    }

    //upon creation, registers with bank by opening account with zero balance
    public void createAccount() {
//        Enum_Commands.Command createAccount = Enum_Commands.Command.CreateBankAccount;

    }

    //closes account at termination
    public void closeAccount() {
//        Enum_Commands.Command closeAccount = Enum_Commands.Command.CloseBankAccount;


    }

    //recives bid and acknowledges with a reject or accept response
    public void validateBid() {
//        Enum_Commands.Command acceptResponse = Enum_Commands.Command.AcceptResponse;
//        Enum_Commands.Command rejectResponse = Enum_Commands.Command.RejectResponse;
    }

    //When bid is accepted, bank is requested to block the funds
    public void acceptedBid() {
//        Enum_Commands.Command blockFunds = Enum_Commands.Command.BlockFunds;
    }

    //when a bid is overtaken, pass notification is sent to the agent and the funds are unblocked from the bank
    public void bidOvertaken() {
//        Enum_Commands.Command bidOvertaken = Enum_Commands.Command.BidOvertaken;
    }

    public void bidSuccessful() {
        //a bid is successful if not overtaken in 30 seconds
    }

    //when winning a bid, agent receives "winner" notification and auction house waits for
    //the blocked funds to be transferred into its account
    public void wonAuction() {
//        Enum_Commands.Command wonAuction = Enum_Commands.Command.WinMessage;
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

    public void startAuctionHouseClient(String data) {
        System.out.println("Starting AH client: " + data);
        String[] clientInfoTokens = data.split("\\s");
        Agent_Client_Proxy proxyClient = new Agent_Client_Proxy(12340, clientInfoTokens[0], Integer.valueOf(clientInfoTokens[1]));
        clients.put(12340, proxyClient);
    }

    public void debug() throws IOException {
        Object[] message = {Command.BlockFunds, "test2"};
        clients.get(12340).clientOutput.writeObject(message);
        bankClient.clientOutput.writeObject(message);
    }
}