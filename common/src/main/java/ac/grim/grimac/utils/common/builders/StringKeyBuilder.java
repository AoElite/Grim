package ac.grim.grimac.utils.common.builders;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class StringKeyBuilder extends BaseKeyBuilder<String> {

    protected StringKeyBuilder(KeyBuilderOptions<String> options, ObjectList<String> builder) {
        super(options, builder);
    }

    public static KeyBuilderOptions<String> options(Consumer<KeyBuilderOptions.Builder<String>> options) {
        var builder = KeyBuilderOptions.<String>of()
                .roundTo(0).sign("=")
                .valueSeparator(", ").listSeparator(",")
                .listStart("[").listEnd("]")
                .objectStart("{").objectEnd("}");
        options.accept(builder);
        return builder.build();
    }

    @Override
    public KeyBuilder<String> value(String comment) {
        if (comment == null || comment.isBlank()) return this;
        return super.value(comment);
    }

    public static final KeyBuilderOptions<String> DEFAULT_OPTIONS = options(builder -> {

    });

    protected static final Supplier<ObjectList<String>> BUILDER_SUPPLIER = new Supplier<>() {
        @Override
        public ObjectList<String> get() {
            return new ObjectList<>() {

                private final StringBuilder sb = new StringBuilder();

                @Override
                public boolean isEmpty() {
                    return sb.isEmpty();
                }

                @Override
                public ObjectList<String> append(String object) {
                    sb.append(object);
                    return this;
                }

                @Override
                public String build() {
                    return sb.toString();
                }
            };
        }
    };

    public static BaseKeyBuilder<String> of(KeyBuilderOptions<String> options) {
        return new StringKeyBuilder(options, BUILDER_SUPPLIER.get());
    }

    public static BaseKeyBuilder<String> of() {
        return new StringKeyBuilder(DEFAULT_OPTIONS, BUILDER_SUPPLIER.get());
    }

    @Override
    public String mapObject(Object object) {
        return String.valueOf(object);
    }
}
