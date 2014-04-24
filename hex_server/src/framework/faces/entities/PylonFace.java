package framework.faces.entities;

import domain.entities.Pylon;
import framework.World;

/**
 * created by Faisal on 4/21/2014 3:16 PM
 */
public class PylonFace extends  EntityFace<Pylon> {
    public PylonFace(Pylon entity, World world) {
        super(entity, world);
    }

    @Override
    public boolean update() {
        int old_energy = wrapped.energy;

        // harvest energy every so often
        // only harvest if our internal buffer isn't full
        if (wrapped.energy < wrapped.max_energy) {
            wrapped.energy += world.tiles.harvest(wrapped.coord, 20);
        }

        return old_energy != wrapped.energy;
    }
}
