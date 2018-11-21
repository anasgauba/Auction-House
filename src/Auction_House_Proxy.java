import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class Auction_House_Proxy {


    private boolean run = true;

    //server
    private ServerSocket serverSocket;
    private Socket acceptSocket;
    //public PrintStream serverOutput;
    //public BufferedReader serverInput;

    //client agent
    private Socket agentClientSocket;
    public BufferedReader agentClientInput;
    public PrintStream agentClientOutput;

    //client Bank
    private Socket bankClientSocket;
    public BufferedReader bankClientInput;
    public PrintStream bankClientOutput;


    public Auction_House_Proxy() {

        startAuctionHouseServer();
        startAuctionHouseClient();
    }

    public void startAuctionHouseServer() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(7577);
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (true) {
                try {
                    acceptSocket = serverSocket.accept();
                    startAuctionHouseThread(acceptSocket);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void startAuctionHouseThread(Socket clientSocket) {
        System.out.println("starting auction house thread");
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
                        serverOutput.println("AH Server received your message " + message);
                    }

                    Thread.sleep(0);
                }

            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }

        }).start();

    }

    public void startAuctionHouseClient() {

        //to AGENT
        new Thread(() -> {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {

                agentClientSocket = new Socket(getServerIP(), 7677);
                agentClientOutput = new PrintStream(agentClientSocket.getOutputStream());
                agentClientOutput.println("testing ah proxy to agent client");



                while (run && agentClientSocket.isConnected()) {
                    agentClientInput = new BufferedReader(new InputStreamReader(agentClientSocket.getInputStream()));
                    String message = agentClientInput.readLine();
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
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {

                bankClientSocket = new Socket(getServerIP(), 7777);
                bankClientOutput = new PrintStream(bankClientSocket.getOutputStream());
                bankClientOutput.println("testing ah proxy to bank client");

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

            if (scanner.hasNext() && scanner.next().equals("AuctionHouseServerIP")) {
                return scanner.next();
            }
        }
        return null;
    }
}
