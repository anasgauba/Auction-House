package Agent_Package;

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
import javafx.stage.Stage;
import javax.swing.*;

public class Agent_Display extends JPanel {
    private ObservableList<String> data = FXCollections.observableArrayList();
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
                new PropertyValueFactory<Agent, String>("agentCoordinate")
        );
        itemName.setCellValueFactory(
                new PropertyValueFactory<Agent, String>("agentID")
        );
        itemStartingBid.setCellValueFactory(
                new PropertyValueFactory<Agent, String>("agentID")
        );
        itemCurrentBid.setCellValueFactory(
                new PropertyValueFactory<Agent, String>("agentID")
        );
        itemPlaceBid.setCellValueFactory(
                new PropertyValueFactory<Agent, String>("agentID")
        );
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
}
