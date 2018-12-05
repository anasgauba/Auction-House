/*Anas Guaba, Clarissa Garcia, and Nathan Schaefer
 * Agent_Main Application
 * <p>
 *
 * @author Nathan Schaefer
 *
 * The Agent_Main Application is used to start all the components of the Agent
 */

import Agent.Agent_Start_Display;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;

/**
 *  The Agent_Main Application is used to start all the components of the agent. It extends Application
 *  so that it may run all of the components necessary that have to do with auction house. It contains its
 *  own display that can be manipulated by the agent Application.
 */
public class Agent_Main extends Application {
    private Agent_Start_Display display;

    /**
     * The main method in this class is used to launch the application, which will
     * create the GUI and trigger the creation of the bank object, server, and client.
     * @param args - the required paramater to launch a main method
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The overriden stop message is used in order to exit the Application. When called,
     * it simply stops the current agent display and uses System.exit to cleanly end the program
     */
    @Override
    public void stop() {
        display.stop();
        System.exit(0);
    }

    /**
     * The start method, a required method in application, is used to
     * start the agent display, which will create the agent server, client,
     * and auction house object. It also reads in the list of names
     * that will be later used to create agent names and passes the list to the agent
     * display that it creates.
     * @param primaryStage - the stage in which the GUI will be drawn on
     * @throws Exception
     */
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
