package cy.jdkdigital.dyenamics.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Cat.class)
public abstract class MixinCat extends TamableAnimal
{
    protected MixinCat(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
            method = "addAdditionalSaveData",
            at = {@At("RETURN")}
    )
    public void addAdditionalDyenamicSaveData(CompoundTag compound, CallbackInfo ci) {
        compound.putByte("CollarColor", (byte) (int)this.getEntityData().get(Cat.DATA_COLLAR_COLOR));
    }

    @Inject(
            method = "readAdditionalSaveData",
            at = {@At("RETURN")}
    )
    public void readAdditionalDyenamicSaveData(CompoundTag compound, CallbackInfo ci) {
        if (compound.contains("CollarColor", 99)) {
            this.getEntityData().set(Cat.DATA_COLLAR_COLOR, compound.getInt("CollarColor"));
        }
    }
}
