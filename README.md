# Project: Auction House

## Students: Nathan Schaefer, Anas Gauba, Clarissa Garcia

## Introduction
We are solving the concurrent auction house with sockets between clients and servers. The
problem that was assigned to us as our final project in CS 351, The Design of Large Programs.
The goal of this project was to have three different components: Agent, Auction House, & Bank.
Each component was assigned to one of the three members of the team. An incremental design approach was
required when working on the project. We were all responsible for interface specification, integration
and testing. The group also had to come up with a method to get all three
components working with each other using the socketing as mentioned earlier.

## Contributions
#####Nathan Schaefer:
- Responsible for Auction House Component
- Took the role of designing the proxy communication between each component, which 
includes the creation of the client and server communication that each component uses.
- Contributed to all display GUI's in Design & creation
- Helped with Agent component in the communication between components that required a collaboriative effort.
  * Time difference method
  * How agent fetches updated times with out refreshing the whole table GUI view constantly
  (Which involves constantly calling the auction house for a list of items and refreshing the view)
  * The dynamic creation of agent, which allows for Auction houses or agent's to start in any order
  and automatically adapt to it.
  * How Agent keeps track of all auction house client connections
- Helped with Bank component in the collaborative effort of auction house communicating to bank with a list
of auction houses info on creation, which agent then uses when grabbing a list of the available
auction houses
- Created Design Diagrams
- Created Design Document (method info + design diagrams)
  
#####Anas Gauba:
- Responsible for Bank Component.
- Designed and implemented the Account, Bank class. 
   * Added the functionality of creating an account with a name and initial 
   balance.
   * Upon account creation, generated a unique bidding key, and kept track of
    all the bank accounts. 
   * Added the functionality of bank holding/locking specific account's 
   funds, a way to deposit agent's money to auction house's account. 
- Helped with Agent component in the collaborative effort of communicating with bank.
   * Used the proxy communication system to send message to bank.  
   * making sure to get the current balance of this agent. 
   * a way to create an account through bank server proxy. 
- Contributed to all display GUI's.  

#####Clarissa Garcia:
- Responsible for Agent Component
- Created the enum Command, which simplifies the switch statements within each client and proxy to only the available 
and pre-defined commands outlined.
- Layed the groundwork for the Object array message, with the first object being the command and the subsequent objects
being the paramaters for method calls or the resulting returned objects from method calls.
- Created the messaging system in Agent Display, which uses a string builder to add item details, the subsequent accept/reject 
message from auctio house or bank, and display the results in the Agent's GUI
- Created the agent display initially, which includes the table view part to it.
- Commented all code
 
## Usage
As this is a very large project I will break the usage down to two sections. One describing
how to get the program gets started, and how to actually use the program.

Also, it's noted that where ever the clients are run, it must have the same folder 'resources' in the same
directory. For example, if you have folder: "AH", inside should be the jar's as well as the resources folder.

- Starting the communications between components
  * The first step required is setting up the configurations to correct IP Addresses.
  * Go into the ~/resources/ folder and open the configuration file.
    + If you're doing multiple machine over IP testing you have to do this for all configuration files
  * For Bank machine, you don't have have to change the configurations as you're not creating a client
  to another server
  * For Auction House, you must change the Agent & Bank's ip from local host to the respective machines IP
  for that component.
  * For Agent, you must change the Auction Houses' & Bank's IP to the respective machine's IP for that component.
  * If you're running off of local host on the same machine, verify all config files IP's are 'localhost'
  
  * Next comes launching the java applications
    + Launch the Bank java application first. This is required for the program to work at all.
    + Establish the port that you will be running the bank on. Default is 7277
    + Click launch server when you're ready.
    + You will see another GUI of bank info that is listed, currently it only shows the amount of bank
    accounts that have been created with bank.
    + On this second gui, there will be a Close Bank button. Only press this if you're done with the
    simulation, as this will stop component from properly working in the simulation
  
  * You can now either launch the Auction Houses or Agent  java applications in any ofder now. For purposes 
  of the usage section, I will instruct on launching Auction Houses First.
    + When launching the Auction House, make  sure the Bank IP matches the port you selected on the step above.
    Default is 7277.
    + Then select the Auction Houses Server that it will host off of, the default is 6666.
    + Finally, click launch server to start up the auction house.
    + After doing this, you should see the other window that popped when you launch the jar change the
    auction houses's gui info. The ID will update, the balance, and the amount of items the Auction House has.
    + There will be a button to close out the Auction House if you wish to before it runs out of items. This will force
    Agent's to refresh their view, and update the current auction houses.
    
  * Lastly, comes launching the Agent java application.
    + Like previously, you will launch the Agent Java application. When doing this, you will see a popup window
    appear. You must specify the Banks port that you started above, default is 7277.
    + You will then select the port number that agent will host off of, default is 8900.
    + If everything launched properly you will see another display popup on the agent's main client.
    + You will see all sorts of functionality that will be described in the next section of usage.
    + If you started Auction Houses before the agent client, you will automatically grab the first Auction House on
    the list, which will automatically pull the items from that auction house.
    
  * Please note that again, Agent or Auction house can start in any order, so choose what you would like to do.
  If you do start Agent clients before any auction houses, the initial view you will see will be blank, until an auction house
  is started and selected from the top drop down combo box by the label "Auction House".

  * Last note, if you wish to run multiple Agent's or multiple Auction Houses, on start of the GUI's, you must change the second server port that's
  respective to the component. Ex: you want to run two agents, you must still enter the Bank's port to whatever it's being hosted off, and then
  for agent server it must be unique, so if first agent is 8900, agent two must be a different number like 8901. The same goes for auction house servers.

- Using the Agents Main GUI
  * I will begin with describing the left side of the Agent's client.
  * You will see the sub sections on the left of this client
    + The first sub section has 3 different labels: Agent Name, Agent Account, Agent Balance, & Close Account
    + Agent Name displays the randomly-generated First and Last name you were given
    + Agent Account display your account number (You don't see your secret bidding key).
    + Agent Balance displays how much money you're holding.
    + Lastly, the Close Account button will close the Agent's account from the bank, and close the GUI.
  
  * The next section you will see the following labels: Item Name, Item ID, Starting Bid, Current Bid, a Text field that shows
  0.00 and lastly, a button with the text place bid.
    + Item Name will display the item's description of the item you're viewing off the table. This is blank by default.
    + Item ID will list the item's ID that you're viewing off the table. This is blank by default.
    + Starting Bid is the minimum bid the user must set on the item to start the bidding for the item. This is blank by default.
    + Current Bid will display the current bid set on the item you're viewing. Default this is blank.
    + Lastly, the Place bid button and the textfield to the left of it go together. When you have an item
    selected, it will display the current bid of the item. This is blank by default, but when clicked on an item, it always sets
    the next bid to 1.00 over the current amount.
    
  * The last section is a notification box.
    + This notification box will inform you of an event relevant to you while using the client.
    + This box works in a Ascending list type. The newest notifications are displayed at the top of
    the notification box.
    + You have a possibility of receiving four different messages
        + Win Message 
            + Will inform you of the item you won the bid on, including info about the item.
        + Bid Over Taken
            + If another agent placed a higher bid on one of the items you were bidding for, you will
            receive a notification informing you of the relevant item and auction house info that you were passed on
        + Reject Response
            + If you place an invalid bid, you receive this response. You can receive this due to a couple of reasons.
            You either don't have the funds for the item, didn't place a high enough bid, you're already the current bidder of
            the item, or lastl,y you have an item selected that the bidding already ended for.
        + Accept Response
            + You receive this message informing you of the item you bidded on with the relevant item info if your
            bid was accepted.
   
  * The other half of the client.
    * Above the table, you will see a label called "Auction House" and to the right of it a combo box you can click on.
    * When clicking on the combo box, you will see a list of available auction houses that are currently open.
    * Simply left click on the Auction House you want to view. When doing this the Auction House will display its
    list of items available to you.
    * Lastly is the table you see on the client
      * You will see multiple columns describing properties of the item currently open for auction. 
      You will see the item's ID, Name (Description), Starting Bid (Minimum required), and the Time Remaining on the auction
    * If you want to bid on an item, simply double-click the row on the table.
    * After selecting an item, you'll see the item info populate on the left if you selected it properly.
    * The bid amount automatically increments by 1.00 when you select the item so you can easily out bid by 1.00 every time you 
    click on the item.
    * You can increase this to any amount you want, as long as you have the funds for it.
    * After placing a bid, you will see your balance update on the left.
    * If your bid is passed up, you receive your bidding funds back.
    * Every time an Agent bids on the item, the bid timer updates back to 30. This keeps happening
    until the time finally expires.
    * Once the timer has expired, the Agent clients update to refresh this change, and remove the item.
    * When the agent receives the win notification, you sign the 'paperwork' for bank to send the auction house
    funds for your item, which it waits for.
    
## Project Assumptions
A few things we have assumed during this project. 
* An auction item is only active when the first agent bids the minimum amount or more. But until then,
the items remain there. This allows for unscripted items going up for bid. This could've been also easily
implemented, but I assumed this worked out in the Auction House better, and really does have the same concept.

* When the bank component 'closes an account', this simply means it takes the account off of its hash map, so it no longer
interacts with this account ever again. The account object still exists within the bank, but we have no access to it. This
is because there was 'vagueness' on the instructions on closing accounts. But if we needed, we could easily add a few
lines of code to fully delete the account nulling it out before removing it from the hash map.

* Names and account IDs don't really do a whole lot in terms of the bank component. It just assigns the account to hold the ID.
Everything in Bank's accounts are tied up through a hash map that uses the secret keys given as its key. I believe this
is how Professor Roman wanted the secret keys to work, while still wanting the accounts to have an actual id.

## Versions 
The jar's for this program can be found in the head of directory "Auction-House": ~/Auction-House/'here'

### Bank v1.0_nathans13_cgarcia528_mgauba.jar
Refer to usage section on using the jars

### AuctionHouse v1.0_nathans13_cgarcia528_mgauba.jar
Refer to usage section on using the jars

## Agent v1.0_nathans13_cgarcia528_mgauba.jar
Refer to usage section on using the jars

## Status
### Implemented Features
It's to my knowledge that we fully implemented Professor Roman's design instruction for the components needed
in this project. We implemented every feature that was noted in the usage and then some more to ensure the programs
workability. 

Creation of Agent names: In Agent, the names are created by scanning a list of first names from a text field and passing 
it into the agent object. The agent then creates its own name by randomly picking indexes from the list and using two of 
the names picked as the agent's name. 

Creation of Item Names: The Auction House creates a list of randomly-generated items with the items including their own name and 
randomly-generated prices.Creation of Item Names and Prices: In Auction House, the names of the items are created by scanning a 
list of adjectives and nouns from a text file and passing it into the auction house object. The auction house then creates its 
item's name by randomly picking indexes from both lists and using the chosen adjective and noun. 

Creation of Item Prices: The auction house, upon creating the item, also randomly generates prices upon creation, choosing between a 
minimum bid amount of between 10 and 200. in the display, the minimum bid is automatically set up to increase from the orignal price by 
 1.00. 

### Known Issues
- When closing the Auction House I've noticed the agent's display doesn't always refresh to an empty table when you manually close 
the auction house. As we're near the project due date, I didn't find the time to track this bug down, it's not consistent enough to
be big issue. It isn't a program breaker as the agent can't spend money or interact with the auction house. The Agent just has to click
on another auction house as they normally would have to do if the auction house ended suddenly or ran out of items.

- Even though the time difference method was written to fix the machines times not matching, it doesn't appear to work
if the auction house is behind the agents machines. This is purely a GUI issue, and although the time left remaining might not be
the proper 30 seconds, it's still properly working in the Auction House and the agent will still be informed of winning the item.
To fix this, I would just compare the time to be greater or less than according to the machine and do the math from there.
It's not a program-breaking bug.

- In different computers, the Agent display does not fully show the display labels, including agent name, agent account, agent 
balance, item name, item ID number, starting bid, and current bid. Instead, it shows the first few letters along with an 
ellipses. We are not sure why this is happening but have theorized that it has to do with either monitor resolution or JavaFX
features clashing. However, these labels randomly display correctly in some resolutions.

- Lastly, as java uses floating point decimals (Uses fractions for doubles) when interacting with the agents balance, sometimes
a weird 'fraction' on balance will give us a long decimal of .0000000002 sometimes. This is a simple round up fix that we would need to implement.
As this isn't program breaking, we've shown that values already properly work, show that fund checking is working
and the proper funds goes into the Auction Houses account. I don't believe this is too big of a problem in the application.
It just comes down to running out of time to iron out every single bug.





## Testing and Debugging
- toString() in items that help print out the items properties when requested.
- Bank prints the agent and account's lockBalance, unlockBalance information 
in the console. It also prints the auction house accoundID getting money 
transferred from which agent accountID after agent won a bid. 
