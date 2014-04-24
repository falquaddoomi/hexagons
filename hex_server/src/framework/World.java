package framework;

import domain.board.HexCoord;
import domain.board.HexMap;
import domain.board.HexRegion;
import domain.board.HexTile;
import domain.entities.Entity;
import domain.entities.EntityList;
import domain.entities.Pylon;
import domain.entities.Worker;
import framework.faces.board.HexMapFace;
import framework.faces.entities.EntityFace;
import framework.faces.entities.PylonFace;
import framework.faces.entities.WorkerFace;
import framework.managers.EntityManager;
import framework.network.Player;
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
    public EntityManager entity_mgr = new EntityManager();

    public World() {
        this.tiles = new HexMapFace(new HexMap(8), this);

        // create a candidate entity
        Worker w = new Worker();
        w.coord = new HexCoord(0, 0, 0);
        entity_mgr.createEntity(new WorkerFace(w, this));
    }

    public void update() {
        // update all the entities first
        // populates all the dirty fields that the hex server uses
        entity_mgr.update();

        // also iterate over the HexMap and updates tiles
        for (HexTile tile : tiles.wrapped.map.values()) {
            tiles.update(tile);
        }
    }

    /***
     * Creates entities for the given player if they're connecting for the first time.
     * @param player the new player who's just connected.
     * @return the pylon which has been created for the player
     */
    public Pylon setupPlayer(Player player) {
        // make a pylon on some random location on the board
        Pylon pylon = new Pylon();
        pylon.coord = tiles.get().getRandomTileCoord();
        pylon.ownerid = player.getID();
        PylonFace pylonFace = new PylonFace(pylon, this);
        entity_mgr.createEntity(pylonFace);

        // FIXME: also make a worker nearby this pylon

        return pylon;
    }
}
