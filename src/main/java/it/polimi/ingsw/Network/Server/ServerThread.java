package it.polimi.ingsw.Network.Server;

import it.polimi.ingsw.Model.ObserverModel;
import it.polimi.ingsw.Network.Message.MessageFromClient.MessageFromClient;
import it.polimi.ingsw.Network.Message.MessageFromServer.*;
import it.polimi.ingsw.View.VirtualView;


import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ServerThread extends Thread implements Runnable {

    private final VirtualView view = new VirtualView(this);
    final Socket socket;
    final Server server;
    private final VisitorServer visitor = new VisitorMethodsServer(view, this);
    private boolean maxPlayerNumberSet;
    private boolean nicknameSet = false;
    int numPlayers;
    final int numOnline = 0;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private volatile boolean keepAlive = true;
    private final boolean ready = false;

    public boolean isNicknameSet() {
        return nicknameSet;
    }

    public void setNicknameSet(boolean nicknameSet) {
        this.nicknameSet = nicknameSet;
    }

    public void setMaxPlayerNumberSet(boolean maxPlayerNumberSet) {
        this.maxPlayerNumberSet = maxPlayerNumberSet;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public VirtualView getView() {
        return view;
    }

    /**
     * This class is a thread create by the server. Each client has its own Thread.
     * The thread is the real gate between the Server and the Client.
     * @param socket socket
     * @param server server
     * @param numPlayers numPlayers
     * @param maxPlayerNumberSet maxPlayerNumberSet
     */
    public ServerThread(Socket socket, Server server, int numPlayers, boolean maxPlayerNumberSet) {
        this.socket = socket;
        this.server = server;
        this.numPlayers = numPlayers;
        this.maxPlayerNumberSet = maxPlayerNumberSet;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public boolean isMaxPlayerNumberSet() {
        return maxPlayerNumberSet;
    }

    public void setMaxPlrMsg(boolean maxPlrSet) {
        this.maxPlayerNumberSet = maxPlrSet;
    }

    public Server getServer() {
        return server;
    }

    public int getNumOnline() {
        return numOnline;
    }

    /**
     * out and in are the channels used to communicate with the clients
     * if this is the first client, request the number of players
     * if the number of connected clients is greater than the number
     *      of maximum players allowed, then a message is sent to that client
     *      notifying of this event
     * otherwise view and controller are set
     * the attribute KeepAlive is used to manage the thread.
     *      If KeepAlive = true, then
     *          the thread runs and continues to accept new packets
     *      If KeepAlive = false, then
     *          the thread stops
     */
    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.getServerThreads().add(this);
        sendToClient(new ConnectionResponse());
        //numOnline = server.getClients().size();
        if (server.getServerThreads().size() == 1) {
            sendToClient(new PlayerNumberRequest());
        } else {
            if (isMaxPlayerNumberSet() && server.getServerThreads().size() == numPlayers) {
                for (int i = 0; i < server.getServerThreads().size(); i++) {
                    if(!server.getServerThreads().get(i).isNicknameSet()) {
                        server.getServerThreads().get(i).sendToClient(new NicknameRequest());
                    }
                }
            }
            if (server.getServerThreads().size() > numPlayers) {
                server.getServerThreads().remove(server.getServerThreads().size() - 1);
                sendToClient(new MaxPlayerReachedUpdate());
                return;
            }
        }
        view.AddObserver(server.getGameController());
        server.getGameController().getGame().AddObserver(view);
        while (keepAlive) {
            try {
                MessageFromClient message = ((MessageFromClient) in.readObject());
                System.out.println("Message received");
                Accept(message);
            } catch (IOException e) {
                return;
            } catch (ClassNotFoundException | InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * This method sends the input to the client
     * @param messageFromServer message
     */
    public void sendToClient(MessageFromServer messageFromServer)  {
        try {
            out.reset();
            out.writeObject(messageFromServer);
            out.flush();
        } catch (IOException e) {
            System.out.println();
            System.out.println("Connection with the client lost");
        }
    }

    /**
     * Visitor analyzes the incoming messages
     * @param msg message
     */
    public synchronized void Accept(MessageFromClient msg) throws IOException, InterruptedException {
        msg.accept(visitor);
    }


    /**
     * Observer is removed
     * @param view view
     */
    public void RemoveObserver(ObserverModel view) {
        server.getGameController().getGame().RemoveObserver(view);
    }

}