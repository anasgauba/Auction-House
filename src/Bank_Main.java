/*Anas Guaba, Clarissa Garcia, and Nathan Schaefer
 * Bank_Main Application
 * <p>
 *
 * @author Nathan Schaefer
 *
 * The Bank_Main Application is used to start all the components of the Agent
 */
import Bank.Bank_Display;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *  The Bank_Main Application is used to start all the components of the Agent. It extends Application
 *  so that it may run all of the components necessary that have to do with bank.
 */
public class Bank_Main extends Application {

    /**
     * The main method in this class is used to laucn the application, which will
     * create the GUI and trigger the creation of the bank object, server, and client.
     * @param args - the required parmater to launch a main method
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The overriden stop message is used in order to exit the Application. When called,
     * it simply uses System.exit to cleanly end the program
     */
    @Override
    public void stop() {
        System.exit(0);
    }

    /**
     * The start method, a required method in application, is used to
     * start the bank display, which will create the bank server, client,
     * and bank object.
     * @param primaryStage - the stage in which the GUI will be drawn on
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Bank_Display display = new Bank_Display(primaryStage);
    }
}
