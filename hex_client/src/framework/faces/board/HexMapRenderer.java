package framework.faces.board;

import domain.board.HexCoord;
import domain.board.HexMap;
import domain.board.HexTile;
import entrypoint.HexagonApp;
import framework.faces.RenderFace;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.Map;

/**
 * created by Faisal on 3/10/14 5:00 PM
 */
public class HexMapRenderer extends RenderFace<HexMap> {
    public static int TILE_SIZE = 24, OFFSET = 2;
    private HexCoord selection = null;

    public HexMapRenderer(HexagonApp state, HexMap target) {
        super(target, state);
    }

    /***
     * Draws the entire map, centered at the origin.
     */
    public void draw() {
        // iterate through the hashmap and draw everything that's been allocated
        for (Map.Entry entry : wrapped.map.entrySet()) {
            // convert hex coord to screen coord and draw that hexagon
            drawHexagon((HexTile)entry.getValue());
        }
    }

    /***
     * Draws a given HexTile at its HexCoord on the screen.
     * @param t the HexTile to draw.
     */
    public void drawHexagon(HexTile t) {
        // nab the location first
        HexCoord c = t.coord;
        PVector loc = getPixelForHexCoord(c);

        // cull hexagons that are outside the viewport
        // FIXME: should handle board scaling
        PVector transformed = PVector.add(loc, state.cam.focus);
        if (transformed.x < -TILE_SIZE || transformed.x > state.width+TILE_SIZE || transformed.y < -TILE_SIZE || transformed.y > state.height+TILE_SIZE)
            return;

        state.pushMatrix();
        state.translate(loc.x, loc.y);

        // lerp the current color toward white every so often
        state.pushStyle();

        // if this tile is selected, draw the reticule around it
        if (c.equals(selection)) {
            state.stroke(state.color(255, PApplet.sin(state.frameCount/20.0f)*55.0f + 200.0f));
            state.strokeWeight(3.0f);
        }

        // visually indicate amount of potential in this cell
        double potential_ratio = Math.min(1.0, t.potential/5000.0); // 5000.0 is the assumed max potential
        state.fill(state.color(
                100,
                // (int)(Math.sin(state.frameCount/(10.0 / potential_ratio))*50.0 + 100), // saturation = sin framecount
                200,
                (int)(50 + potential_ratio * 100.0) // brightness = potential
        ));

        // draw a single hexagon
        state.beginShape();
        for (int i = 0; i < 6; i++) {
            float angle = 2 * PConstants.PI / 6 * (i + 0.5f);
            state.vertex((TILE_SIZE - OFFSET) * PApplet.cos(angle), (TILE_SIZE - OFFSET) * PApplet.sin(angle));
        }
        state.endShape(PConstants.CLOSE);

        // draw text on this thing
        if (state.debug) {
            state.textAlign(PConstants.CENTER, PConstants.CENTER);
            state.fill(255);
            state.text(c.x + ", " + c.y + ", " + c.z + "\n" + t.potential, 0, 0);
        }

        state.popStyle();

        state.popMatrix();
    }

    /***
     * Returns a PVector of the pixel coordinates of the center of the given HexCoord.
     * @param c the HexCoord for which to retrieve the pixel coordinates
     * @return a PVector consisting of the pixel coordinates
     */
    public static PVector getPixelForHexCoord(HexCoord c) {
        // convert hex coords to pixel coords
        int q = c.x + (c.z - (c.z&1)) / 2, r = c.z;
        float cx = TILE_SIZE * PApplet.sqrt(3) * (q + 0.5f * (r & 1));
        float cy = TILE_SIZE * 3/2 * r;

        return new PVector(cx, cy);
    }

    /***
     * Returns the HexCoord corresponding to the pixel coordinate.
     * @param x the x component of the pixel coordinate
     * @param y the y component of the pixel coordinate
     * @return the HexCoord corresponding to the pixel coordinate
     */
    public static HexCoord getHexCoordForPixel(float x, float y) {
        // compute axial coords from x, y pixel coords
        float q = ((1.0f/3.0f)*(float)Math.sqrt(3.0f) * x - (1.0f/3.0f) * y) / (float)TILE_SIZE;
        float r = (2.0f/3.0f) * y / (float)TILE_SIZE;

        // convert axial coords to x, y, z
        float tx = q;
        float tz = r;
        float ty = -tx-tz;

        // create a hex coordinate then round it to the hex grid
        HexCoord c = roundHexCoord(tx, ty, tz);

        // and then make a hexcoord like that?
        return c;
    }

    /***
     * Gets the selected HexCoord, or null if nothing is selected.
     * @return the selected HexCoord, or null if no selection exists.
     */
    public HexCoord getSelection() {
        return selection;
    }

    /***
     * Sets the given HexCoord as the selection.
     *
     * Also displays a selection reticule around the corresponding map tile, if it exists.
     * @param selection the HexCoord to select
     */
    public void setSelection(HexCoord selection) {
        this.selection = selection;
    }

    /***
     * Takes a fractional set of 3-axis coords and discretizes them.
     * @param cx x-coordinate in the 3-axis space
     * @param cy y-coordinate in the 3-axis space
     * @param cz z-coordinate in the 3-axis space
     * @return a HexCoord containing the discretized location in the grid
     */
    public static HexCoord roundHexCoord(float cx, float cy, float cz) {
        int rx = PApplet.round(cx);
        int ry = PApplet.round(cy);
        int rz = PApplet.round(cz);

        int x_diff = PApplet.abs(rx - (int)cx);
        int y_diff = PApplet.abs(ry - (int)cy);
        int z_diff = PApplet.abs(rz - (int)cz);

        if (x_diff > y_diff && x_diff > z_diff)
            rx = -ry-rz;
        else if (y_diff > z_diff)
            ry = -rx-rz;
        else
            rz = -rx-ry;

        return new HexCoord(rx, ry, rz);
    }
}
