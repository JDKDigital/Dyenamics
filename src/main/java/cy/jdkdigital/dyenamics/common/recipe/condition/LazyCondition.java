package cy.jdkdigital.dyenamics.common.recipe.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.common.conditions.FalseCondition;
import net.neoforged.neoforge.common.conditions.ICondition;

public record LazyCondition(ICondition value) implements ICondition {

    public static final MapCodec<LazyCondition> CODEC = RecordCodecBuilder.mapCodec((builder) -> {
        return builder.group(ICondition.CODEC.fieldOf("value").orElse(FalseCondition.INSTANCE).forGetter(LazyCondition::value)).apply(builder, LazyCondition::new);
    });

    public boolean test(IContext context) {
        return this.value.test(context);
    }

    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }

    public String toString() {
        return "lazy:" + this.value;
    }

    public ICondition value() {
        return this.value;
    }
}
