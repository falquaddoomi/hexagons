package framework.faces;

import domain.effects.Effect;
import entrypoint.HexagonApp;
import framework.faces.board.HexMapRenderer;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * created by Faisal on 3/11/14 2:13 PM
 */
public class EffectSetRenderer {
    protected final HexagonApp state;
    protected final ArrayList<Effect> effects;

    public EffectSetRenderer(HexagonApp state, ArrayList<Effect> effects) {
        this.state = state;
        this.effects = effects;
    }

    public void draw() {
        synchronized (effects) {
            // iterate through the effects, drawing and aging each
            // remove any which have exceeded their max_life parameter
            Iterator<Effect> iter = effects.iterator();
            while (iter.hasNext()) {
                Effect e = iter.next();
                if (e.life++ >= e.max_life)
                    iter.remove();
                else {
                    // draw this effect somehow
                    drawEffect(e);
                }
            }
        }
    }

    private void drawEffect(Effect e) {
        state.pushStyle();
        state.noFill(); state.stroke(255, e.max_life - e.life);
        state.ellipseMode(PConstants.CENTER);

        // figure out pixel coord for effect
        PVector pos = HexMapRenderer.getPixelForHexCoord(e.pos);

        state.ellipse(pos.x, pos.y, e.life, e.life);
        state.popStyle();
    }
}
