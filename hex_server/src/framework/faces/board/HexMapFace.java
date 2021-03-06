package framework.faces.board;

import domain.board.HexCoord;
import domain.board.HexMap;
import domain.board.HexRegion;
import domain.board.HexTile;
import framework.World;
import support.Face;

/**
 * created by Faisal on 3/16/14 6:06 PM
 */
public class HexMapFace extends Face<HexMap> {
    protected World world;

    public HexMapFace(HexMap wrapped, World world) {
        super(wrapped);
        this.world = world;
    }

    public HexRegion getMapAsRegion() {
        return wrapped.getMapAsRegion();
    }

    public boolean update(HexTile tile) {
        boolean dirty = false;

        // move potential to our neighbors, if possible
        for (HexCoord ncoord : tile.coord.neighbors()) {
            // if there's a tile for this coordinate, shift our flow
            HexTile candidate = wrapped.map.get(ncoord);
            if (candidate != null && tile.potential > 10 &&  candidate.potential < tile.potential/2) {
                candidate.potential += 10;
                tile.potential -= 10;

                // add us both to the 'map_dirty' list
                world.map_dirty.append(tile);
                world.map_dirty.append(candidate);
                dirty = true;
            }
        }

        return dirty;
    }

    /***
     * Attempts to draw energy from the given HexCoord, removing it from the field and returning the amount that could be drawn.
     * @param coord the coordinate from which to draw field potential.
     * @param requested the amount that we'd like to request
     * @return the amount of energy that could be returned.
     */
    public int harvest(HexCoord coord, int requested) {
        HexTile target = wrapped.get(coord);

        // FIXME: should there be a 'bandwidth' limit to the request?

        if (requested < target.potential) {
            // take what was requested
            target.potential -= requested;
            return requested;
        } else {
            // take everything
            int total_drawn = target.potential;
            target.potential = 0;
            return total_drawn;
        }
    }
}
