package ac.grim.grimac.utils.common.builders;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ComponentKeyBuilder extends BaseKeyBuilder<Component> {

    protected ComponentKeyBuilder(KeyBuilderOptions<Component> options, ObjectList<Component> builder) {
        super(options, builder);
    }

    public static KeyBuilderOptions<Component> options(Consumer<KeyBuilderOptions.Builder<Component>> options) {
        var builder = KeyBuilderOptions.<Component>of()
                .roundTo(0).sign(Component.text("="))
                .valueSeparator(Component.text(", ")).listSeparator(Component.text(","))
                .listStart(Component.text("[")).listEnd(Component.text("]"))
                .objectStart(Component.text("{")).objectEnd(Component.text("}"));
        options.accept(builder);
        return builder.build();
    }

    public static final KeyBuilderOptions<Component> DEFAULT_OPTIONS = options(builder -> {

    });

    @Override
    public Component mapObject(Object object) {
        return MAPPER.apply(object);
    }

    private static final Function<Object, Component> MAPPER = o -> {
        if (o instanceof Component) return (Component) o;
        return Component.text(String.valueOf(o.toString()));
    };

    private static final Supplier<ObjectList<Component>> BUILDER_SUPPLIER = new Supplier<>() {
        @Override
        public ObjectList<Component> get() {
            return new ObjectList<>() {

                private final TextComponent.Builder builder = Component.text();

                @Override
                public boolean isEmpty() {
                    return builder.children().isEmpty();
                }

                @Override
                public ObjectList<Component> append(Component object) {
                    builder.append(object);
                    return this;
                }

                @Override
                public Component build() {
                    return builder.build();
                }
            };
        }
    };

    public static BaseKeyBuilder<Component> of(KeyBuilderOptions<Component> options) {
        return new ComponentKeyBuilder(options, BUILDER_SUPPLIER.get());
    }

    public static ComponentKeyBuilder of() {
        return new ComponentKeyBuilder(DEFAULT_OPTIONS, BUILDER_SUPPLIER.get());
    }

    public static ComponentKeyBuilder from(KeyBuilderOptions<Component> options) {
        return new ComponentKeyBuilder(options, BUILDER_SUPPLIER.get());
    }

}
