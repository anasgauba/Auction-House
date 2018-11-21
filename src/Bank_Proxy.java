import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class Bank_Proxy {


    private boolean run = true;

    //server
    private ServerSocket serverSocket;
    private Socket acceptSocket;

    //client
    private Socket clientSocket;
    public BufferedReader clientInput;
    public PrintStream clientOutput;

    public Bank_Proxy() {

        startBankServer();
        startBankClient();
    }


    public void startBankServer() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(7777);
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (true) {
                try {
                    acceptSocket = serverSocket.accept();
                    startBankThread(acceptSocket);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void startBankThread(Socket clientSocket) {
        System.out.println("starting bank thread");
        new Thread(() -> {

            try {
                PrintStream serverOutput;
                BufferedReader serverInput;
                serverOutput = new PrintStream(clientSocket.getOutputStream());
                serverInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                while (run && clientSocket.isConnected()) {

                    String message = serverInput.readLine();
                    //System.out.println(message);

                    if (message != null) {
                        serverOutput.println("Bank received your message " + message);
                    }

                    Thread.sleep(0);
                }

            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }

        }).start();
    }



    public void startBankClient() {

        //TO AH
        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                clientSocket = new Socket(getAuctionHouseIP(), 7577);
                clientOutput = new PrintStream(clientSocket.getOutputStream());
                clientOutput.println("testing bank to ah proxy client");

                while (run && clientSocket.isConnected()) {
                    clientInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String message = clientInput.readLine();
                    System.out.println(message);

                    Thread.sleep(0);
                }


            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }

        }).start();
    }

    private String getAuctionHouseIP() throws FileNotFoundException {

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
