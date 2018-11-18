
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

public class Agent_Display extends JPanel {
    private ObservableList<tableItem> listofTableItems = FXCollections.observableArrayList();
    private ObservableList<String> listOfHouses = FXCollections.observableArrayList();

    private TableView table = new TableView();
    
    public void drawGUI(Stage stage) {
        BorderPane agentInfo = new BorderPane();
        agentInfo.setPadding(new Insets(20, 20, 20, 20));

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
        /*agentLabels.setSpacing(10);
        Button quitSim = new Button("Quit");
        quitSim.setFont(new Font("Calibri", 10));
        quitSim.setStyle("-fx-font-weight: bold");
        quitSim.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.exit(0);
            }
        });
        */
        agentInfo.setTop(agentLabels);
        //agentInfo.setBottom(quitSim);

        VBox auctionHouseBox = new VBox();
        auctionHouseBox.setPadding(new Insets(20, 20, 20, 20));

        Label auctionHouseChoice = new Label("Auction House: ");
        auctionHouseChoice.setFont(new Font("Calibri", 25));
        auctionHouseChoice.setStyle("-fx-font-weight: bold; -fx-text-fill: white");

        /*
        ChoiceBox<String> auctionHouseList = new ChoiceBox<>();
        auctionHouseList.setMinWidth(100);
        listOfHouses.addAll("House 1", "House 2");
        auctionHouseList.setItems(listOfHouses);
        auctionHouseList.getSelectionModel().selectFirst();
        */

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
        TableColumn itemPlaceBid = new TableColumn("Item Place Bid");
        itemPlaceBid.setMinWidth(100);
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
        itemPlaceBid.setCellValueFactory(
                new PropertyValueFactory<tableItem, String>("itemPlaceBid")
        );

        //A list of items will be passed in. In final product, I will loop through the list and pass each item
        //to the constructor of new tableItem, which sets all the correct info to the right places on table view. Then,
        // the table data is set. To change list on board, you clear listofTableItems, add table items to it, then set the
        //info in the table to that.
        Item first = new Item("3333","22222","red table",400,330);
        Item second = new Item("666","9999","orange mat",200,100);
        Item third = new Item("4444","11111","green grass",20,10);
        listofTableItems.add(new tableItem(first));
        listofTableItems.add(new tableItem(second));
        listofTableItems.add(new tableItem(third));
        table.setItems(listofTableItems);
        table.getColumns().addAll(itemID, itemName, itemStartingBid, itemCurrentBid, itemPlaceBid);

        BorderPane borderpane = new BorderPane();
        borderpane.setLeft(agentInfo);
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
        private Button itemPlaceBid;

        public tableItem(Item itemPassedIn) {
            this.itemID = new SimpleStringProperty(itemPassedIn.getItemID());
            this.itemName = new SimpleStringProperty(itemPassedIn.getDescription());
            this.itemStartingBid = new SimpleStringProperty(Double.toString(itemPassedIn.getMinimumBidAmount()));
            this.itemCurrentBid = new SimpleStringProperty(Double.toString(itemPassedIn.getCurrentBidAmount()));
            this.itemPlaceBid = new Button("Place a bet");
            itemPlaceBid.setOnAction(e -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Make your Bet");
                alert.setHeaderText("Please place your bet in the text field:");
                GridPane biddingPane = new GridPane();
                TextField enterBid = new TextField();
                enterBid.setPromptText("Enter Your Bet");
                enterBid.setPrefColumnCount(10);
                enterBid.getText();
                biddingPane.add(enterBid,0,0);
                GridPane.setConstraints(enterBid, 0, 0);
                alert.getDialogPane().setContent(biddingPane);
                alert.showAndWait();
            });

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

        public Button getItemPlaceBid() {
            return itemPlaceBid;
        }

        public void setItemPlaceBid(Button itemPlaceBidString) {
            this.itemPlaceBid=itemPlaceBidString;
        }
    }
}
