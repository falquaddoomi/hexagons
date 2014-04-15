package framework.faces.board;

import domain.board.HexCoord;
import domain.board.HexTile;
import framework.World;
import framework.faces.UpdateFace;
import support.Face;
import framework.faces.entities.EntityFace;

import java.util.ArrayList;

/**
 * created by Faisal on 3/16/14 3:03 PM
 */
public class HexTileFace extends UpdateFace<HexTile> {
    // maintain a list of entities we know live on this tile
    protected ArrayList<EntityFace> entites = new ArrayList<EntityFace>();

    public HexTileFace(HexTile tile, World world) {
        super(tile, world);
    }
}
