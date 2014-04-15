package support;

/**
 * created by Faisal on 3/16/14 6:06 PM
 */
public abstract class Face<T> {
    public final T wrapped;

    public Face(T wrapped) {
        this.wrapped = wrapped;
    }

    public T get() {
        return wrapped;
    }
}
