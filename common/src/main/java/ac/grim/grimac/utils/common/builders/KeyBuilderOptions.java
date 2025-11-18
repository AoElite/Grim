package ac.grim.grimac.utils.common.builders;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Builder(builderMethodName = "of", builderClassName = "Builder")
@AllArgsConstructor
@Getter
public class KeyBuilderOptions<T> {

    private int roundTo;
    private T sign;
    private T valueSeparator;
    private T listSeparator;
    private T listStart;
    private T listEnd;
    private T objectStart;
    private T objectEnd;

}
