package framework.managers;

import domain.entities.EntityList;
import framework.faces.entities.EntityFace;
import notifies.EntDeathNotifyList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * created by Faisal on 4/18/2014 5:28 PM
 */
public class EntityManager {
    protected EntityList ents_dirty = new EntityList();
    protected EntDeathNotifyList ents_dead_dirty = new EntDeathNotifyList();
    protected HashMap<Long, EntityFace> entities = new HashMap<Long, EntityFace>();
    protected long entity_curID = 1;

    public HashMap<Long, EntityFace> getEntities() {
        return entities;
    }

    public EntityList getEntityList() {
        EntityList entities_unwrapped = new EntityList();
        for (EntityFace e : entities.values())
            entities_unwrapped.entities.put(e.get().id, e.get());
        return entities_unwrapped;
    }

    public EntityList getDirtyEnts() {
        return ents_dirty;
    }

    public EntDeathNotifyList getInvalidatedEntIDs() {
        return ents_dead_dirty;
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
        // update all the entities
        // also cull dead entities and put their notifications on the queue
        Iterator<Map.Entry<Long,EntityFace>> iter = entities.entrySet().iterator();
        while (iter.hasNext()) {
            EntityFace e = iter.next().getValue();
            if (e.get().isAlive()) {
                if (e.update()) {
                    // if update returns true, it means this entity needs to send state to the client
                    ents_dirty.entities.put(e.get().getId(), e.get());
                }
            }
            else {
                ents_dead_dirty.ids.add(e.get().getId());
                iter.remove();
            }
        }
    }
}
