package domain.board;

/**
* created by Faisal on 3/5/14 2:03 PM
*/
public class HexTile {
    // represents a single tile
    // should we know our own location?
    public HexCoord coord;

    // potential stored in this node
    public int potential;
    public boolean dirty;

    public HexTile() {
        this(0, 0, 0);
    }

    public HexTile(int x, int y, int z) {
        coord = new HexCoord(x, y, z);
        potential = (int)(Math.random() * 5000.0);
    }

    public HexTile(HexCoord c) {
        coord = c;
    }

    // copy constructor(?)
    public HexTile(HexTile copy) {
        this.coord = copy.coord.get();
    }
}
