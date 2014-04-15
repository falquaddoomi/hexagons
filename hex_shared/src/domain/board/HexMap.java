package domain.board;

import processing.core.PApplet;

import java.util.HashMap;

/**
* created by Faisal on 3/5/14 2:03 PM
*/
public class HexMap {
    public HashMap<HexCoord, HexTile> map = new HashMap<HexCoord, HexTile>();

    public HexMap() {
        // yep, just doing nothing
    }

    public HexMap(int radius) {
        if (radius <= 0)
            return;

        // let's allocate a few tiles in the hashmap
        for (int q = -radius; q <= radius; q++) {
            for (int r = -radius; r <= radius; r++) {
                // convert axial coords to x, y, z
                int x = q - (r + (r&1)) / 2;
                int z = r;
                int y = -x-z;
                HexCoord candidate = new HexCoord(x, y, z);

                if (hexDistFromOrigin(candidate) > radius)
                    continue;

                map.put(candidate, new HexTile(x, y, z));
            }
        }
    }

    public HexRegion getMapAsRegion() {
        HexRegion region = new HexRegion();
        region.region.putAll(map);
        return region;
    }

    public HexTile get(int x, int y, int z) {
        return map.get(new HexCoord(x, y, z));
    }

    public static int hexDist(HexCoord a, HexCoord b) {
        return PApplet.max(PApplet.abs(a.x - b.x), PApplet.abs(a.y - b.y), PApplet.abs(a.z - b.z));
    }

    public static int hexDistFromOrigin(HexCoord a) {
        return PApplet.max(PApplet.abs(a.x), PApplet.abs(a.y), PApplet.abs(a.z));
    }

    public void roundHexCoord(HexCoord c) {
        int rx = PApplet.round(c.x);
        int ry = PApplet.round(c.y);
        int rz = PApplet.round(c.z);

        int x_diff = PApplet.abs(rx - c.x);
        int y_diff = PApplet.abs(ry - c.y);
        int z_diff = PApplet.abs(rz - c.z);

        if (x_diff > y_diff && x_diff > z_diff)
            rx = -ry-rz;
        else if (y_diff > z_diff)
            ry = -rx-rz;
        else
            rz = -rx-ry;

        c.x = rx; c.y = ry; c.z =rz;
    }
}
