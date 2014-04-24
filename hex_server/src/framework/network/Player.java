package framework.network;

import com.esotericsoftware.kryonet.Connection;

/**
 * created by Faisal on 3/16/14 6:01 PM
 */
public class Player {
    protected final long id;
    protected Connection conn;
    protected PlayerStatus status;

    public Player(long id, Connection conn) {
        this.id = id;
        this.conn = conn;
        this.status = PlayerStatus.CONNECTING;
    }

    public long getID() {
        return id;
    }

    public Connection getConn() {
        return conn;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    public void replaceConn(Connection conn) {
        this.conn = conn;
    }

    public void releaseConn() {
        this.conn = null;
    }

    public enum PlayerStatus { CONNECTING, IN_GAME, DISCONNECTED }
}
