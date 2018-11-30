package Agent;

import Auction_House.Auction_House;
import Auction_House.Item;
import Bank.Bank;
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

import javax.swing.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class Agent_Display extends JPanel {
    public ObservableList<tableItem> listofTableItems = FXCollections.observableArrayList();
    private ObservableList<String> listOfHouses = FXCollections.observableArrayList();
    public ObservableList<String> options;
    public StringBuilder newNotificationMessage;
    public String newLine;
    public Label displayLabel;
    private String agentNameInfo;
    public TextField agentAccountTextField;
    public TextField agentBalanceTextField;

    public TableView<tableItem> table = new TableView();

    private Agent agent;
    private Auction_House auctionHouse;
    private Bank bank;

    boolean getHouseListDone = false;


    public Agent_Display(Agent agent) {
        this.agent = agent;
        displayLabel = new Label("");
        newNotificationMessage = new StringBuilder();
        this.agentNameInfo=agent.agentName;
        newLine = "";
        //agentAccountTextField = new Label();
        //agentBalanceTextField = new Label();

//        this.auctionHouse = auctionHouse;
//        this.bank = bank;

    }

    public void drawGUI(Stage stage) {
        BorderPane agentInfo = new BorderPane();
        agentInfo.setPadding(new Insets(27, 20, 20, 20));

        Label agentName = new Label("Agent Name:");
        agentName.setFont(new Font("Calibri", 20));
        agentName.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
        agentName.setPadding(new Insets(0, 10, 0, 0));

        TextField agentNameTextField = new TextField(""+agentNameInfo);
        agentNameTextField.setAlignment(Pos.CENTER_RIGHT);
        agentNameTextField.setPrefWidth(163);
        agentNameTextField.setEditable(false);
        //agentNameTextField.setDisable(true);
        agentNameTextField.setFocusTraversable(false);
        HBox agentNameHBox = new HBox();
        agentNameHBox.getChildren().addAll(agentName, agentNameTextField);

        Label agentAccount = new Label("Agent Account:");
        agentAccount.setFont(new Font("Calibri", 20));
        agentAccount.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
        agentAccount.setPadding(new Insets(0, 10, 0, 0));

        agentAccountTextField = new TextField();
        agentAccountTextField.setAlignment(Pos.CENTER_RIGHT);
        agentAccountTextField.setPrefWidth(113);
        agentAccountTextField.setEditable(false);
        //agentAccoutnTextField.setDisable(true);
        agentAccountTextField.setFocusTraversable(false);
        HBox agentAccountHBox = new HBox();
        agentAccountHBox.getChildren().addAll(agentAccount, agentAccountTextField);


        Label agentBalance = new Label("Agent Balance:");
        agentBalance.setFont(new Font("Calibri", 20));
        agentBalance.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
        agentBalance.setPadding(new Insets(0, 10, 0, 0));

        agentBalanceTextField = new TextField();
        agentBalanceTextField.setAlignment(Pos.CENTER_RIGHT);
        agentBalanceTextField.setPrefWidth(116);
        agentBalanceTextField.setEditable(false);
        //agentBalanceTextField.setDisable(true);
        agentBalanceTextField.setFocusTraversable(false);
        HBox agentBalanceHBox = new HBox();
        agentBalanceHBox.getChildren().addAll(agentBalance, agentBalanceTextField);


        VBox agentLabels = new VBox(agentNameHBox, agentAccountHBox, agentBalanceHBox);
        agentLabels.setMaxWidth(250);
        agentLabels.setMinWidth(250);
        agentLabels.setPrefWidth(250);
        agentLabels.setSpacing(5);
        agentLabels.setSpacing(5);


        /************************item info start*******************************/
        BorderPane itemInfo = new BorderPane();
        itemInfo.setPadding(new Insets(20, 20, 5, 20));

        Label itemIDLabel = new Label("Item ID:");
        itemIDLabel.setFont(new Font("Calibri", 20));
        itemIDLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
        itemIDLabel.setPadding(new Insets(0, 10, 0, 0));

        TextField itemIDTextField = new TextField();
        itemIDTextField.setAlignment(Pos.CENTER_RIGHT);
        itemIDTextField.setPrefWidth(174);
        itemIDTextField.setEditable(false);
        //itemIDTextField.setDisable(true);
        itemIDTextField.setFocusTraversable(false);
        HBox itemIDTextFieldHBox = new HBox();
        itemIDTextFieldHBox.getChildren().addAll(itemIDLabel, itemIDTextField);


        Label itemDescriptionLabel = new Label("Item Name:");
        itemDescriptionLabel.setFont(new Font("Calibri", 20));
        itemDescriptionLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
        itemDescriptionLabel.setPadding(new Insets(0, 10, 0, 0));

        TextField itemDescriptionTextField = new TextField();
        itemDescriptionTextField.setAlignment(Pos.CENTER_RIGHT);
        itemDescriptionTextField.setPrefWidth(142);
        itemDescriptionTextField.setEditable(false);
        //itemDescriptionTextField.setDisable(true);
        itemDescriptionTextField.setFocusTraversable(false);
        HBox itemIDDescriptionHBox = new HBox();
        itemIDDescriptionHBox.getChildren().addAll(itemDescriptionLabel, itemDescriptionTextField);


        Label itemStartingBidLabel = new Label("Starting Bid:");
        itemStartingBidLabel.setFont(new Font("Calibri", 20));
        itemStartingBidLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
        itemStartingBidLabel.setPadding(new Insets(0, 8, 0, 0));

        TextField itemStartingBidTextField = new TextField();
        itemStartingBidTextField.setAlignment(Pos.CENTER_RIGHT);
        itemStartingBidTextField.setPrefWidth(140);
        itemStartingBidTextField.setEditable(false);
        //itemStartingBidTextField.setDisable(true);
        itemStartingBidTextField.setFocusTraversable(false);
        HBox itemStartingBidHBox = new HBox();
        itemStartingBidHBox.getChildren().addAll(itemStartingBidLabel, itemStartingBidTextField);


        Label itemCurrentBidLabel = new Label("Current Bid:");
        itemCurrentBidLabel.setFont(new Font("Calibri", 20));
        itemCurrentBidLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
        itemCurrentBidLabel.setPadding(new Insets(0, 10, 0, 0));

        TextField itemCurrentBidTextField = new TextField();
        itemCurrentBidTextField.setAlignment(Pos.CENTER_RIGHT);
        itemCurrentBidTextField.setPrefWidth(140);
        itemCurrentBidTextField.setEditable(false);
        //itemCurrentBidTextField.setDisable(true);
        itemCurrentBidTextField.setFocusTraversable(false);
        HBox itemCurrentBidHBox = new HBox();
        itemCurrentBidHBox.getChildren().addAll(itemCurrentBidLabel, itemCurrentBidTextField);


        VBox itemLabels = new VBox(itemIDDescriptionHBox, itemIDTextFieldHBox, itemStartingBidHBox, itemCurrentBidHBox);
        itemLabels.setMaxWidth(250);
        itemLabels.setMinWidth(250);
        itemLabels.setPrefWidth(250);
        itemLabels.setSpacing(5);

        /************************item info end*******************************/

        agentInfo.setTop(agentLabels);
        itemInfo.setTop(itemLabels);

        TextField bidAmount = new TextField("0.00");
        bidAmount.setPrefWidth(100);
        bidAmount.setAlignment(Pos.CENTER_RIGHT);
        bidAmount.setFocusTraversable(false);
        //bidAmount.setEditable(false);
        //bidAmount.setDisable(true);  //tomorrow maybe add these for the entries similar to
        //how I do it for the button and bit amount fields, hbox,etc
        bidAmount.setOnMouseClicked(event -> {
            bidAmount.setText("");
        });

        Button placeBid = new Button("Place Bid");
        placeBid.setPrefWidth(75);


        HBox bidStackHBox = new HBox();
        bidStackHBox.getChildren().addAll(bidAmount, placeBid);
        bidStackHBox.setPadding(new Insets(0, 0, 20, 20));
        bidStackHBox.setSpacing(10);


        VBox auctionHouseBox = new VBox();
        auctionHouseBox.setPadding(new Insets(20, 20, 20, 20));

        Label auctionHouseChoice = new Label("Auction House: ");
        auctionHouseChoice.setFont(new Font("Calibri", 25));
        auctionHouseChoice.setStyle("-fx-font-weight: bold; -fx-text-fill: white");


        options = FXCollections.observableArrayList();
        ComboBox comboBox = new ComboBox(options);
        comboBox.setMinWidth(100);
        comboBox.getStyleClass().add("center-aligned");


//        initialize the selected first

        ScrollPane messagesPane = new ScrollPane();
//        displayLabel.setWrappingWidth(500);

        displayLabel.setTextAlignment(TextAlignment.LEFT);
        messagesPane.setPrefSize(200, 200);
        messagesPane.setContent(displayLabel);
        String notifications = "This is a test";
        displayLabel.setText(notifications);
        placeBid.setOnAction(event -> {
            try {
                agent.sendBid(itemIDTextField.getText(), Double.parseDouble(bidAmount.getText()));
                newLine=("Auction House " + comboBox.getSelectionModel().getSelectedItem().toString()
                        + " Item: " + itemDescriptionTextField.getText()+" ");
                System.out.println("Placebid clicked message is "+newNotificationMessage);
//                setNewNotificationMessage();

//                displayLabel.setText(newNotificationMessage);

            } catch (IOException e) {
                e.printStackTrace();
            }
            //agent.getListActiveAuctions();
        });



        comboBox.setOnMouseClicked(e -> {
            //agent.getHouseList();
        });


        //HBox topBar = new HBox(auctionHouseChoice, auctionHouseList);
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
                new PropertyValueFactory<tableItem, String>("itemID")
        );

        itemName.setCellValueFactory(
                new PropertyValueFactory<tableItem, String>("itemName")
        );

        itemStartingBid.setCellValueFactory(
                new PropertyValueFactory<tableItem, String>("itemStartingBid")
        );
        itemCurrentBid.setCellValueFactory(
                new PropertyValueFactory<tableItem, String>("itemCurrentBid")
        );
        itemTime.setCellValueFactory(
                new PropertyValueFactory<tableItem, String>("itemTime")
        );


        //A list of items will be passed in. In final product, I will loop through the list and pass each item
        //to the constructor of new tableItem, which sets all the correct info to the right places on table view. Then,
        // the table data is set. To change list on board, you clear listofTableItems, add table items to it, then set the
        //info in the table to that.
//        Auction_House.Item first = new Auction_House.Item(3333,"22222","@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@",400,330.75);
//        Auction_House.Item second = new Auction_House.Item(666,"9999","orange mat",200,100.02);
//        Auction_House.Item third = new Auction_House.Item(4444,"11111","green grass",20,10.20);
//        listofTableItems.add(new tableItem(first));
//        listofTableItems.add(new tableItem(second));
//        listofTableItems.add(new tableItem(third));
//        table.setItems(listofTableItems);
        table.getColumns().addAll(itemID, itemName, itemStartingBid, itemCurrentBid, itemTime);


        table.setRowFactory(tv -> {
            TableRow row = new TableRow();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    DecimalFormat tempFormat = new DecimalFormat("#.00");
                    tableItem rowData = (tableItem) row.getItem();
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
        //You are going to add your scrollpane in the below line
        leftVBox.getChildren().addAll(agentInfo, itemInfo, bidStackHBox, messagesPane);
        BorderPane borderpane = new BorderPane();
        borderpane.setLeft(leftVBox);
        borderpane.setCenter(auctionHouseBox);
        borderpane.setBackground(new Background(new BackgroundFill(Color.rgb(54, 69, 79), CornerRadii.EMPTY, Insets.EMPTY)));
        Scene scene = new Scene(borderpane, 1300, 600);
        stage.setScene(scene);
        stage.setTitle("Auction House Client");
        stage.show();


        /******************debug*********************/
//        agentNameTextField.setText("10034");
//        agentAccountTextField.setText("00900");
//        agentBalanceTextField.setText("3000.03");



        AnimationTimer animationTimer = new AnimationTimer() {
            private long nextTime = 0;

            @Override
            public void handle(long now) {

                if (now > nextTime) {

                    nextTime = now + 1000000000;
                    /*
                    listofTableItems.clear();
                    message[0] = Command.GetListItems;
                    String tempAuctionID = (String) comboBox.getValue();
                    agent.setCurrentAuctionHouse(Integer.valueOf(tempAuctionID));
                    try {
                        agent.clients.get(Integer.valueOf(tempAuctionID)).clientOutput.writeObject(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    */
                    agent.refreshTimes();
                    table.refresh();

                    //table.getColumns().get(0).setVisible(false);
                    //table.getColumns().get(0).setVisible(true);

                }
            }

        };
        animationTimer.start();

        synchronized (agent) {
            agent.notifyAll();
            System.out.println("???");
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
                    System.out.println("done getting house list" + options);
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

            System.out.println("temp id " + tempAuctionID);

            System.out.println("temp id2 " + tempAuctionID);

//            synchronized ()
            agent.setCurrentAuctionHouse(Integer.valueOf(tempAuctionID));
            System.out.println("agents lcients " + agent.clients);

            System.out.println("agents lcients 2" + agent.clients.get(Integer.valueOf(tempAuctionID)));
            System.out.println("agents lcients 3" + agent.clients.get(Integer.valueOf(tempAuctionID)).clientOutput);

            synchronized (this) {
                while (agent.clients.get(Integer.valueOf(tempAuctionID)).clientOutput == null) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            agent.clients.get(Integer.valueOf(tempAuctionID)).clientOutput.writeObject(message);
        } catch (IOException e1) {
            e1.printStackTrace();
        }


        comboBox.setOnAction(e -> {
            listofTableItems.clear();
            message[0] = Command.GetListItems;
            try {
                //System.out.println("work ree " + comboBox.getSelectionModel().getSelectedItem().getClass());
                String tempAuctionID = (String) comboBox.getValue();
                agent.setCurrentAuctionHouse(Integer.valueOf(tempAuctionID));
                agent.clients.get(Integer.valueOf(tempAuctionID)).clientOutput.writeObject(message);
                //agent.getHouseList();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });



    }

    public void setNewNotificationMessage() {
        newNotificationMessage.insert(0,newLine);
        newLine="";
        displayLabel.setText(this.newNotificationMessage.toString());
    }

    public void setAccountBalance(double agentBalance){
        System.out.println("account balance  " + agentBalance);
        this.agentBalanceTextField.setText(Double.toString(agentBalance));

    }
    public void setAccountID(int accountIDPassed){
        System.out.println("account id paassed " + accountIDPassed);
        this.agentAccountTextField.setText(Integer.toString(accountIDPassed));

    }
    public static class tableItem {
        private final SimpleStringProperty itemID;
        private final SimpleStringProperty itemName;
        private final SimpleStringProperty itemStartingBid;
        private final SimpleStringProperty itemCurrentBid;
        private final SimpleStringProperty itemTime;
        DecimalFormat tempFormat = new DecimalFormat("#.00");

        public tableItem(Item itemPassedIn) {

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

        public String getItemTime() {

            return itemTime.get();
        }

        public void setItemTime(String itemTimeRemaining) {
            itemTime.set(itemTimeRemaining);
        }

        public String getItemID() {
            return itemID.get();
        }

        public void setItemID(String itemIDString) {
            itemID.set(itemIDString);
        }

        public String getItemName() {
            return itemName.get();
        }

        public void setItemName(String itemNameString) {
            itemName.set(itemNameString);
        }

        public String getItemStartingBid() {
            return itemStartingBid.get();
        }

        public void setItemStartingBid(String itemStartingBidString) {
            itemStartingBid.set(itemStartingBidString);
        }

        public String getItemCurrentBid() {
            return itemCurrentBid.get();
        }

        public void setItemCurrentBid(String itemCurrentBidString) {
            itemStartingBid.set(itemCurrentBidString);
        }
    }
}
