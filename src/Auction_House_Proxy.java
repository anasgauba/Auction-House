import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Auction_House_Proxy extends Thread {


    private boolean run;

    private ServerSocket serverSocket;
    private Socket acceptSocket;
    public PrintStream output;
    public BufferedReader input;


    public Auction_House_Proxy() {
        this.run = true;
        start();
    }





    @Override
    public void run() {

        System.out.println("AH ALIVE");


        try {
            if (serverSocket == null) {
                serverSocket = new ServerSocket(7677);
                acceptSocket = serverSocket.accept();
                output = new PrintStream(acceptSocket.getOutputStream());
                input = new BufferedReader(new InputStreamReader(acceptSocket.getInputStream()));
            }

            while (run && acceptSocket.isConnected()) {


                String message = input.readLine();
                System.out.println(message);
                if (message != null) {
                    //output.println("test successful!");
                }


                //System.out.println("AH is alive: " + auctionHouseID);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
