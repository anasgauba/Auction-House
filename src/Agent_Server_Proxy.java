import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Agent_Server_Proxy extends Thread {
    private ServerSocket serverSocket;
    private Socket acceptSocket;
    private Agent agent;
    int portNumber;
    boolean run;

    //add port number to constructor so we can create multiple agents.
    public Agent_Server_Proxy(Agent agent, int portNumber) {
        this.run = true;
        this.agent = agent;
        this.portNumber = portNumber;
        start();
        startAgentServer();
    }


    public void startAgentServer() {
        System.out.println("Starting Agent thread");
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(portNumber);
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (true) {
                try {
                    acceptSocket = serverSocket.accept();
                    startAgentThread(acceptSocket);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void startAgentThread(Socket clientSocket) {
        System.out.println("starting agent thread for incoming connection");
        new Thread(() -> {
            try {
                PrintStream serverOutput;
                BufferedReader serverInput;
                serverOutput = new PrintStream(clientSocket.getOutputStream());
                serverInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                while (run && clientSocket.isConnected()) {

                    String message = serverInput.readLine();
                    System.out.println(message);

                    if (message != null){
                        serverOutput.println("AH Server received your message " + message);
                    }

                    Thread.sleep(0);
                }

            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
