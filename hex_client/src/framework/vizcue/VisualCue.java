package framework.vizcue;

import entrypoint.HexagonApp;
import processing.core.PVector;

/**
 * created by Faisal on 4/16/2014 10:56 AM
 */
public abstract class VisualCue {
    protected final HexagonApp state;
    protected PVector pos;

    public VisualCue(HexagonApp state, PVector pos) {
        this.pos = pos.get();
        this.state = state;
    }

    public void draw() {
        state.pushMatrix();
        state.translate(pos.x, pos.y);
        drawTransformed();
        state.popMatrix();
    }

    /***
     * Draws the visual cue's body.
     */
    public abstract void drawTransformed();
}
