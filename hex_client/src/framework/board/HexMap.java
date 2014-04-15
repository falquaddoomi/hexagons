package framework.board;

import entrypoint.hexagon_maps;
import processing.core.PApplet;
import processing.core.PConstants;

import java.util.HashMap;
import java.util.Map;

/**
* created by Faisal on 3/5/14 2:03 PM
*/
public class HexMap {
    public int TILE_SIZE = 24, OFFSET = 2;
    public HashMap<HexCoord, HexTile> map = new HashMap<HexCoord, HexTile>();
    private hexagon_maps state;

    public HexMap(hexagon_maps state, int radius) {
        this.state = state;

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

                map.put(candidate, new HexTile(state, x, y, z));
            }
        }
    }

    public HexTile get(int x, int y, int z) {
        return map.get(new HexCoord(x, y, z));
    }

    public HexTile getTileAtPxCoords(float x, float y) {
        // compute axial coords from x, y pixel coords
        float q = 2.0f/3.0f * x / TILE_SIZE;
        float r = (1.0f/3.0f* PApplet.sqrt(3.0f) * y - 1.0f/3.0f * x) / TILE_SIZE;

        // convert from axial to hex
        int tx = (int)(q - (r + ((int)r&1)) / 2);
        int tz = (int)r;
        int ty = -tx-tz;

        // create a hex coordinate then round it to the hex grid
        HexCoord c = new HexCoord(tx, ty, tz);
        roundHexCoord(c);

        PApplet.println("Coords: " + c.x + ", " + c.y + ", " + c.z);

        // and then make a hexcoord like that?
        return map.get(c);
    }

    public void draw() {
        // iterate through the hashmap and draw everything that's been allocated
        for (Map.Entry entry : map.entrySet()) {
            HexCoord coord = (HexCoord)entry.getKey();
            HexTile tile = (HexTile)entry.getValue();

            // convert hex coord to screen coord and draw that hexagon
            drawHexagon(tile);
        }
    }

    public int hexDist(HexCoord a, HexCoord b) {
        return PApplet.max(PApplet.abs(a.x - b.x), PApplet.abs(a.y - b.y), PApplet.abs(a.z - b.z));
    }

    public int hexDistFromOrigin(HexCoord a) {
        return PApplet.max(PApplet.abs(a.x), PApplet.abs(a.y), PApplet.abs(a.z));
    }

    public void drawHexagon(HexTile t) {
        // nab the location first
        HexCoord c = t.coord;

        // convert hex coords to pixel coords
        int q = c.x + (c.z - (c.z&1)) / 2, r = c.z;
        float cx = TILE_SIZE * PApplet.sqrt(3) * (q + 0.5f * (r & 1));
        float cy = TILE_SIZE * 3/2 * r;

        state.pushMatrix();
        state.translate(cx, cy);

        // lerp the current color toward white every so often
        state.pushStyle();

        if (t.shine_debounce > 0) {
            state.fill(
                    state.lerpColor(state.color(255), state.g.fillColor, 1.0f - t.shine_debounce / PApplet.parseFloat(HexTile.SHINE_DURATION))
            );
            t.shine_debounce -= 1;
        }

        // draw a single hexagon
        state.beginShape();
        for (int i = 0; i < 6; i++) {
            float angle = 2 * PConstants.PI / 6 * (i + 0.5f);
            state.vertex((TILE_SIZE - OFFSET) * PApplet.cos(angle), (TILE_SIZE - OFFSET) * PApplet.sin(angle));
        }
        state.endShape(PConstants.CLOSE);

        // draw text on this thing
        state.textAlign(PConstants.CENTER, PConstants.CENTER);
        state.fill(255);
        state.text(c.x + ", " + c.y + ", " + c.z, 0, 0);

        state.popStyle();

        state.popMatrix();
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
