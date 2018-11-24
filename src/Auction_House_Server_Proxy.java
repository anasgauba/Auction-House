import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Auction_House_Server_Proxy extends Thread{

    private ServerSocket serverSocket;
    private Socket acceptSocket;

    private Auction_House auctionHouse;

    boolean run;

    public Auction_House_Server_Proxy(Auction_House auctionHouse) {
        this.run = true;
        this.auctionHouse = auctionHouse;
        start();
        startAuctionHouseServer();
    }


    public void startAuctionHouseServer() {
        System.out.println("Starting AH thread");
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(6666);
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
        new Thread(() -> {
            try {
                PrintStream serverOutput;
                BufferedReader serverInput;
                serverOutput = new PrintStream(clientSocket.getOutputStream());
                serverInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String temp = serverInput.readLine();
                if (temp.contains("Agent")) {
                    auctionHouse.startAuctionHouseClient(temp);
                }

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
