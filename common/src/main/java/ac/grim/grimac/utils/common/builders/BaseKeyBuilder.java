package ac.grim.grimac.utils.common.builders;

import ac.grim.grimac.utils.common.FormattingUtil;
import ac.grim.grimac.utils.math.GrimMath;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3i;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class BaseKeyBuilder<T> implements KeyBuilder<T> {

    protected BaseKeyBuilder(KeyBuilderOptions<T> options, ObjectList<T> builder) {
        this.options = options;
        this.currentSeparator = this.options.getValueSeparator();
        this.builder = builder;
    }

    private final KeyBuilderOptions<T> options;
    // current separator
    private T currentSeparator;

    private final ObjectList<T> builder;
    private boolean prefixNext = false;

    @Override
    public boolean isEmpty() {
        return builder.isEmpty();
    }

    public abstract T mapObject(Object object);

    @Override
    public KeyBuilder<T> object(Object object) {
        return object(object, currentSeparator);
    }

    private KeyBuilder<T> object(Object object, T separator) {
        if (object == null) return this;
        if (prefixNext) builder.append(separator);
        builder.append(mapObject(object));
        prefixNext = true;
        return this;
    }

    @Override
    public KeyBuilder<T> value(T comment) {
        if (comment == null) return this;
        return object(comment);
    }

    protected KeyBuilder<T> append(T key, Object object) {
        if (prefixNext) builder.append(currentSeparator);
        builder.append(key).append(options.getSign()).append(mapObject(object));
        prefixNext = true;
        return this;
    }

    @SafeVarargs
    protected final KeyBuilder<T> merge(T key, T... values) {
        if (prefixNext) builder.append(currentSeparator);
        builder.append(key).append(options.getSign());
        for (T value : values) builder.append(value);
        prefixNext = true;
        return this;
    }

    @Override
    public T build() {
        return builder.build();
    }

    @Override
    public <K> KeyBuilder<T> value(T key, K object) {
        return append(key, object);
    }

    @Override
    public KeyBuilder<T> number(T key, float value) {
        return number(key, (double) value);
    }

    @Override
    public KeyBuilder<T> number(T key, double value) {
        return append(key, options.getRoundTo() > 0 ? GrimMath.round(value, options.getRoundTo()) : value);
    }

    @Override
    public KeyBuilder<T> time(T key, long inMilliseconds) {
        return append(key, FormattingUtil.formatTime(inMilliseconds));
    }

    @Override
    public KeyBuilder<T> item(T key, ItemStack itemStack) {
        return append(key, itemStack != null ? itemStack.getType().getName().getKey() : "null");
    }

    @Override
    public KeyBuilder<T> conditional(boolean condition, T key) {
        if (condition) return append(key, true);
        return this;
    }

    @Override
    public KeyBuilder<T> conditional(boolean condition, T key, String value) {
        if (condition) return append(key, value);
        return this;
    }

    @Override
    public KeyBuilder<T> conditional(boolean condition, Consumer<KeyBuilder<T>> builder) {
        if (condition) builder.accept(this);
        return this;
    }

    @Override
    public <K, V> KeyBuilder<T> map(T key, Map<K, V> map) {
        return list(key, map.entrySet(), (builder, entry) -> append(mapObject(entry.getKey()), entry.getValue()), options.getObjectStart(), options.getObjectEnd());
    }

    @Override
    public <L> KeyBuilder<T> list(T key, Collection<L> collection) {
        return list(key, collection, KeyBuilder::object);
    }

    @Override
    public <L> KeyBuilder<T> list(T key, Collection<L> collection, BiConsumer<KeyBuilder<T>, L> consumer) {
        return list(key, collection, consumer, options.getListStart(), options.getListEnd());
    }

    protected <L> KeyBuilder<T> list(T key, Collection<L> collection, BiConsumer<KeyBuilder<T>, L> consumer, T start, T end) {
        if (collection != null && !collection.isEmpty()) {
            append(key, start);
            prefixNext = false;
            for (L value : collection) {
                this.currentSeparator = options.getListSeparator();
                consumer.accept(this, value);
            }
            this.currentSeparator = options.getValueSeparator();
            builder.append(end);
        }
        return this;
    }

    @Override
    public <L extends Optional<?>> KeyBuilder<T> optional(T key, L optional) {
        if (optional != null && optional.isPresent()) {
            return append(key, optional.get());
        }
        return this;
    }

    @Override
    public <L, O extends Optional<L>> KeyBuilder<T> optional(T key, O optional, Function<L, T> function) {
        if (optional != null && optional.isPresent()) {
            return append(key, function.apply(optional.get()));
        }
        return this;
    }

    @Override
    public <L> KeyBuilder<T> object(T key, L object, Function<L, KeyBuilder<T>> function) {
        if (object == null) return merge(key, options.getObjectStart(), options.getObjectEnd());
        return merge(key, options.getObjectStart(), function.apply(object).build(), options.getObjectEnd());
    }

    @Override
    public KeyBuilder<T> vector(T key, Vector3i vector) {
        return append(key, "{x=" + vector.getX() + ", y=" + vector.getY() + ", z=" + vector.getZ() + "}");
    }

    @Override
    public KeyBuilder<T> vector(T key, Vector3d vector) {
        return append(key, "{x=" + vector.getX() + ", y=" + vector.getY() + ", z=" + vector.getZ() + "}");
    }

}
