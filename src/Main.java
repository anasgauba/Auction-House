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


        Agent_Proxy agent_proxy = new Agent_Proxy();
        //Auction_House_Proxy auction_house_proxy = new Auction_House_Proxy();
        Bank_Proxy bank_proxy = new Bank_Proxy();

        //Agent agent2 = new Agent();
        Auction_House auction_house_server = new Auction_House("2", nouns, adjectives);

        Agent_Display agent = new Agent_Display();
        agent.drawGUI(stage);


    }
}
