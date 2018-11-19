
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.text.DecimalFormat;
import java.util.Stack;

public class Agent_Display extends JPanel {
    private ObservableList<tableItem> listofTableItems = FXCollections.observableArrayList();
    private ObservableList<String> listOfHouses = FXCollections.observableArrayList();

    private TableView<tableItem> table = new TableView();
    
    public void drawGUI(Stage stage) {
        BorderPane agentInfo = new BorderPane();
        agentInfo.setPadding(new Insets(23, 20, 20, 20));

        Label agentID = new Label("Agent ID: 3429234");
        agentID.setFont(new Font("Calibri", 20));
        agentID.setStyle("-fx-font-weight: bold; -fx-text-fill: white");

        Label agentAccount = new Label("Agent Account: 3229082");
        agentAccount.setFont(new Font("Calibri", 20));
        agentAccount.setStyle("-fx-font-weight: bold; -fx-text-fill: white");

        Label agentBalance = new Label("Agent Balance: 0943043");
        agentBalance.setFont(new Font("Calibri", 20));
        agentBalance.setStyle("-fx-font-weight: bold; -fx-text-fill: white");

        VBox agentLabels = new VBox(agentID, agentAccount, agentBalance);

        BorderPane itemInfo = new BorderPane();
        itemInfo.setPadding(new Insets(20, 20, 5, 20));

        Label itemIDLabel = new Label("Item ID:");
        itemIDLabel.setFont(new Font("Calibri", 20));
        itemIDLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white");

        Label itemDescriptionLabel = new Label("Item Name:");
        itemDescriptionLabel.setFont(new Font("Calibri", 20));
        itemDescriptionLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white");

        Label itemStartingBidLabel = new Label("Starting Bid:");
        itemStartingBidLabel.setFont(new Font("Calibri", 20));
        itemStartingBidLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white");

        Label itemCurrentBidLabel = new Label("Current Bid:");
        itemCurrentBidLabel.setFont(new Font("Calibri", 20));
        itemCurrentBidLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white");

        VBox itemLabels = new VBox(itemDescriptionLabel, itemIDLabel, itemStartingBidLabel, itemCurrentBidLabel);


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


        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "House 1", "House 2");
        ComboBox comboBox = new ComboBox(options);
        comboBox.setMinWidth(100);
        comboBox.getStyleClass().add("center-aligned");
        comboBox.getSelectionModel().selectFirst();


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
        itemName.setMinWidth(100);
        TableColumn itemStartingBid = new TableColumn("Item Starting Bid");
        itemStartingBid.setMinWidth(150);
        TableColumn itemCurrentBid = new TableColumn("Item Current Bid");
        itemCurrentBid.setMinWidth(150);
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

        //A list of items will be passed in. In final product, I will loop through the list and pass each item
        //to the constructor of new tableItem, which sets all the correct info to the right places on table view. Then,
        // the table data is set. To change list on board, you clear listofTableItems, add table items to it, then set the
        //info in the table to that.
        Item first = new Item("3333","22222","red table",400,330.75);
        Item second = new Item("666","9999","orange mat",200,100.02);
        Item third = new Item("4444","11111","green grass",20,10.20);
        listofTableItems.add(new tableItem(first));
        listofTableItems.add(new tableItem(second));
        listofTableItems.add(new tableItem(third));
        table.setItems(listofTableItems);
        table.getColumns().addAll(itemID, itemName, itemStartingBid, itemCurrentBid);


        table.setRowFactory(tv -> {
            TableRow row = new TableRow();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    DecimalFormat tempFormat = new DecimalFormat("#.00");
                    tableItem rowData = (tableItem) row.getItem();
                    itemDescriptionLabel.setText("Item Name: " + rowData.getItemName());
                    itemIDLabel.setText("Item ID: " + rowData.getItemID());
                    itemStartingBidLabel.setText("Starting Bid: " + rowData.getItemStartingBid());
                    itemCurrentBidLabel.setText("Current Bid: " + rowData.getItemCurrentBid());
                    double tempValue = Double.valueOf(rowData.getItemCurrentBid()) + 1.00;
                    bidAmount.setText(String.valueOf(tempFormat.format(tempValue)));
                }
            });
            return row;
        });

        VBox leftVBox = new VBox();
        leftVBox.getChildren().addAll(agentInfo, itemInfo, bidStackHBox);
        BorderPane borderpane = new BorderPane();
        borderpane.setLeft(leftVBox);
        borderpane.setCenter(auctionHouseBox);
        borderpane.setBackground(new Background(new BackgroundFill(Color.rgb(54, 69, 79), CornerRadii.EMPTY, Insets.EMPTY)));
        Scene scene = new Scene(borderpane, 1300, 600);
        stage.setScene(scene);
        stage.setTitle("Auction House Client");
        stage.show();
    }

    public static class tableItem {
        private final SimpleStringProperty itemID;
        private final SimpleStringProperty itemName;
        private final SimpleStringProperty itemStartingBid;
        private final SimpleStringProperty itemCurrentBid;
        DecimalFormat tempFormat = new DecimalFormat("#.00");

        public tableItem(Item itemPassedIn) {

            this.itemID = new SimpleStringProperty(itemPassedIn.getItemID());
            this.itemName = new SimpleStringProperty(itemPassedIn.getDescription());
            this.itemStartingBid = new SimpleStringProperty(tempFormat.format(itemPassedIn.getMinimumBidAmount()));
            this.itemCurrentBid = new SimpleStringProperty(tempFormat.format(itemPassedIn.getCurrentBidAmount()));
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
