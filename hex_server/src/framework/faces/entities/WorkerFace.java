package framework.faces.entities;

import domain.entities.Entity;
import domain.entities.Worker;
import framework.World;

/**
 * created by Faisal on 3/16/14 2:14 PM
 */
public class WorkerFace extends EntityFace<Worker> {
    public WorkerFace(Worker entity, World world) {
        super(entity, world);
    }

    @Override
    public boolean update() {
        // do whatever workers do here
        return false;
    }
}
