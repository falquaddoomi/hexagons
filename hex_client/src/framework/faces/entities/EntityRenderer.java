package framework.faces.entities;

import domain.entities.Entity;
import entrypoint.hexagon_maps;
import framework.faces.RenderFace;
import framework.faces.board.HexMapRenderer;
import processing.core.PApplet;
import processing.core.PVector;

/**
 * created by Faisal on 3/15/14 5:33 PM
 */
public abstract class EntityRenderer<T extends Entity> extends RenderFace<T> {
    PVector loc = new PVector();

    public EntityRenderer(T entity, hexagon_maps state) {
        super(entity, state);
        loc.set(getServerLoc());
    }

    public void draw() {
        state.pushMatrix();
        state.pushStyle();
        state.translate(loc.x, loc.y);
        drawRelative();
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
