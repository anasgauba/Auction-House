import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Agent_Proxy extends Thread {

    private Socket clientSocket;
    private BufferedReader input;
    private PrintStream output;

    private boolean run;

    public Agent_Proxy() {

        this.run = true;
        start();
    }

    @Override
    public void run() {

        System.out.println("AGENT ALIVE");

        try {
            clientSocket = new Socket("localhost" , 7677);
            output = new PrintStream(clientSocket.getOutputStream());
            output.println("Hello server!");

            while (run && clientSocket.isConnected()) {
                input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String message = input.readLine();
                System.out.println(message);
                Thread.sleep(1000);
                //output.println("second message!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
