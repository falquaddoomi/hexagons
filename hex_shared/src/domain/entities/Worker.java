package domain.entities;

/**
 * created by Faisal on 3/16/14 2:12 PM
 */
public class Worker extends Entity {
    // some constants that apply to workers on both the client and server
    public static final int MOVE_ENERGY = 20;
    public static final int MAX_RESERVE_ENERGY = 200;

    public Worker() {
        energy = MAX_RESERVE_ENERGY/2;
        max_energy = MAX_RESERVE_ENERGY;
    }
}
