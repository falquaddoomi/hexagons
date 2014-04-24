package framework.interaction.widgets.layouts;

import entrypoint.HexagonApp;
import framework.interaction.widgets.Clickable;
import framework.interaction.widgets.Widget;

import java.util.ArrayList;

/**
 * A basic layout that just draws the child elements, treating their coordinates as absolute positions.
 */
public class Layout {
    protected final ArrayList<Widget> widgets = new ArrayList<Widget>();
    protected final HexagonApp state;

    public Layout(HexagonApp state) {
        this.state = state;
    }

    public void draw() {
        for (Widget e : widgets) {
            state.pushMatrix();
            state.translate(e.getX(), e.getY());
            e.draw();
            state.popMatrix();
        }
    }

    public boolean handleClick(int x, int y, boolean pressed, int button) {
        for (Widget w: widgets) {
            // only examine elements that are clickable
            if (w instanceof Clickable) {
                // no transform necessary since
                Clickable clicky = (Clickable)w;
                if (clicky.contains(x, y) && clicky.handlePress(button))
                    return true;
            }
        }

        return false;
    }

    public int getWidth() { return getDim(false); }
    public int getHeigth() { return getDim(true); }

    private int getDim(boolean vertical) {
        // find the element v w/minimal v.x-v.width
        // do the same for maxes and for the y axis

        int min = 99999, max = 0;
        for (Widget w : widgets) {
            int coord = (vertical)?w.getY():w.getX();
            int diff = (vertical)?w.getHeight():w.getWidth();
            if (coord < min)
                min = coord;
            if (coord + diff > max)
                max = coord + diff;
        }

        return max - min;
    }
}
