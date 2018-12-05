import Bank.Bank_Display;
import javafx.application.Application;
import javafx.stage.Stage;

public class Bank_Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() {
        System.exit(0);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Bank_Display display = new Bank_Display(primaryStage);
    }
}
