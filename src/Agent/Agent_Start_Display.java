/*Anas Guaba, Clarissa Garcia, and Nathan Schaefer
 * Agent_Start_Display Class
 * <p>
 *
 * @author Nathan Schaefer
 *
 * Agent_Start_Display is a small GUI that used to issue the port number that will start the agent server.
 * It will then create the agent server at that port and open another window that will allow the user
 * to close the bank and exit the program.
 */
package Agent;
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
 * The Agent Start Display class is a small GUI that used to issue the port number that will start the agent server.
 * It contains a private agent, and a list of names that will later be used to create randomly-generated names.
 */
public class Agent_Start_Display {
    LinkedList<String> names;
    private Agent agent;

    /**
     * This stop method, when called, stops the agent thread and closes its account
     * before making the agent null, effectively erasing the agent.
     */
    public void stop() {
        try {
            agent.closeAccount();
            agent = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * The agentDisplay constructor, when called, is passed in the default JavaFX stage object and the names
     * the agent will later use to create a randomly-generated name . This method
     * will then call the drawGUI method which will set up the rest of the GUI.
     * @param stage - the stage in which the GUI will be displayed on.
     * @param names - a list of names the agent will later use to create a randomly-generated name .
     */
    public Agent_Start_Display(Stage stage, LinkedList<String> names) {
        this.names = names;
        drawGUI(stage);
    }
    /**
     * The drawGUI method is used in order to set up the user's ability to create a agent port. Upon entering a port,
     * the GUI will fire a command to create a agent with the same port and set up the client and server of the
     * agent using the requested port.
     * @param stage- the stage in which the GUI will be displayed on.
     */
    public void drawGUI(Stage stage) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        Label auctionPortNumberLabel = new Label("Enter Agent Server Port Number:");
        auctionPortNumberLabel.setFont(new Font("Calibri", 17));
        auctionPortNumberLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
        TextField portTextField = new TextField("8900");
        HBox.setMargin(portTextField, new Insets(0,0,0,20));
        portTextField.setAlignment(Pos.CENTER);
        HBox portHBox = new HBox(auctionPortNumberLabel, portTextField);
        Label bankServerPort = new Label("Enter Bank Server Port Number:");
        bankServerPort.setFont(new Font("Calibri", 17));
        bankServerPort.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
        TextField bankPortTextField = new TextField("7277");
        HBox.setMargin(bankPortTextField, new Insets(0,0,0,27));
        bankPortTextField.setAlignment(Pos.CENTER);
        HBox bankPortHBox = new HBox(bankServerPort, bankPortTextField);
        Button startButton = new Button("Start Server");
        startButton.setPrefWidth(250);
        startButton.setStyle("-fx-background-color: beige; -fx-font-weight: " +
                "bold; -fx-font: 14 arial");
        HBox startButtonHbox = new HBox(startButton);
        startButtonHbox.setAlignment(Pos.CENTER);
        VBox dialogVbox = new VBox(15);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        dialogVbox.getChildren().addAll(bankPortHBox, portHBox, startButtonHbox);
        Scene dialogScene = new Scene(dialogVbox, 450, 150);
        dialogVbox.setBackground(new Background(new BackgroundFill(Color.rgb(54, 69, 79), CornerRadii.EMPTY, Insets.EMPTY)));
        dialog.setScene(dialogScene);
        dialog.show();
        startButton.setOnAction(e -> {
            int portNumber = Integer.parseInt(portTextField.getText());
            int bankPortNumber = Integer.parseInt(bankPortTextField.getText());
            dialog.hide();
            agent = new Agent(portNumber, bankPortNumber, names);
        });
    }
}
