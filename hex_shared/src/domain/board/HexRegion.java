package domain.board;

import java.util.HashMap;

/**
 * created by Faisal on 3/9/14 2:33 PM
 */
public class HexRegion {
    public HashMap<HexCoord, HexTile> region = new HashMap<HexCoord, HexTile>();

    public boolean append(HexTile tile, boolean replace) {
        if (!replace && region.containsKey(tile.coord))
            return false;

        region.put(tile.coord, tile);
        return true;
    }

    public void intersect(HexRegion other) {
        region.keySet().retainAll(other.region.keySet());
    }

    public boolean append(HexTile tile) {
        return append(tile, true);
    }

    public void clear() {
        region.clear();
    }

    public int size() {
        return region.size();
    }

    public void expand(HexCoord locus, int radius) {
        if (radius <= 0)
            return;

        // let's allocate a few tiles in the hashmap
        for (int q = -radius; q <= radius; q++) {
            for (int r = -radius; r <= radius; r++) {
                // convert axial coords to x, y, z
                int x = q - (r + (r&1)) / 2;
                int z = r;
                int y = -x-z;
                HexCoord candidate = new HexCoord(x + locus.x, y + locus.y, z + locus.z);

                if (HexMap.hexDist(locus, candidate) > radius || region.containsKey(candidate))
                    continue;

                region.put(candidate, new HexTile(x + locus.x, y + locus.y, z + locus.z));
            }
        }
    }
}
