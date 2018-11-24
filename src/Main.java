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


        Bank bank = new Bank();
        Auction_House auction_house = new Auction_House(1, 6666, nouns, adjectives);
        Agent agent = new Agent(8900);

        Agent_Display agentdisp = new Agent_Display(agent, auction_house, bank);
        agentdisp.drawGUI(stage);

        System.out.println();
    }
}
