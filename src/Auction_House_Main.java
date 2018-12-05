/*Anas Guaba, Clarissa Garcia, and Nathan Schaefer
 * Auction_House_Main Application
 * <p>
 *
 * @author Nathan Schaefer
 *
 * The Auction_House_Main Application is used to start all the components of the Auction House
 */
import Auction_House.Auction_House_Display;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;

/**
 *  The Auction_House_Main Application is used to start all the components of the Auction House. It extends Application
 *  so that it may run all of the components necessary that have to do with auction house. It contains its
 *  own display that can be manipulated by the auction house Application.
 */
public class Auction_House_Main extends Application {
    private Auction_House_Display display;

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
     * it simply stops the current auction house display and uses System.exit to cleanly end the program
     */
    @Override
    public void stop() {
        display.stop();
        System.exit(0);
    }

    /**
     * The start method, a required method in application, is used to
     * start the auction display, which will create the auction house server, client,
     * and auction house object. It also reads in the list of adjectives and nouns
     * that will be later used to create items and passes the list to the auction house
     * display that it creates.
     * @param primaryStage - the stage in which the GUI will be drawn on
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Scanner inNouns = new Scanner(new File("resources/nouns"));
        LinkedList<String> nouns = new LinkedList();
        while (inNouns.hasNext()) {
            nouns.add(inNouns.next());
        }
        Scanner inAdjectives = new Scanner(new File("resources/adjectives"));
        LinkedList<String> adjectives = new LinkedList();
        while (inAdjectives.hasNext()) {
            adjectives.add(inAdjectives.next());
        }
        display = new Auction_House_Display(primaryStage, nouns, adjectives);
    }
}
