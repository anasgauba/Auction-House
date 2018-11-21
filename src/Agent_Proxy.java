import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class Agent_Proxy {


    private boolean run = true;

    //server
    private ServerSocket serverSocket;
    private Socket acceptSocket;
    //public PrintStream serverOutput;
    //public BufferedReader serverInput;

    //client AH
    private Socket auctionHouseClientSocket;
    public BufferedReader auctionHouseClientInput;
    public PrintStream auctionHouseClientOutput;

    //client Bank
    private Socket bankClientSocket;
    public BufferedReader bankClientInput;
    public PrintStream bankClientOutput;

    public Agent_Proxy() {

        startAgentServer();
        startAgentClient();
    }

    public void startAgentServer() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(7677);
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
        System.out.println("starting agent thread");
        new Thread(() -> {
            try {
                PrintStream serverOutput;
                BufferedReader serverInput;
                serverOutput = new PrintStream(clientSocket.getOutputStream());
                serverInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                while (run && clientSocket.isConnected()) {

                    String message = serverInput.readLine();
                    //System.out.println(message);

                    if (message != null){
                        serverOutput.println("agent Server saw your message: " + message);
                    }

                    Thread.sleep(0);
                }

            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }

        }).start();
    }



    public void startAgentClient() {

        //TO AH
        new Thread(() -> {

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {

                auctionHouseClientSocket = new Socket(getServerIP(), 7577);
                auctionHouseClientOutput = new PrintStream(auctionHouseClientSocket.getOutputStream());
                auctionHouseClientOutput.println("testing agent proxy client to AH");

                System.out.println(auctionHouseClientSocket.isConnected());

                while (run && auctionHouseClientSocket.isConnected()) {
                    auctionHouseClientInput = new BufferedReader(new InputStreamReader(auctionHouseClientSocket.getInputStream()));
                    String message = auctionHouseClientInput.readLine();
                    System.out.println(message);


                    Thread.sleep(0);
                }

            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }

        }).start();

        //TO BANK
        new Thread(() -> {

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {

                bankClientSocket = new Socket(getServerIP(), 7777);
                bankClientOutput = new PrintStream(bankClientSocket.getOutputStream());
                bankClientOutput.println("testing agent proxy client to bank");
                System.out.println(bankClientSocket.isConnected());

                while (run && bankClientSocket.isConnected()) {
                    bankClientInput = new BufferedReader(new InputStreamReader(bankClientSocket.getInputStream()));
                    String message = bankClientInput.readLine();
                    System.out.println(message);

                    Thread.sleep(0);
                }

            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }

        }).start();
    }

    private String getServerIP() throws FileNotFoundException {

        Scanner in = new Scanner(new File("resources/Configuration"));

        while (in.hasNextLine()) {
            Scanner scanner = new Scanner(in.nextLine());

            if (scanner.hasNext() && scanner.next().equals("AgentIP")) {
                return scanner.next();
            }
        }

        return null;
    }

}
