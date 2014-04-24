package framework.faces.entities;

import domain.entities.Worker;
import entrypoint.HexagonApp;
import processing.core.PApplet;

/**
 * created by Faisal on 3/16/14 6:46 PM
 */
public class WorkerRenderer extends EntityRenderer<Worker>  {
    public WorkerRenderer(Worker entity, HexagonApp state) {
        super(entity, state);
    }
    protected float advanceSpeed = 0.0f;

    @Override
    public void drawRelative() {
        state.rectMode(PApplet.CENTER);

        // left square
        state.rotate(advanceSpeed);
        state.fill(255, 100);
        state.rect(0, 0, 20, 20);

        // right square
        state.rotate(-advanceSpeed * 2.0f);
        state.fill(255, 100);
        state.rect(0, 0, 20, 20);

        advanceSpeed += PApplet.lerp(0.0f, 0.06f, wrapped.energy/(float)wrapped.max_energy);

        // maintain bounds
        if (advanceSpeed > PApplet.PI*2.0)
            advanceSpeed -= PApplet.PI*2.0;
    }
}
