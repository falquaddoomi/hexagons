package domain.board;

import processing.core.PApplet;

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

    public int hexDist(HexCoord other) {
        return PApplet.max(PApplet.abs(x - other.x), PApplet.abs(y - other.y), PApplet.abs(z - other.z));
    }

    /***
     * Returns the HexCoord neighboring the current that's in the direction of the destination.
     * @param dest the eventual target of our movement
     * @return an adjacent tile that's closer to the target
     */
    public HexCoord inDirection(HexCoord dest) {
        // this is a stupid way to do it, but iterate over each neighbor and return the one with the min dist to dest
        // FIXME: we should somehow be able to identify the neighbor just based on the difference of this vs. dest
        int min_dist = 9999;
        HexCoord candidate = null;

        for (HexCoord n : neighbors()) {
            int dist = n.hexDist(dest);
            if (dist < min_dist) {
                min_dist = dist;
                candidate = n;
            }
        }

        return candidate;
    }
}
