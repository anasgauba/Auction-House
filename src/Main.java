import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;


public class Main extends Application {

    public void start(Stage stage) throws FileNotFoundException {

        System.out.println("Hello World!");

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

        //testing purposes
//        Auction_House_Server auctionHouse = new Auction_House_Server("1", nouns, adjectives);
        //Auction_House_Server auctionHouse2 = new Auction_House_Server("2", nouns, adjectives);

        Agent_Display agent = new Agent_Display();
        agent.drawGUI(stage);


    }
}
