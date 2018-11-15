public class Auction_House {
    //hosts a list of items being auctioned
    //hosts the bidding status for each bid (house id, item id, description, min bid, current bid)

    //upon creation, registers with bank by opening account with zero balance
    public void createAccount(){

    }

    //closes account at termination
    public void closeAccount(){

    }

    //recives bid and acknowledges with a reject or accept response
    public void validateBid(){

    }

    //When bid is accepted, bank is requested to block the funds
    public void acceptedBid(){
        //block the funds

    }
    //when a bid is overtaken, pass notification is sent to the agent and the funds are unblocked from the bank
    public void bidOvertaken(){
    }

    public void bidSuccessful(){
        //a bid is successful if not overtaken in 30 seconds
        //
    }
    //when winning a bid, agent receives "winner" notification and auction house waits for
    //the blocked funds to be transferred into its account
    public void wonAuction(){

    }




}