package domain.entities;

import domain.board.HexCoord;

/**
* created by Faisal on 3/5/14 2:05 PM
*/
public abstract class Entity {
    protected boolean isAlive = true;
    public HexCoord coord, dest;
    public int energy = 0, max_energy = 100;
    public long id;
    public long ownerid = -1;

    public long getId() { return id; }
    public long getOwnerId() { return ownerid; }

    public boolean isAlive() { return isAlive; }
    public void kill() { isAlive = false; }

    public void sync(Entity delta) {
        this.coord = delta.coord;
        this.energy = delta.energy;
        this.id = delta.id; // FIXME: should we really be syncing the id?
        this.ownerid = delta.ownerid;
    }
}
