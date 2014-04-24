package framework.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import domain.board.HexCoord;
import domain.board.HexRegion;
import domain.effects.Effect;
import domain.entities.EntityList;
import domain.entities.Pylon;
import exchanges.*;
import framework.World;
import framework.faces.entities.EntityFace;
import notifies.EntDeathNotifyList;
import support.Registry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * created by Faisal on 3/5/14 4:42 PM
 */
public class HexServer extends Listener implements Runnable {
    private Server server;
    final int TCP_PORT = 54555, UDP_PORT = 54777;
    private final World world;

    // queue for storing players on whom we're waiting for login IDs
    private final ArrayList<Connection> login_queue = new ArrayList<Connection>();
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
    public void received(Connection connection, Object o) {
        // dispatch based on detected object type
        if (o instanceof MoveRequest) {
            // activate the game map someplace, i guess?
            MoveRequest click = (MoveRequest)o;

            // send them an effect to signify that this worked, i guess
            Effect fx = new Effect();
            fx.max_life = 100;
            fx.pos = click.to;
            connection.sendUDP(fx);

            // check if there's anything to move; if there is, move it
            for (EntityFace e : world.entity_mgr.getEntities().values()) {
                if (e.get().coord.equals(click.from)) {
                    // set the entity's destination
                    // it'll be auto-flushed as it moves
                    e.get().dest = click.to;
                }
            }
        }
        else if (o instanceof PlayerLoginResponse) {
            // received from the client, contains what they think their ID is
            PlayerLoginResponse response = (PlayerLoginResponse)o;
            // what we'll eventually be sending to the client to clarify their login attempt
            PlayerWelcomeResponse welcomeResponse = new PlayerWelcomeResponse();

            if (players.containsKey(response.playerID) && players.get(response.playerID).getStatus() == Player.PlayerStatus.DISCONNECTED) {
                // set this player's connection object to this object
                // also flag the player as playing
                Player thisPlayer = players.get(response.playerID);
                thisPlayer.replaceConn(connection);
                thisPlayer.setStatus(Player.PlayerStatus.IN_GAME);

                System.out.format("Welcomed pre-known user w/ID %d\n", response.playerID);

                // and tell the player that they connected
                welcomeResponse.playerID = thisPlayer.getID();
                connection.sendTCP(welcomeResponse);
            }
            else {
                // just set them up as a new player
                Player player = createPlayer(connection);
                player.setStatus(Player.PlayerStatus.IN_GAME);

                System.out.format("Created new user w/ID %d\n", player.getID());

                synchronized (world) {
                    // allow the world to set up the player
                    Pylon ownedPylon = world.setupPlayer(player);
                }

                // and tell the player that they connected
                welcomeResponse.playerID = player.getID();
                connection.sendTCP(welcomeResponse);
            }

            // either way, we flush the state to the user
            sendFullState(connection);
        }
    }

    @Override
    public void connected(Connection connection) {
        super.connected(connection);
        System.out.println("Connection received from " + connection);

        // ask this newly-connected player for their login info
        connection.sendTCP(new PlayerLoginRequest());
    }

    @Override
    public void disconnected(Connection connection) {
        super.disconnected(connection);

        // find players who are associated w/this connection and set their status as disconnected
        for (Player p : players.values()) {
            if (p.getConn() != null && p.getConn().equals(connection)) {
                p.setStatus(Player.PlayerStatus.DISCONNECTED);
                p.releaseConn();
            }
        }
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
            EntityList dirty_ents = world.entity_mgr.getDirtyEnts();
            if (dirty_ents.entities.size() > 0) {
                // System.out.printf("Sending out %d dirty entities\n", dirty_ents.entities.size());

                server.sendToAllTCP(dirty_ents);
                dirty_ents.entities.clear();
            }

            // finally send out any ent death notifications
            EntDeathNotifyList dead_ents = world.entity_mgr.getInvalidatedEntIDs();
            if (dead_ents.ids.size() > 0) {
                // System.out.printf("Sending out %d dead entity notifications\n", dead_ents.ids.size());

                server.sendToAllTCP(dead_ents);
                dead_ents.ids.clear();
            }
        }
    }

    protected Player createPlayer(Connection conn) {
        long playerID = getNewPlayerID();
        Player newPlayer = new Player(playerID, conn);

        // add to the player list
        players.put(newPlayerID, newPlayer);

        return newPlayer;
    }

    protected long getNewPlayerID() {
        newPlayerID = (newPlayerID + 1) % Long.MAX_VALUE;
        return newPlayerID;
    }

    protected void sendFullState(Connection connection) {
        // send user the map + all entities
        synchronized (world) {
            System.out.println("Entered synchronized block...");
            HexRegion region = world.tiles.getMapAsRegion();
            System.out.println("Acquired region, sending!");
            connection.sendTCP(region);
            System.out.printf("Sent %d tiles to %s\n", region.region.size(), connection);

            connection.sendTCP(world.entity_mgr.getEntityList());
            System.out.printf("Sent %d entity notifications to %s\n", world.entity_mgr.getEntityList().entities.size(), connection);
        }
    }
}
