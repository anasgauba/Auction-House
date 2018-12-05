import Agent.Agent_Start_Display;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;

public class Agent_Main extends Application {

    private Agent_Start_Display display;
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
        Scanner inNames = new Scanner(new File("resources/names"));
        LinkedList<String> names = new LinkedList();
        while (inNames.hasNext()) {
            names.add(inNames.next());
        }

        display = new Agent_Start_Display(primaryStage, names);
    }
}
