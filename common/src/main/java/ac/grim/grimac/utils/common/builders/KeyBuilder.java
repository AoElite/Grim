package ac.grim.grimac.utils.common.builders;

import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3i;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public interface KeyBuilder<T> {

    boolean isEmpty();

    KeyBuilder<T> object(Object object);

    KeyBuilder<T> value(T value);

    <K> KeyBuilder<T> value(T key, K object);

    KeyBuilder<T> number(T key, float value);

    KeyBuilder<T> number(T key, double value);

    KeyBuilder<T> time(T key, long inMilliseconds);

    KeyBuilder<T> item(T key, ItemStack itemStack);

    KeyBuilder<T> conditional(boolean condition, T key);

    KeyBuilder<T> conditional(boolean condition, T key, String value);

    KeyBuilder<T> conditional(boolean condition, Consumer<KeyBuilder<T>> builder);

    <K, V> KeyBuilder<T> map(T key, Map<K, V> map);

    <L> KeyBuilder<T> list(T key, Collection<L> collection);

    <L> KeyBuilder<T> list(T key, Collection<L> collection, BiConsumer<KeyBuilder<T>, L> consumer);

    <L extends Optional<?>> KeyBuilder<T> optional(T key, L optional);

    <L, O extends Optional<L>> KeyBuilder<T> optional(T key, O optional, Function<L, T> function);

    <L> KeyBuilder<T> object(T key, L object, Function<L, KeyBuilder<T>> function);

    KeyBuilder<T> vector(T key, Vector3i vector);

    KeyBuilder<T> vector(T key, Vector3d vector);

    T build();
}
