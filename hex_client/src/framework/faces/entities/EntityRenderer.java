package framework.faces.entities;

import domain.entities.Entity;
import entrypoint.HexagonApp;
import framework.faces.RenderFace;
import framework.faces.board.HexMapRenderer;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

/**
 * created by Faisal on 3/15/14 5:33 PM
 */
public abstract class EntityRenderer<T extends Entity> extends RenderFace<T> {
    PVector loc = new PVector();

    public EntityRenderer(T entity, HexagonApp state) {
        super(entity, state);
        loc.set(getServerLoc());
    }

    public void draw() {
        state.pushMatrix();
        state.pushStyle();
        state.translate(loc.x, loc.y);

        state.pushMatrix();
        drawRelative();
        state.popMatrix();

        // draw the energy bar background
        // FIXME: only in debug mode?
        int BAR_WIDTH = 20;
        state.rectMode(PApplet.CENTER);
        state.fill(100);
        state.rect(0, 10, BAR_WIDTH, 4);

        // draw the colored bar filling it up
        float energy_frac = wrapped.energy/(float)wrapped.max_energy;
        state.rectMode(PConstants.CORNER);
        state.fill(energy_frac*60, 255, 200);
        state.rect(-BAR_WIDTH/2, 8, BAR_WIDTH*energy_frac, 4);

        state.popStyle();
        state.popMatrix();

        // deal with interpolating the visible loc to the server's loc
        if (PVector.sub(loc, getServerLoc()).magSq() > 0.1) {
            // move loc toward server_loc
            loc.set(PVector.lerp(loc, getServerLoc(), 0.25f));
        }
    }

    public void drawRelative() { }

    public PVector getServerLoc() {
        return HexMapRenderer.getPixelForHexCoord(wrapped.coord);
    }

    @Override
    public T get() {
        return super.get();
    }
}
