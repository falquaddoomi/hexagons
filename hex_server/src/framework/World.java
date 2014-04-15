package framework;

import domain.board.HexCoord;
import domain.board.HexMap;
import domain.board.HexRegion;
import domain.board.HexTile;
import domain.entities.Entity;
import domain.entities.EntityList;
import domain.entities.Worker;
import framework.faces.board.HexMapFace;
import framework.faces.entities.EntityFace;
import framework.faces.entities.WorkerFace;
import notifies.EntDeathNotifyList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
* created by Faisal on 3/5/14 2:05 PM
*/
public class World {
    public HexMapFace tiles;
    public HexRegion map_dirty = new HexRegion();
    public EntityList ents_dirty = new EntityList();
    public EntDeathNotifyList ents_dead_dirty = new EntDeathNotifyList();
    public HashMap<Long, EntityFace> entities = new HashMap<Long, EntityFace>();
    public long entity_curID = 1;

    public World() {
        this.tiles = new HexMapFace(new HexMap(8), this);

        // create a candidate entity
        Worker w = new Worker();
        w.coord = new HexCoord(0, 0, 0);
        createEntity(new WorkerFace(w, this));
    }

    public EntityList getEntityList() {
        EntityList entities_unwrapped = new EntityList();
        for (EntityFace e : entities.values())
            entities_unwrapped.entities.put(e.get().id, e.get());
        return entities_unwrapped;
    }

    public void createEntity(EntityFace in) {
        // get a new entity ID
        entity_curID = (entity_curID + 1) % Long.MAX_VALUE;
        in.get().id = entity_curID;

        // add to both entity list and the output queue
        entities.put(entity_curID, in);
        ents_dirty.entities.put(entity_curID, in.get());
    }

    public void update() {
        // update all the entities first
        // also cull dead entities and put their notifications on the queue
        Iterator<Map.Entry<Long,EntityFace>> iter = entities.entrySet().iterator();
        while (iter.hasNext()) {
            EntityFace e = iter.next().getValue();
            if (e.get().isAlive()) {
                e.update();
            }
            else {
                ents_dead_dirty.ids.add(e.get().getId());
                iter.remove();
            }
        }

        // also iterate over the HexMap and updates tiles
        for (HexTile tile : tiles.wrapped.map.values()) {
            tiles.update(tile);
        }
    }
}
