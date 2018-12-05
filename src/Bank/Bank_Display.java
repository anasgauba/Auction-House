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

public class Bank_Display {

    protected Label numAccounts;
    private Bank bank;

    public Bank_Display(Stage stage) {

        drawGUI(stage);
    }


    public void drawGUI(Stage stage) {


        Label bankAccountsIDLabel = new Label("Number of Accounts:");
        bankAccountsIDLabel.setFont(new Font("Calibri", 17));
        bankAccountsIDLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white");

        numAccounts = new Label("");
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
        pane.setBackground(new Background(new BackgroundFill(Color.rgb(54, 69, 79), CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(pane, 400, 400);
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
        Scene dialogScene = new Scene(dialogVbox, 450, 100);
        dialogVbox.setBackground(new Background(new BackgroundFill(Color.rgb(54, 69, 79), CornerRadii.EMPTY, Insets.EMPTY)));
        dialog.setScene(dialogScene);
        dialog.show();




        startButton.setOnAction(e -> {
            int portNumber = Integer.parseInt(portTextField.getText());
            dialog.hide();
            bank = new Bank(this, portNumber);
            System.out.println("portn " + portNumber);

        });
    }

    public int getPortNumber() {


        return 0;

    }
}
