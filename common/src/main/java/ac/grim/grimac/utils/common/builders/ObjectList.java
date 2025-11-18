package ac.grim.grimac.utils.common.builders;

public interface ObjectList<T> {

    boolean isEmpty();

    ObjectList<T> append(T object);

    T build();

}
