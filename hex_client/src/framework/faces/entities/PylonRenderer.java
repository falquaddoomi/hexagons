package framework.faces.entities;

import domain.entities.Pylon;
import entrypoint.HexagonApp;
import processing.core.PApplet;

/**
 * created by Faisal on 4/21/2014 3:19 PM
 */
public class PylonRenderer extends EntityRenderer<Pylon> {
    public PylonRenderer(Pylon entity, HexagonApp state) {
        super(entity, state);
    }

    @Override
    public void drawRelative() {
        // i suspect we should draw the pylon here
        state.fill(200, 200);
        state.triangle(
                PApplet.sin(state.frameCount / 50.0f)*10.0f, PApplet.cos(state.frameCount / 50.0f)*5.0f - 10,
                PApplet.sin(state.frameCount / 50.0f + PApplet.PI)*10.0f, PApplet.cos(state.frameCount / 50.0f + PApplet.PI)*5.0f - 10,
                0, 15);
        state.fill(200, 200);
        state.triangle(
                PApplet.cos(-state.frameCount / 50.0f)*10.0f, PApplet.sin(-state.frameCount / 50.0f)*5.0f - 10,
                PApplet.cos(-state.frameCount / 50.0f + PApplet.PI)*10.0f, PApplet.sin(-state.frameCount / 50.0f + PApplet.PI)*5.0f - 10,
                0, 15);
    }
}
