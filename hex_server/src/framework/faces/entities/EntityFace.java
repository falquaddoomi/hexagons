package framework.faces.entities;

import domain.entities.Entity;
import framework.World;
import framework.faces.UpdateFace;

/**
* created by Faisal on 3/5/14 2:05 PM
*/
public abstract class EntityFace<T extends Entity> extends UpdateFace<Entity> {
    public EntityFace(T entity, World world) {
        super(entity, world);
    }

    @Override
    public Entity get() {
        return super.get();
    }
}
