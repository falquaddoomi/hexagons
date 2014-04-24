package framework.interaction.widgets;

import entrypoint.HexagonApp;

/**
 * created by Faisal on 4/23/2014 11:56 AM
 */
public abstract class Button extends Widget implements Clickable {
    protected boolean activated = false;

    public Button(HexagonApp state, int x, int y, int w, int h) {
        super(state, x, y, w, h);
    }

    @Override
    public boolean contains(int x, int y) {
        return (x >= this.x - width /2 && x <= this.x + width /2) && (y >= this.y - height /2 && y <= this.y + height /2);
    }

    @Override
    public void draw() {
        // draw a nice frame around the button
        state.noFill();
        state.stroke((activated)?200:150);
        state.strokeWeight((activated)?5:1);
        state.rect(0, 0, width, height);
        drawGraphic();
    }

    /***
     * Draws the graphic in the center of the button.
     */
    public void drawGraphic() {
        state.stroke(0, 255, (activated)?200:150);
        state.strokeWeight(8);
        state.line(-width /3, -height /3, width /3, height /3);
        state.line(width /3, -height /3, -width /3, height /3);
    }

    @Override
    public boolean handlePress() {
        activated = true;
        return true;
    }

    @Override
    public boolean handleRelease() {
        if (activated) {
            handleClick();
            activated = false;
            return true;
        }

        return false;
    }

    /***
     * Defines what should happen when the button is successfully clicked
     * (i.e. a mouse up event occurs within it while it's activated.)
     */
    public abstract void handleClick();
}
