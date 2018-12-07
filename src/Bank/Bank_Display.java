/*Anas Guaba, Clarissa Garcia, and Nathan Schaefer
 * Bank_Display Class
 * <p>
 *
 * @author Nathan Schaefer, Anas Gauba
 *
 * Bank Display is a small GUI that used to issue the port number that will start the bank server.
 * It will then create the bank server at that port and open another window that will allow the user
 * to close the bank and exit the program.
 */
package Bank;

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

/**
 * The Bank Display class is a small GUI that used to issue the port number that will start the bank server.
 * It contains a private bank field,
 */
public class Bank_Display {
    protected Label numAccounts;
    private Bank bank;

    /**
     * The bankDisplay constructor, when called, is passed in the default JavaFX stage object. This object
     * will then call the drawGUI method which will set up the rest of the GUI.
     * @param stage - the stage in which the GUI will be displayed on.
     */
    public Bank_Display(Stage stage) {
        drawGUI(stage);
    }

    /**
     * The drawGUI method is used in order to set up the user's ability to create a bank port. Upon entering a port,
     * the GUI will fire a command to create a bank with the same port and set up the client and server of the
     * bank using the requested port.
     * @param stage- the stage in which the GUI will be displayed on.
     */
    public void drawGUI(Stage stage) {
        Label bankAccountsIDLabel = new Label("Number of Accounts: ");
        bankAccountsIDLabel.setFont(new Font("Calibri", 17));
        bankAccountsIDLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
        numAccounts = new Label("0");
        numAccounts.setFont(new Font("Calibri", 17));
        numAccounts.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
        numAccounts.setAlignment(Pos.CENTER);
        HBox bankAccountsHBox = new HBox(bankAccountsIDLabel, numAccounts);
        HBox.setMargin(bankAccountsHBox, new Insets(10,0,0,10));
        Button stopBank = new Button("Close Bank");
        stopBank.setPrefWidth(200);
        stopBank.setStyle("-fx-background-color: beige; -fx-font-weight: " +
                "bold; -fx-font: 14 arial");
        stopBank.setOnAction(event -> {
            bank = null;
            System.exit(0);

        });
        VBox auctionHouseVBox = new VBox(bankAccountsHBox, stopBank);
        Pane pane = new Pane(auctionHouseVBox);
        pane.setBackground(new Background(new BackgroundFill(Color.rgb(54, 69, 79),
                CornerRadii.EMPTY, Insets.EMPTY)));
        Scene scene = new Scene(pane, 400, 100);
        stage.setScene(scene);
        stage.setTitle("Bank Server");
        stage.show();
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        Label bankPortNumberLabel = new Label("Enter Bank Port Number:");
        bankPortNumberLabel.setFont(new Font("Calibri", 17));
        bankPortNumberLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
        TextField portTextField = new TextField("7277");
        HBox.setMargin(portTextField, new Insets(0,0,0,20));
        portTextField.setAlignment(Pos.CENTER);
        HBox portHBox = new HBox(bankPortNumberLabel, portTextField);
        portHBox.setAlignment(Pos.CENTER);
        Button startButton = new Button("Start Server");
        startButton.setPrefWidth(200);
        startButton.setStyle("-fx-background-color: beige; -fx-font-weight: " +
                "bold; -fx-font: 14 arial");
        HBox buttonHBox = new HBox(startButton);
        buttonHBox.setAlignment(Pos.CENTER);
        VBox dialogVbox = new VBox(15);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        dialogVbox.getChildren().addAll(portHBox, buttonHBox);
        Scene dialogScene = new Scene(dialogVbox, 400, 100);
        dialogVbox.setBackground(new Background(new BackgroundFill(Color.rgb(54, 69, 79),
                CornerRadii.EMPTY, Insets.EMPTY)));
        dialog.setScene(dialogScene);
        dialog.show();
        startButton.setOnAction(e -> {
            int portNumber = Integer.parseInt(portTextField.getText());
            dialog.hide();
            bank = new Bank(this, portNumber);
        });
    }
}
