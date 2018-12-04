package Auction_House;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedList;

public class Auction_House_Display {


    LinkedList<String> nouns;
    LinkedList<String> adjectives;
    private Auction_House auctionHouse;

    Label auctionHouseID;
    Label itemsRemainingLabel;
    Label auctionHouseBalance;

    public Auction_House_Display(Stage stage, LinkedList<String> nouns, LinkedList <String> adjectives) {

        this.nouns = nouns;
        this.adjectives = adjectives;
        drawGUI(stage);
    }


    public void drawGUI(Stage stage) {

        Label auctionHouseIDLabel = new Label("Auction House ID:");
        auctionHouseIDLabel.setFont(new Font("Calibri", 17));
        auctionHouseIDLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white");

        auctionHouseID = new Label("");
        auctionHouseID.setFont(new Font("Calibri", 17));
        auctionHouseID.setStyle("-fx-font-weight: bold; -fx-text-fill: white");

        HBox auctionKeyHBox = new HBox(auctionHouseIDLabel, auctionHouseID);
        HBox.setMargin(auctionKeyHBox, new Insets(10,0,0,10));
        auctionHouseID.setAlignment(Pos.CENTER);


        Label auctionHouseItemsLabel = new Label("Auction House Items Remaining:");
        auctionHouseItemsLabel.setFont(new Font("Calibri", 17));
        auctionHouseItemsLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white");

        itemsRemainingLabel = new Label("");
        itemsRemainingLabel.setFont(new Font("Calibri", 17));
        itemsRemainingLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white");

        HBox auctionInfoHBox = new HBox(auctionHouseItemsLabel, itemsRemainingLabel);
        HBox.setMargin(auctionInfoHBox, new Insets(10,0,0,10));
        auctionHouseItemsLabel.setAlignment(Pos.CENTER);




        Label auctionHouseBalanceLabel = new Label("Auction House Balance:");
        auctionHouseBalanceLabel.setFont(new Font("Calibri", 17));
        auctionHouseBalanceLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white");

        auctionHouseBalance = new Label("");
        auctionHouseBalance.setFont(new Font("Calibri", 17));
        auctionHouseBalance.setStyle("-fx-font-weight: bold; -fx-text-fill: white");

        HBox auctionHouseBalanceHBox = new HBox(auctionHouseBalanceLabel, auctionHouseBalance);
        HBox.setMargin(auctionHouseBalanceHBox, new Insets(10,0,0,10));
        auctionHouseBalanceLabel.setAlignment(Pos.CENTER);





        Button stopAuctionHouse = new Button("Close Auction House");
        stopAuctionHouse.setPrefWidth(200);
        stopAuctionHouse.setStyle("-fx-background-color: beige; -fx-font-weight: " +
                "bold; -fx-font: 14 arial");

        VBox auctionHouseVBox = new VBox(auctionKeyHBox, auctionInfoHBox, auctionHouseBalanceHBox, stopAuctionHouse);

        Pane pane = new Pane(auctionHouseVBox);
        pane.setBackground(new Background(new BackgroundFill(Color.rgb(54, 69, 79), CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(pane, 450, 400);
        stage.setScene(scene);
        stage.setTitle("Auction House Server");
        stage.show();







        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);

        Label auctionPortNumberLabel = new Label("Enter Auction House Server Port Number:");
        auctionPortNumberLabel.setFont(new Font("Calibri", 17));
        auctionPortNumberLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white");



        TextField portTextField = new TextField("6666");
        HBox.setMargin(portTextField, new Insets(0,0,0,10));
        portTextField.setAlignment(Pos.CENTER);
        HBox portHBox = new HBox(auctionPortNumberLabel, portTextField);

        Label bankServerPort = new Label("Enter Bank Server Port Number:");
        bankServerPort.setFont(new Font("Calibri", 17));
        bankServerPort.setStyle("-fx-font-weight: bold; -fx-text-fill: white");



        TextField bankPortTextField = new TextField("7277");
        bankPortTextField.setAlignment(Pos.CENTER);
        HBox.setMargin(bankPortTextField, new Insets(0,0,0,78));
        HBox bankPortHBox = new HBox(bankServerPort, bankPortTextField);


        Button startButton = new Button("Start Server");
        startButton.setPrefWidth(300);
        startButton.setStyle("-fx-background-color: beige; -fx-font-weight: " +
                "bold; -fx-font: 14 arial");
        HBox startButtonHbox = new HBox(startButton);
        startButtonHbox.setAlignment(Pos.CENTER);


        VBox dialogVbox = new VBox(15);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        dialogVbox.getChildren().addAll(bankPortHBox, portHBox, startButtonHbox);
        Scene dialogScene = new Scene(dialogVbox, 500, 150);
        dialogVbox.setBackground(new Background(new BackgroundFill(Color.rgb(54, 69, 79), CornerRadii.EMPTY, Insets.EMPTY)));
        dialog.setScene(dialogScene);
        dialog.show();

        startButton.setOnAction(e -> {

            int portNumber = Integer.parseInt(portTextField.getText());
            int bankPortNumber = Integer.parseInt(bankPortTextField.getText());
            dialog.hide();
            try {
                auctionHouse = new Auction_House(this, portNumber, bankPortNumber, nouns, adjectives);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.out.println("portn " + portNumber);
            System.out.println("portn " + bankPortNumber);

        });

        stopAuctionHouse.setOnAction(event -> {
            System.out.println("stopping ah");
            try {
                auctionHouse.stopAuctionHouse();
                auctionHouse = null;
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });






    }

    public int getPortNumber() {


        return 0;

    }
}
