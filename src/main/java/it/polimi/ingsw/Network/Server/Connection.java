

package it.polimi.ingsw.Network.Server;

/**
 *
 */
public class Connection {

    /**
     * This class is used by ServerBeatReceiver to keep track of the current connections.
     * New attributes of the connection can be added here to improve its management.
     *
     * @param serverThread serverThread
     */
    public Connection(ServerThread serverThread, long lastBeatInstant) {
        this.serverThread = serverThread;
        this.lastBeatInstant = lastBeatInstant;
    }

    public final ServerThread serverThread;
    public long lastBeatInstant;
}