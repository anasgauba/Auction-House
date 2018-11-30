import Agent.Agent;
import Auction_House.Auction_House;
import Bank.Bank;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;


public class Main extends Application {

    @Override
    public void stop() {
        System.exit(0);
    }

    public void start(Stage stage) throws IOException {

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

        Scanner inNames = new Scanner(new File("resources/names"));
        LinkedList<String> names = new LinkedList();
        while (inNames.hasNext()) {
            names.add(inNames.next());
        }


        Bank bank = new Bank();
        Auction_House auction_house = new Auction_House(6666, nouns, adjectives);
        Auction_House auction_house2 = new Auction_House(6667, nouns, adjectives);
        Auction_House auction_house3 = new Auction_House(6668, nouns, adjectives);
        Agent agent = new Agent(8900, names);

        //Agent.Agent_Display agentdisp = new Agent.Agent_Display();
        //agentdisp.drawGUI(stage);

        System.out.println();
    }
}
