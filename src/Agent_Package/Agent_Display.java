package Agent_Package;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
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
        agentID.setFont(new Font("Arial", 15));
        agentID.setStyle("-fx-font-weight: bold");

        Label agentAccount = new Label("Agent Account: 3229082");
        agentAccount.setFont(new Font("Arial", 15));
        agentAccount.setStyle("-fx-font-weight: bold");

        Label agentBalance = new Label("Agent Balance: 0943043");
        agentBalance.setFont(new Font("Arial", 15));
        agentBalance.setStyle("-fx-font-weight: bold");

        VBox agentLabels = new VBox(agentID, agentAccount, agentBalance);
        Button quitSim = new Button("Quit");
        quitSim.setFont(new Font("Arial", 10));
        quitSim.setStyle("-fx-font-weight: bold");
        quitSim.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.exit(0);
            }
        });
        agentInfo.setTop(agentLabels);
        agentInfo.setBottom(quitSim);

        VBox auctionHouseBox = new VBox();
        auctionHouseBox.setPadding(new Insets(20, 20, 20, 20));

        Label auctionHouseChoice = new Label("Auction House: ");
        auctionHouseChoice.setFont(new Font("Arial", 20));
        auctionHouseChoice.setStyle("-fx-font-weight: bold");

        ChoiceBox<String> auctionHouseList = new ChoiceBox<>();
        listOfHouses.addAll("House 1", "House 2");
        auctionHouseList.setItems(listOfHouses);
        auctionHouseList.getSelectionModel().selectFirst();

        HBox topBar = new HBox(auctionHouseChoice, auctionHouseList);
        auctionHouseBox.getChildren().addAll(topBar, table);

        table.setEditable(true);
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
        Scene scene = new Scene(borderpane, borderpane.getWidth(), borderpane.getHeight());
        stage.setScene(scene);
        stage.show();
    }
}
