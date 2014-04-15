package framework.network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import domain.board.HexCoord;
import domain.board.HexRegion;
import domain.effects.Effect;
import domain.entities.Entity;
import domain.entities.EntityList;
import domain.entities.Worker;
import exchanges.MoveRequest;
import framework.WorldView;
import framework.faces.board.HexMapRenderer;
import framework.faces.entities.EntityRenderer;
import framework.faces.entities.WorkerRenderer;
import notifies.EntDeathNotifyList;
import support.Registry;

import java.io.IOException;
import java.util.ArrayList;

/**
 * created by Faisal on 3/5/14 4:42 PM
 */
public class HexClient extends Listener {
    private Client client;
    private final WorldView worldview;

    public HexClient(WorldView worldview) {
        this.worldview = worldview;

        try {
            client = new Client(8192, 8192);

            // register classes first
            Registry.register(client.getKryo());

            client.start();
            client.connect(5000, "localhost", 54555, 54777);

            client.addListener(this);

        } catch (IOException e) {
            System.err.println("Couldn't connect to localhost:54555, continuing...");
        }
    }

    @Override
    public void received(Connection connection, Object o) {
        // dispatch based on detected object type
        if (o instanceof HexRegion) {
            HexRegion region = (HexRegion)o;
            System.out.printf("Received region of size %d!\n", region.region.size());
            // add everything in the HexRegion to our render cache
            worldview.addRegion(region);
        }
        else if (o instanceof Effect) {
            // add the effect to the worldview
            synchronized (worldview.effects) {
                worldview.effects.add((Effect)o);
            }
        }
        else if (o instanceof EntityList) {
            System.out.printf("Got entity update notification\n");

            // unpack the entities into the worldview
            synchronized (worldview.entities) {
                for (Entity e : ((EntityList) o).entities.values()) {
                    // iterate through every entity in the update/create list

                    if (worldview.entities.containsKey(e.getId())) {
                        // since we already contain it, update just the backed instance
                        worldview.entities.get(e.getId()).get().sync(e);
                    }
                    else {
                        // it's a new entity, wrap it in an appropriate face
                        if (e instanceof Worker)
                            worldview.entities.put(e.getId(), new WorkerRenderer((Worker)e, worldview.state));
                        // we ignore entities for which we don't have faces
                        System.out.printf("Added entity %d to list!\n", e.getId());
                    }
                }
            }
        }
        else if (o instanceof EntDeathNotifyList) {
            // remove the entity from our list
            synchronized (worldview.entities) {
                for (Long dead_id : ((EntDeathNotifyList) o).ids) {
                    if (worldview.entities.containsKey(dead_id)) {
                        // get the old entity to display the death splash location
                        Entity e = worldview.entities.get(dead_id).get();

                        // FIXME: is this the right thing to do to indicate the removal?
                        Effect removeEffect = new Effect();
                        removeEffect.max_life = 50;
                        removeEffect.pos = e.coord;
                        worldview.effects.add(removeEffect);

                        // and finally axe this dead entity
                        worldview.entities.remove(dead_id);
                    }
                }
            }
        }
    }

    public void sendMoveRequest(HexCoord from, HexCoord to) {
        MoveRequest click = new MoveRequest();
        click.from = from;
        click.to = to;
        client.sendTCP(click);
    }
}
