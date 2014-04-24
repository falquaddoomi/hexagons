package framework.interaction.widgets;

/**
 * created by Faisal on 4/23/2014 11:51 AM
 */
public interface Clickable {
    public boolean contains(int x, int y);
    public boolean handlePress(int button);
    public boolean handleRelease(int button);
}
