package framework.interaction.widgets.layouts;

import entrypoint.HexagonApp;
import framework.interaction.widgets.Widget;
import processing.core.PVector;

import java.lang.annotation.ElementType;

/**
 * created by Faisal on 4/23/2014 3:57 PM
 */
public class RelativeLayout extends Layout {
    protected PVector offset;

    public RelativeLayout(HexagonApp state, PVector offset) {
        super(state);
        this.offset = offset.get();
    }

    @Override
    public void draw() {
        state.pushMatrix();
        state.translate(offset.x, offset.y);

        for (Widget w : widgets) {
            state.pushMatrix();
            state.translate(w.getX(), w.getY());
            w.draw();
            state.popMatrix();
        }

        state.popMatrix();
    }
}
