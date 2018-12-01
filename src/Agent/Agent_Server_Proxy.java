package Agent;

import Misc.Command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
        System.out.println("Starting Agent.Agent thread");
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
                ObjectOutputStream serverOutput;
                ObjectInputStream serverInput;
                serverOutput = new ObjectOutputStream(clientSocket.getOutputStream());
                serverInput = new ObjectInputStream(clientSocket.getInputStream());
                String temp = (String) serverInput.readObject();
                System.out.println("received: " + temp);

                while (run && clientSocket.isConnected()) {

                    Object[] message = (Object[]) serverInput.readObject();
                    Command command = (Command) message[0];

                    //this switch handles incoming messages.  NOT from client, but from what ever
                    //button the user on the gui will press or from the agent thread
                    //Clients switch will be for INCOMING messages from Server or the ser
                    switch (command) {

                        case RefreshTimes:
                            agent.refreshItems();
                            System.out.println("refreshing");
                            break;

                        case BidOvertaken:
                            agent.printDetermination((Command) message[0]);
                            break;

                        case WinMessage:
                            agent.printDetermination((Command) message[0]);
                    }

                    serverOutput.reset();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
