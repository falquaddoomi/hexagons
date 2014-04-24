package domain.entities;

/**
 * created by Faisal on 4/17/2014 1:31 PM
 */
public class Pylon extends Entity {
    public static final int MAX_RESERVE_ENERGY = 500;

    public Pylon() {
        max_energy = MAX_RESERVE_ENERGY;
    }
}
