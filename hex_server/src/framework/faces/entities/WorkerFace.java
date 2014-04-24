package framework.faces.entities;

import domain.board.HexCoord;
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
        // FIXME: perhaps we should throttle updates via some internal 'wait' timer
        // the timer would decrement until it hits 0, then do the update and return to its previous value

        // flag that we'll return at the end if we changed *anything*
        boolean did_action = false;

        // draw from the field and update our energy storage
        int old_energy = wrapped.energy;

        // only harvest if our internal buffer isn't full
        if (wrapped.energy < Worker.MAX_RESERVE_ENERGY) {
            wrapped.energy += world.tiles.harvest(wrapped.coord, 5);

            // report energy changes to the client
            if (old_energy != wrapped.energy)
                did_action = true;
        }

        // attempt a move if we have a destination that's not our current one
        if (wrapped.dest != null && !wrapped.dest.equals(wrapped.coord) && wrapped.energy >= Worker.MOVE_ENERGY) {
            // debit our energy
            wrapped.energy -= 20;

            // try to move one closer to our dest
            HexCoord newpos = wrapped.coord.inDirection(wrapped.dest);

            // ensure the newpos exists in the map
            if (world.tiles.wrapped.get(newpos) != null) {
                // since it does, do the move...
                wrapped.coord = newpos;
                // ...and mark us as being dirty since we moved
                did_action = true;
            }
        }
        else if (wrapped.dest != null && wrapped.dest.equals(wrapped.coord)) {
            // clear our dest if we've reached it
            wrapped.dest = null;
        }

        // true if our energy level changed, indicating that we're "dirty"
        return did_action;
    }
}
