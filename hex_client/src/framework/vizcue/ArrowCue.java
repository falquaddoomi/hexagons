package framework.vizcue;

import entrypoint.HexagonApp;
import processing.core.PVector;

/**
 * created by Faisal on 4/16/2014 10:56 AM
 */
public class ArrowCue extends VisualCue {
    protected int num_arrows = 5;
    protected int MAX_MOD = 50;

    public ArrowCue(HexagonApp state, PVector pos) {
        super(state, pos);
    }

    @Override
    public void drawTransformed() {
        state.pushStyle();

        int mod_len = state.frameCount*2 % MAX_MOD;

        state.fill(255, 100 + ((mod_len < MAX_MOD/2)?(mod_len*10):0));

        for (int i = 0; i < num_arrows; i++) {
            state.pushMatrix();

            state.translate((i*MAX_MOD + mod_len)/3.0f - 40, 0);

            // some set of arrows are going to be coming in
            if (i == 0)
                state.scale(mod_len/(float)MAX_MOD);
            else if (i == num_arrows-1)
                state.scale(1.0f - mod_len/(float)MAX_MOD);

            state.triangle(-10, -10, 5, 0, -10, 10);

            state.popMatrix();
        }

        state.popStyle();
    }
}
