package domain.board;

/**
* created by Faisal on 3/5/14 2:03 PM
*/
public class HexCoord {
    public int x, y, z;

    public HexCoord() {
        this.x = 0; this.y = 0; this.z = 0;
    }

    public HexCoord(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public HexCoord get() {
        return new HexCoord(this.x, this.y, this.z);
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HexCoord that = (HexCoord)o;

        if (x != that.x) return false;
        if (y != that.y) return false;
        if (z != that.z) return false;

        return true;
    }

    public int hashCode()
    {
        int result = (int) (x ^ (x >>> 32));
        result = 31 * result + (int) (y ^ (y >>> 32));
        result = 31 * result + (int) (z ^ (z >>> 32));
        return result;
    }

    /**
     * Returns an array of the six neighboring coords to this position.
     * @return a HexCoord array of the six neighboring coords to this position.
     */
    public HexCoord[] neighbors() {
        return new HexCoord[]{
            new HexCoord(x-1, y+1, z),
            new HexCoord(x, y+1, z-1),
            new HexCoord(x+1, y, z-1),
            new HexCoord(x+1, y-1, z),
            new HexCoord(x, y-1, z+1),
            new HexCoord(x-1, y, z+1)
        };
    }
}
