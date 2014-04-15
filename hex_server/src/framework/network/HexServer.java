package framework.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import domain.board.HexRegion;
import domain.effects.Effect;
import exchanges.MoveRequest;
import framework.World;
import framework.faces.entities.EntityFace;
import processing.core.PVector;
import support.Registry;

import java.io.IOException;
import java.util.HashMap;

/**
 * created by Faisal on 3/5/14 4:42 PM
 */
public class HexServer extends Listener implements Runnable {
    private Server server;
    final int TCP_PORT = 54555, UDP_PORT = 54777;
    private final World world;

    // mapping of player IDs to players
    private final HashMap<Long, Player> players = new HashMap<Long, Player>();
    private long newPlayerID = 1;

    public HexServer(World world) throws IOException {
        this.world = world;

        server = new Server(8192, 8192);
        server.bind(TCP_PORT, UDP_PORT);

        // register classes first
        Registry.register(server.getKryo());

        server.addListener(this);

        System.out.printf("Awaiting connections on port %d (TCP), %d (UDP)...\n", TCP_PORT, UDP_PORT);
        server.start();
    }

    public void shutdown() {
        // shut down the server here
    }

    @Override
    public void received(Connection connection, Object object) {
        // dispatch based on detected object type
        if (object instanceof MoveRequest) {
            // activate the game map someplace, i guess?
            MoveRequest click = (MoveRequest)object;

            // send them an effect to signify that this worked, i guess
            Effect fx = new Effect();
            fx.max_life = 100;
            fx.pos = click.to;
            connection.sendUDP(fx);

            // check if there's anything to move; if there is, move it
            for (EntityFace e : world.entities.values()) {
                if (e.get().coord.equals(click.from)) {
                    // move that entity and dirty it
                    e.get().coord = click.to;
                    world.ents_dirty.entities.put(e.get().id, e.get());
                }
            }
        }
    }

    @Override
    public void connected(Connection connection) {
        super.connected(connection);
        System.out.println("Connection received from " + connection);

        // save this player object
        long playerID = getNewPlayerID();
        players.put(playerID, new Player(playerID, connection));

        // send user the map + all entities
        synchronized (world) {
            System.out.println("Entered synchronized block...");
            HexRegion region = world.tiles.getMapAsRegion();
            System.out.println("Acquired region, sending!");
            connection.sendTCP(region);
            System.out.printf("Sent %d tiles to %s\n", region.region.size(), connection);

            connection.sendTCP(world.getEntityList());
            System.out.printf("Sent %d entity notifications to %s\n", world.getEntityList().entities.size(), connection);
        }

        System.out.println("Connection complete for " + connection);
    }

    @Override
    public void run() {
        // updates the world
        synchronized (world) {
            world.update();

            // send out the world's map_dirty region to all clients
            if (world.map_dirty.size() > 0) {
                server.sendToAllTCP(world.map_dirty);
                world.map_dirty.clear();
            }

            // also send out any queued entity creation events
            if (world.ents_dirty.entities.size() > 0) {
                System.out.printf("Sending out %d dirty entities\n", world.ents_dirty.entities.size());

                server.sendToAllTCP(world.ents_dirty);
                world.ents_dirty.entities.clear();
            }

            // finally send out any ent death notifications
            if (world.ents_dead_dirty.ids.size() > 0) {
                System.out.printf("Sending out %d dead entity notifications\n", world.ents_dead_dirty.ids.size());

                server.sendToAllTCP(world.ents_dead_dirty);
                world.ents_dead_dirty.ids.clear();
            }
        }
    }

    public long getNewPlayerID() {
        newPlayerID = (newPlayerID + 1) % Long.MAX_VALUE;
        return newPlayerID;
    }
}
