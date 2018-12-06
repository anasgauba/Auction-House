/**Anas Guaba, Clarissa Garcia, and Nathan Schaefer
 * Agent Class
 *
 * @author Anas Guaba, Clarissa Garcia, Nathan Schaefer
 *
 * Agent Display is the class in charge of triggering commands such as bidding on items, displaying
 * account information, and receiving rejecction or acceptance notifications from the other components.
 */
package Agent;
import Auction_House.Item;
import Misc.Command;
import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class Agent_Display {
    public ObservableList<TableItem> listofTableItems = FXCollections.observableArrayList();
    private ObservableList<String> listOfHouses = FXCollections.observableArrayList();
    public ObservableList<String> options;
    public StringBuilder newNotificationMessage;
    public String newLine;
    public Label displayLabel;
    private String agentNameInfo;
    public TextField agentAccountTextField;
    public TextField agentBalanceTextField;
    public TableView<TableItem> table = new TableView();
    private Agent agent;
    boolean getHouseListDone = false;


    /**
     * The agent_Display constructor is used to set the values of the current agent's screen. It sets
     * the display's agent, the display label which is a string of all the notifications that are
     * received, the new notification string builder which will piece together a new line of notification
     * depending on the item and the accept/reject response, the agent's name label, and the dashes to
     * serperate notifications wiht newLine.
     * @param agent
     */
    public Agent_Display(Agent agent) {
        this.agent = agent;
        displayLabel = new Label("");
        newNotificationMessage = new StringBuilder();
        this.agentNameInfo=agent.agentName;
        newLine = "";
    }

    /**
     * The drawGui function is used to draw the GUI for the agent. It sets up the agent's name, balance,
     * account ID, and notification panel. It also has a table view to display the list of items and
     * a drop down menu so the user can choose from different auction houses.
     * @param stage - the default JavaFX stage object to display the GUI on
     */
    public void drawGUI(Stage stage) {
        BorderPane agentInfo = new BorderPane();
        agentInfo.setPadding(new Insets(27, 20, 20, 20));
        HBox alignHorizontal = new HBox();
        VBox labelInfo = new VBox();
        VBox textInfo = new VBox();
        Label agentName = new Label("Agent Name:");
        agentName.setFont(new Font("Calibri", 17));
        agentName.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
        agentName.setPadding(new Insets(1, 10, 0, 0));
        Label agentAccount = new Label("Agent Account:");
        agentAccount.setFont(new Font("Calibri", 17));
        agentAccount.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
        agentAccount.setPadding(new Insets(4, 10, 0, 0));
        Label agentBalance = new Label("Agent Balance:");
        agentBalance.setFont(new Font("Calibri", 17));
        agentBalance.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
        agentBalance.setPadding(new Insets(2, 10, 0, 0));
        labelInfo.getChildren().addAll(agentName, agentAccount, agentBalance);
        labelInfo.setSpacing(5);
        TextField agentNameTextField = new TextField(""+agentNameInfo);
        agentNameTextField.setAlignment(Pos.CENTER_RIGHT);
        agentNameTextField.setPrefWidth(132);
        agentNameTextField.setEditable(false);
        agentNameTextField.setFocusTraversable(false);
        agentAccountTextField = new TextField();
        agentAccountTextField.setAlignment(Pos.CENTER_RIGHT);
        agentAccountTextField.setPrefWidth(113);
        agentAccountTextField.setEditable(false);
        agentAccountTextField.setFocusTraversable(false);
        agentBalanceTextField = new TextField();
        agentBalanceTextField.setAlignment(Pos.CENTER_RIGHT);
        agentBalanceTextField.setPrefWidth(116);
        agentBalanceTextField.setEditable(false);
        agentBalanceTextField.setFocusTraversable(false);
        textInfo.getChildren().addAll(agentNameTextField,
                agentAccountTextField, agentBalanceTextField);
        textInfo.setSpacing(4);
        alignHorizontal.getChildren().addAll(labelInfo, textInfo);
        Button closeAccount = new Button("Close Account");
        closeAccount.setStyle("-fx-background-color: beige; -fx-font-weight: " +
                "bold; -fx-font: 14 arial");
        closeAccount.setPrefWidth(300);
        VBox agentLabels = new VBox(alignHorizontal, closeAccount);
        agentLabels.setMaxWidth(250);
        agentLabels.setMinWidth(250);
        agentLabels.setPrefWidth(250);
        agentLabels.setSpacing(5);
        /************************Item Info*******************************/
        BorderPane itemInfo = new BorderPane();
        itemInfo.setPadding(new Insets(20, 20, 5, 20));
        Label itemIDLabel = new Label("Item ID:");
        itemIDLabel.setFont(new Font("Calibri", 17));
        itemIDLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
        itemIDLabel.setPadding(new Insets(0, 10, 0, 0));
        TextField itemIDTextField = new TextField();
        itemIDTextField.setAlignment(Pos.CENTER_RIGHT);
        itemIDTextField.setPrefWidth(134);
        itemIDTextField.setEditable(false);
        itemIDTextField.setFocusTraversable(false);
        HBox itemIDTextFieldHBox = new HBox();
        itemIDTextFieldHBox.getChildren().addAll(itemIDLabel, itemIDTextField);
        HBox.setMargin(itemIDTextField, new Insets(0,0,0,50));
        Label itemDescriptionLabel = new Label("Item Name:");
        itemDescriptionLabel.setFont(new Font("Calibri", 17));
        itemDescriptionLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
        itemDescriptionLabel.setPadding(new Insets(0, 10, 0, 0));
        TextField itemDescriptionTextField = new TextField();
        itemDescriptionTextField.setAlignment(Pos.CENTER_RIGHT);
        itemDescriptionTextField.setPrefWidth(134);
        itemDescriptionTextField.setEditable(false);
        itemDescriptionTextField.setFocusTraversable(false);
        HBox itemIDDescriptionHBox = new HBox();
        itemIDDescriptionHBox.getChildren().addAll(itemDescriptionLabel, itemDescriptionTextField);
        HBox.setMargin(itemDescriptionTextField, new Insets(0,0,0,23));
        Label itemStartingBidLabel = new Label("Starting Bid:");
        itemStartingBidLabel.setFont(new Font("Calibri", 17));
        itemStartingBidLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
        itemStartingBidLabel.setPadding(new Insets(0, 8, 0, 0));
        TextField itemStartingBidTextField = new TextField();
        itemStartingBidTextField.setAlignment(Pos.CENTER_RIGHT);
        itemStartingBidTextField.setPrefWidth(134);
        itemStartingBidTextField.setEditable(false);
        itemStartingBidTextField.setFocusTraversable(false);
        HBox itemStartingBidHBox = new HBox();
        itemStartingBidHBox.getChildren().addAll(itemStartingBidLabel, itemStartingBidTextField);
        HBox.setMargin(itemStartingBidTextField, new Insets(0,0,0,22));
        Label itemCurrentBidLabel = new Label("Current Bid:");
        itemCurrentBidLabel.setFont(new Font("Calibri", 17));
        itemCurrentBidLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
        itemCurrentBidLabel.setPadding(new Insets(0, 10, 0, 0));
        TextField itemCurrentBidTextField = new TextField();
        itemCurrentBidTextField.setAlignment(Pos.CENTER_RIGHT);
        itemCurrentBidTextField.setPrefWidth(134);
        itemCurrentBidTextField.setEditable(false);
        itemCurrentBidTextField.setFocusTraversable(false);
        HBox itemCurrentBidHBox = new HBox();
        itemCurrentBidHBox.getChildren().addAll(itemCurrentBidLabel, itemCurrentBidTextField);
        HBox.setMargin(itemCurrentBidTextField, new Insets(0,0,0,21));
        VBox itemLabels = new VBox(itemIDDescriptionHBox, itemIDTextFieldHBox, itemStartingBidHBox, itemCurrentBidHBox);
        itemLabels.setMaxWidth(250);
        itemLabels.setMinWidth(250);
        itemLabels.setPrefWidth(250);
        itemLabels.setSpacing(5);
        /************************Item Info End*******************************/
        agentInfo.setTop(agentLabels);
        itemInfo.setTop(itemLabels);
        TextField bidAmount = new TextField("0.00");
        bidAmount.setPrefWidth(100);
        bidAmount.setAlignment(Pos.CENTER_RIGHT);
        bidAmount.setFocusTraversable(false);
        bidAmount.setOnMouseClicked(event -> {
            bidAmount.setText("");
        });
        Button placeBid = new Button("Place Bid");
        placeBid.setPrefWidth(134);
        placeBid.setStyle("-fx-background-color: beige; -fx-font-weight: " +
                "bold; -fx-font: 14 arial");
        HBox bidStackHBox = new HBox();
        bidStackHBox.getChildren().addAll(bidAmount, placeBid);
        bidStackHBox.setPadding(new Insets(0, 0, 20, 20));
        bidStackHBox.setSpacing(10);
        HBox.setMargin(placeBid, new Insets(0,0,0,7));
        VBox auctionHouseBox = new VBox();
        auctionHouseBox.setPadding(new Insets(20, 20, 20, 20));
        Label auctionHouseChoice = new Label("Auction House: ");
        auctionHouseChoice.setFont(new Font("Calibri", 25));
        auctionHouseChoice.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
        options = FXCollections.observableArrayList();
        ComboBox comboBox = new ComboBox(options);
        comboBox.setMinWidth(100);
        comboBox.getStyleClass().add("center-aligned");
        ScrollPane messagesPane = new ScrollPane();
        displayLabel.setTextAlignment(TextAlignment.LEFT);
        messagesPane.setPrefSize(200, 213);
        messagesPane.setContent(displayLabel);
        String notifications = "";
        displayLabel.setText(notifications);
        messagesPane.setFitToWidth(true);
        displayLabel.setWrapText(true);
        BorderPane notificationsPane = new BorderPane(messagesPane);
        notificationsPane.setPadding(new Insets(20, 20, 5, 20));
        placeBid.setOnAction(event -> {
            try {
                agent.sendBid(itemIDTextField.getText(), Double.parseDouble(bidAmount.getText()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        HBox topBar = new HBox(auctionHouseChoice, comboBox);
        topBar.setSpacing(5);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 0, 10, 0));
        auctionHouseBox.getChildren().addAll(topBar, table);
        table.setEditable(true);
        table.setPrefHeight(700);
        table.setPadding(new Insets(0, 0, 20, 0));
        table.setBackground(new Background(new BackgroundFill(Color.rgb(171, 171, 171), CornerRadii.EMPTY, Insets.EMPTY)));
        TableColumn itemID = new TableColumn("Item ID");
        itemID.setMinWidth(100);
        TableColumn itemName = new TableColumn("Item Name");
        itemName.setMinWidth(200);
        itemName.setStyle("-fx-alignment: CENTER;");
        TableColumn itemStartingBid = new TableColumn("Starting Bid");
        itemStartingBid.setMinWidth(150);
        itemStartingBid.setStyle("-fx-alignment: CENTER-RIGHT;");
        TableColumn itemCurrentBid = new TableColumn("Current Bid");
        itemCurrentBid.setMinWidth(150);
        itemCurrentBid.setStyle("-fx-alignment: CENTER-RIGHT;");
        TableColumn itemTime = new TableColumn("Item Time Remaining");
        itemTime.setMinWidth(150);
        itemTime.setStyle("-fx-alignment: CENTER;");
        itemID.setCellValueFactory(
                new PropertyValueFactory<TableItem, String>("itemID")
        );
        itemName.setCellValueFactory(
                new PropertyValueFactory<TableItem, String>("itemName")
        );
        itemStartingBid.setCellValueFactory(
                new PropertyValueFactory<TableItem, String>("itemStartingBid")
        );
        itemCurrentBid.setCellValueFactory(
                new PropertyValueFactory<TableItem, String>("itemCurrentBid")
        );
        itemTime.setCellValueFactory(
                new PropertyValueFactory<TableItem, String>("itemTime")
        );
        table.getColumns().addAll(itemID, itemName, itemStartingBid, itemCurrentBid, itemTime);
        table.setRowFactory(tv -> {
            TableRow row = new TableRow();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    DecimalFormat tempFormat = new DecimalFormat("#.00");
                    TableItem rowData = (TableItem) row.getItem();
                    itemDescriptionTextField.setText(rowData.getItemName());
                    itemIDTextField.setText(rowData.getItemID());
                    itemStartingBidTextField.setText(rowData.getItemStartingBid());
                    itemCurrentBidTextField.setText(rowData.getItemCurrentBid());
                    double tempValue = Double.valueOf(rowData.getItemCurrentBid()) + 1.00;
                    bidAmount.setText(String.valueOf(tempFormat.format(tempValue)));
                }
            });
            return row;
        });
        VBox leftVBox = new VBox();
        leftVBox.getChildren().addAll(agentInfo, itemInfo, bidStackHBox, notificationsPane);
        BorderPane borderpane = new BorderPane();
        borderpane.setLeft(leftVBox);
        borderpane.setCenter(auctionHouseBox);
        borderpane.setBackground(new Background(new BackgroundFill(Color.rgb(54, 69, 79), CornerRadii.EMPTY, Insets.EMPTY)));
        Scene scene = new Scene(borderpane, 1300, 600);
        stage.setScene(scene);
        stage.setTitle("Auction House Client");
        stage.show();
        AnimationTimer animationTimer = new AnimationTimer() {
            private long nextTime = 0;
            @Override
            public void handle(long now) {
                if (now > nextTime) {
                    nextTime = now + 1000000000;
                    agent.refreshTimes();
                    table.refresh();
                }
            }
        };
        animationTimer.start();
        synchronized (agent) {
            agent.notifyAll();
        }
        Object[] message = {Command.GetListItems};
        try {
            agent.getHouseList();

        } catch (IOException e) {
            e.printStackTrace();
        }
        synchronized (this) {
            while (!getHouseListDone) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        getHouseListDone = false;
        comboBox.getSelectionModel().selectFirst(); //no idea why this doesn work
        message[0] = Command.GetListItems;
        try {
            String tempAuctionID = (String) comboBox.getValue();
            if (tempAuctionID != null) {
                agent.setCurrentAuctionHouse(Integer.valueOf(tempAuctionID));
                synchronized (this) {
                    while (agent.clients.get(Integer.valueOf(tempAuctionID)).clientOutput == null) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                agent.clients.get(Integer.valueOf(tempAuctionID)).clientOutput.writeObject(new Object[]{Command.GetListItems});
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        comboBox.setOnMouseClicked(e -> {
            comboBox.hide();
            synchronized (this) {
                while (!getHouseListDone) {
                    try {
                        options.clear();
                        agent.getHouseList();
                        wait();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            comboBox.show();
            getHouseListDone = false;
        });
        comboBox.setOnAction(e -> {
            if (comboBox.getValue() != null) {
                listofTableItems.clear();
                message[0] = Command.GetListItems;
                try {
                    String tempAuctionID = (String) comboBox.getValue();
                    agent.setCurrentAuctionHouse(Integer.valueOf(tempAuctionID));
                    agent.clients.get(Integer.valueOf(tempAuctionID)).clientOutput.writeObject(message);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        closeAccount.setOnAction(event -> {
            try {
                boolean tryClosing = agent.closeAccount();
                if (tryClosing){
                    agent = null;
                    System.exit(0);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * The setNewNotificationMesssage is a method that sets the notification panel to the
     * string builder that collects all of the past notifications into a string.
     */
    public void setNewNotificationMessage() {
        newNotificationMessage.insert(0, newLine + "-----------------------------" +
                "-------------------\n");
        newLine="";
        displayLabel.setText(this.newNotificationMessage.toString());
    }

    /**
     * The setAccountBalance is a method that, when passed in a balance, sets the
     * account balance of the current agent on the display to the amount passed in.
     * @param agentBalance - new balance of the agent to be displayed
     */
    public void setAccountBalance(double agentBalance){
        this.agentBalanceTextField.setText(Double.toString(agentBalance));

    }

    /**
     * The setAccountID method, when passed in an accountID, sets the display to
     * show the accountID.
     * @param accountIDPassed - the account ID to be shown
     */
    public void setAccountID(int accountIDPassed){
        this.agentAccountTextField.setText(Integer.toString(accountIDPassed));
    }


    /**
     * The TableItem class is the required class needed when setting up a tableView, which
     * the GUI utilizes to show items and their information. The table class shows string
     * properties that will be set to the columns, such as the itemID, the item name,
     * the itemStartingBid, the itemCurrentBid, and the item time.
     */
    public static class TableItem {
        private final SimpleStringProperty itemID;
        private final SimpleStringProperty itemName;
        private final SimpleStringProperty itemStartingBid;
        private final SimpleStringProperty itemCurrentBid;
        private final SimpleStringProperty itemTime;
        DecimalFormat tempFormat = new DecimalFormat("#.00");

        /**
         * The constructor of Tableitem allows for an actual item object to be passed in.
         * It then grabs the information of the item and disperses it among the fields of
         * the new TableItem so that the can be properly displayed in the table.
         * @param itemPassedIn - the item to be converted into a tableItem
         */

        public TableItem(Item itemPassedIn) {
            this.itemID = new SimpleStringProperty(itemPassedIn.getItemID());
            this.itemName = new SimpleStringProperty(itemPassedIn.getDescription());
            this.itemStartingBid = new SimpleStringProperty(tempFormat.format(itemPassedIn.getMinimumBidAmount()));
            this.itemCurrentBid = new SimpleStringProperty(tempFormat.format(itemPassedIn.getCurrentBidAmount()));

            if (itemPassedIn.getBidTimeRemaining() > 0) {
                long secondsRemaining = TimeUnit.MILLISECONDS.toSeconds(itemPassedIn.getBidTimeRemaining() - System.currentTimeMillis());
                this.itemTime = new SimpleStringProperty(Long.toString(secondsRemaining));
            } else {
                this.itemTime = new SimpleStringProperty("0");
            }
        }

        /**
         * The simple getItemTime method is a method that retrieves the time from the current
         * tableItem object.
         * @return - time of the tableItemObject
         */
        public String getItemTime() {
            return itemTime.get();
        }

        /**
         * setItemTime method is a setter method that sets the time of the current tableItem object.
         * @param itemTimeRemaining - time to be set of the tableItem object.
         */
        public void setItemTime(String itemTimeRemaining) {
            itemTime.set(itemTimeRemaining);
        }

        /**
         * getItemID is a getter method that retrieves the tableItem's ID
         * @return - tableItemID
         */
        public String getItemID() {
            return itemID.get();
        }

        /**
         * The setItemID is a method that, when passed and ItemID, sets the TableItem's ID
         * @param itemIDString - tableItemID to be set
         */
        public void setItemID(String itemIDString) {
            itemID.set(itemIDString);
        }

        /**
         * The getItemName method is a getter method that retrieves the tableItem's name
         * @return - tableItem's name
         */
        public String getItemName() {
            return itemName.get();
        }

        /**
         * The setItemName method is a method that, when passed in a string, sets the tableItem's name
         * @param itemNameString - tableItem's name to be set
         */
        public void setItemName(String itemNameString) {
            itemName.set(itemNameString);
        }

        /**
         * The getItemStartingBid is a method that retrieves the tableItem's starting bid
         * @return - the current tableItem's starting bid
         */
        public String getItemStartingBid() {
            return itemStartingBid.get();
        }

        /**
         * The setItemStartingBid is a method that, when passed in a string, sets the tableItem's starting bid
         *
         * @param itemStartingBidString - the item's starting bid to be set
         */
        public void setItemStartingBid(String itemStartingBidString) {
            itemStartingBid.set(itemStartingBidString);
        }

        /**
         * The getItemCurrentBid method is a method that, when called, retrieves the tableItem's current bid
         * @return - current tableItem's current bid
         */
        public String getItemCurrentBid() {
            return itemCurrentBid.get();
        }

        /**
         * The setItemCurrentBid is a method that, when called, sets the taleItem's current bid
         * @param itemCurrentBidString - string of item current bid to be set.
         */
        public void setItemCurrentBid(String itemCurrentBidString) {
            itemStartingBid.set(itemCurrentBidString);
        }
    }
}
