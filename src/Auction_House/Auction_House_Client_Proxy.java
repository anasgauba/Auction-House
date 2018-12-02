package Auction_House;

import Agent.Agent;
import Misc.Command;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class Auction_House_Client_Proxy extends Thread {



    private Socket clientSocket;
    public ObjectInputStream clientInput;
    public ObjectOutputStream clientOutput;

    Agent agent;
    String clientType;
    int portNumber;
    int key;
    boolean run;

    public Auction_House_Client_Proxy(int key, String clientType, int portNumber) {
        this.key = key;
        this.clientType = clientType;
        this.portNumber = portNumber;
        this.run = true;
        start();
    }

    public Auction_House_Client_Proxy(Agent agent, int key, String clientType, int portNumber) {
        this.agent = agent;
        this.key = key;
        this.clientType = clientType;
        this.portNumber = portNumber;
        this.run = true;
        start();
        System.out.println("agent client proxy key " + key);
    }


    public void run() {

        try {
            System.out.println("here " + portNumber);
            clientSocket = new Socket(getServerIP(), portNumber);
            clientOutput = new ObjectOutputStream(clientSocket.getOutputStream());
            System.out.println("in ah client why " + clientOutput + clientType);




//            if (agent != null) {
//                while (clientOutput == null);
//                synchronized (agent) {
//                    agent.notifyAll();
//                }
//            }



            clientOutput.writeObject(clientType + " " + clientSocket.getPort() + " " + key);

            synchronized (agent) {
                agent.notifyAll();
            }

            System.out.println("wtf");
            System.out.println((clientType + " " + clientSocket.getPort() + " " + key).getClass());
            while (run && clientSocket.isConnected()) {

                if (clientInput == null) {
                    clientInput = new ObjectInputStream(clientSocket.getInputStream());
                }

                Object[] message = (Object[]) clientInput.readObject();
                Command command = (Command) message[0];

                switch (command) {

                    case SetListItems:
                        agent.createItemList((LinkedList<Item>) message[1]);
                        break;

                    case SendBid:
                        agent.printDetermination((Command)message[1], (Item) message[2]);
                        break;


                }

            }


    } catch (IOException | ClassNotFoundException e) {
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
