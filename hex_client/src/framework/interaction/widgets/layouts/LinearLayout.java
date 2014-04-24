package framework.interaction.widgets.layouts;

import entrypoint.HexagonApp;
import framework.interaction.widgets.Widget;
import processing.core.PVector;

import java.lang.annotation.ElementType;

/**
 * created by Faisal on 4/23/2014 3:58 PM
 */
public class LinearLayout extends RelativeLayout {
    protected final boolean horizontal;

    public LinearLayout(HexagonApp state, PVector offset, boolean horizontal) {
        super(state, offset);
        this.horizontal = horizontal;
    }

    @Override
    public void draw() {
        state.pushMatrix();
        state.translate(offset.x, offset.y);

        int cur_offset = 0;
        for (Widget w : widgets) {
            state.pushMatrix();

            if (horizontal) {
                state.translate(cur_offset, w.getY());
                cur_offset += w.getWidth();
            }
            else {
                state.translate(w.getX(), cur_offset);
                cur_offset += w.getHeight();
            }

            w.draw();
            state.popMatrix();
        }

        state.popMatrix();
    }
}
