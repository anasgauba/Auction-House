import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Bank_Server_Proxy {

    private ServerSocket serverSocket;
    private Socket acceptSocket;

    private Bank bank;

    boolean auctionHouseClient;
    boolean run;

    public Bank_Server_Proxy(Bank bank) {
        this.auctionHouseClient = true;
        this.run = true;
        this.bank = bank;
        startBankServer();
    }


    public void startBankServer() {
        System.out.println("starting bank thread");
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(7277);
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
        new Thread(() -> {
            try {
                PrintStream serverOutput;
                BufferedReader serverInput;
                serverOutput = new PrintStream(clientSocket.getOutputStream());
                serverInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String temp = serverInput.readLine();
                if (temp.contains("AuctionHouse")) {
                    bank.startBankClient(temp);
                }


                while (run && clientSocket.isConnected()) {

                    String message = serverInput.readLine();

                    if (message != null){
                        serverOutput.println("bank Server received your message " + message);
                    }

                    Thread.sleep(0);
                }

            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
