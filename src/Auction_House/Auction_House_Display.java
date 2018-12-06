/*Anas Guaba, Clarissa Garcia, and Nathan Schaefer
 * Auction_House_Display Class
 * <p>
 *
 * @author Nathan Schaefer
 *
 * Auction House Display is a small GUI that used to issue the port number that will start the auction house server.
 * It will then create the auction server at that port and open another window that will allow the user
 * to close the auction house and exit the program. The new window also displays the auction house items left,
 * the auction house ID, and the balance of the auction house as it sells items
 */

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

/**
 * The Bank Display class is a small GUI that used to issue the port number that will start the bank server.
 * It contains a private auction house, and a list of nouns and adjective that will later be used to create
 * the items in the auction house object. It also contains labels that will be used to display the auction
 * house ID, the items remaining, and the current auction house balance.
 */
public class Auction_House_Display {
    LinkedList<String> nouns;
    LinkedList<String> adjectives;
    private Auction_House auctionHouse;
    Label auctionHouseID;
    Label itemsRemainingLabel;
    Label auctionHouseBalance;

    /**
     * This stop method, when called, stops the auction house thread
     * before making the auction house null, effectively erasing the
     * auction house.
     */
    public void stop() {
        try {
            auctionHouse.stopAuctionHouse();
            auctionHouse = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The constructor for  the auction house display initializes its list of nouns and adjectives
     * with the lists that are passed in. It also calls for the drawing of the GUI by calling the
     * drawGUI method
     * @param stage - the stage in which the GUI is drawn
     * @param nouns - the list of nouns the auction house will have to make items.
     * @param adjectives -  the list of nouns the auction house will have to make items.
     */
    public Auction_House_Display(Stage stage, LinkedList<String> nouns, LinkedList <String> adjectives) {
        this.nouns = nouns;
        this.adjectives = adjectives;
        drawGUI(stage);
    }

    /**
     * The drawGUI method is used to set up all of the components of the GUI, including the pop up
     * that requests for a port and creates an auction house server based on the port inputted. It also
     * displays information such as the auction house ID, the items remaining, and the current auction house
     * balance.
     * @param stage - the stage in which the GUI will be drawn on
     */
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
        Label auctionHouseItemsLabel = new Label("Auction House Items Remaining: ");
        auctionHouseItemsLabel.setFont(new Font("Calibri", 17));
        auctionHouseItemsLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
        itemsRemainingLabel = new Label("");
        itemsRemainingLabel.setFont(new Font("Calibri", 17));
        itemsRemainingLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
        HBox auctionInfoHBox = new HBox(auctionHouseItemsLabel, itemsRemainingLabel);
        HBox.setMargin(auctionInfoHBox, new Insets(10,0,0,10));
        auctionHouseItemsLabel.setAlignment(Pos.CENTER);
        Label auctionHouseBalanceLabel = new Label("Auction House Balance: ");
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
        Scene scene = new Scene(pane, 500, 150);
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
        Label bankServerPort = new Label("Enter Bank Server Port Number: ");
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
        });
        stopAuctionHouse.setOnAction(event -> {
            try {
                boolean tempClose = auctionHouse.stopAuctionHouse();
                if (!tempClose) {
                    auctionHouse = null;
                    System.exit(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
