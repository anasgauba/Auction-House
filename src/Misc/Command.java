package Misc;

/**
 * Anas Guaba, Clarissa Garcia, and Nathan Schaefer
 *
 * Enum Command
 *
 * This enum named Command is used to facilitate the commands between clients and proxies when messages are
 * being passed. Each of the commands listed are used to signal to the different classes what information
 * to return and what functions to call.
 */
public enum Command {
    GetListHouses, GetListItems, SetListItems, CloseBankAccount,TransferBlockedFunds,
    CreateBankAccount, BlockFunds, UnlockFunds, WinMessage, BidOvertaken, AcceptResponse,
    RejectResponse, AddAuctionHouseID, SetListHouses, SendBid, SetAgentKey, SetAuctionHouseKey,
    CheckAgentFunds, RefreshTimes, GetBalance, SetBalance, TimeOffSet, CloseAuctionHouseID,
    FundsTransferred, CloseAgentAccount

}
