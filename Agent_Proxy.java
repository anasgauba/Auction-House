import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class Agent_Proxy {

    public void startAgentClient() {

        //TO AH
        new Thread(() -> {

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {

                auctionHouseClientSocket = new Socket(getServerIP(), 6666);
                auctionHouseClientOutput = new PrintStream(auctionHouseClientSocket.getOutputStream());
                auctionHouseClientOutput.println("12340 6666");

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
                //bankClientOutput.println("testing agent proxy client to bank");
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
