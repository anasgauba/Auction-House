import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client_Proxy extends Thread {

    private Socket clientSocket;
    public BufferedReader clientInput;
    public PrintStream clientOutput;

    String clientType;
    int portNumber;
    boolean run;

    public Client_Proxy(int key, String clientType, int portNumber) {
        this.clientType = clientType;
        this.portNumber = portNumber;
        this.run = true;
        start();
    }

    public void run() {
        try {
            clientSocket = new Socket(getServerIP(), portNumber);
            clientOutput = new PrintStream(clientSocket.getOutputStream());

            if (clientType.contains("Bank")) {
                clientOutput.println(clientType);
            }

            else if (clientType.contains("AuctionHouse")) {
                sleep(2000);
                clientOutput.println(clientType);
            }

            else if (clientType.contains("Agent")) {
                sleep(3000);
                clientOutput.println(clientType + " " + clientSocket.getPort()); //I dont remember why this is different from the rest. works though.. look into this later
            }

            while (run && clientSocket.isConnected()) {
                clientInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String message = clientInput.readLine();
                System.out.println("message received: " + message);
                Thread.sleep(0);
            }

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
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
