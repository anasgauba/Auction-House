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

    public Auction_House_Display(Stage stage, LinkedList<String> nouns, LinkedList <String> adjectives) {
        this.auctionHouse = auctionHouse;

        this.nouns = nouns;
        this.adjectives = adjectives;
        drawGUI(stage);
    }


    public void drawGUI(Stage stage) {

        Pane pane = new Pane();
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
        HBox.setMargin(portTextField, new Insets(0,0,0,20));
        portTextField.setAlignment(Pos.CENTER);
        HBox portHBox = new HBox(auctionPortNumberLabel, portTextField);

        Label bankServerPort = new Label("Enter Bank Server Port Number:");
        bankServerPort.setFont(new Font("Calibri", 17));
        bankServerPort.setStyle("-fx-font-weight: bold; -fx-text-fill: white");



        TextField bankPortTextField = new TextField("7277");
        HBox.setMargin(bankPortTextField, new Insets(0,0,0,20));
        bankPortTextField.setAlignment(Pos.CENTER);
        HBox bankPortHBox = new HBox(bankServerPort, bankPortTextField);


        Button startButton = new Button("Start Server");
        startButton.setPrefWidth(357);
        startButton.setStyle("-fx-background-color: beige; -fx-font-weight: " +
                "bold; -fx-font: 14 arial");


        VBox dialogVbox = new VBox(15);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        dialogVbox.getChildren().addAll(bankPortHBox, portHBox, startButton);
        Scene dialogScene = new Scene(dialogVbox, 450, 150);
        dialogVbox.setBackground(new Background(new BackgroundFill(Color.rgb(54, 69, 79), CornerRadii.EMPTY, Insets.EMPTY)));
        dialog.setScene(dialogScene);
        dialog.show();




        startButton.setOnAction(e -> {


            int portNumber = Integer.parseInt(portTextField.getText());
            int bankPortNumber = Integer.parseInt(bankPortTextField.getText());
            dialog.hide();
            try {
                Auction_House auction_house = new Auction_House(portNumber, bankPortNumber, nouns, adjectives);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.out.println("portn " + portNumber);
            System.out.println("portn " + bankPortNumber);

        });
    }

    public int getPortNumber() {


        return 0;

    }
}
