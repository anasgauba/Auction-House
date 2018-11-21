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


        //Auction_House_Proxy agent_proxy = new Auction_House_Proxy();
        //Agent_Proxy agent_proxy = new Agent_Proxy();
        //Bank_Proxy bank_proxy = new Bank_Proxy();
        Bank bank = new Bank();
        Auction_House auction_house = new Auction_House("1", nouns, adjectives);
        Agent agent2 = new Agent();

        //Agent agent3 = new Agent();
        //Agent agent4 = new Agent();
        //Auction_House auction = new Auction_House("2", nouns, adjectives);



        Agent_Display agent = new Agent_Display(agent2, auction_house, bank);
        //Agent_Display agent5 = new Agent_Display(agent3);
        //Agent_Display agent6 = new Agent_Display(agent4);
        agent.drawGUI(stage);


        System.out.println();
        //agent5.drawGUI(stage);
        //agent6.drawGUI(stage);


    }
}
