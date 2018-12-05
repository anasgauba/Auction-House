import Auction_House.Auction_House_Display;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;


public class Auction_House_Main extends Application {

    private Auction_House_Display display;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() {
        display.stop();
        System.exit(0);
    }

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
