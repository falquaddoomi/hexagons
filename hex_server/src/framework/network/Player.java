package framework.network;

import com.esotericsoftware.kryonet.Connection;

/**
 * created by Faisal on 3/16/14 6:01 PM
 */
public class Player {
    protected final long id;
    protected final Connection conn;

    public Player(long id, Connection conn) {
        this.id = id;
        this.conn = conn;
    }
}
