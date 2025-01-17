package raltsmc.mobblindness.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import raltsmc.mobblindness.MBMod;

@Mixin(LivingEntity.class)
public class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(
            method = "getAttackDistanceScalingFactor",
            at = @At(
                    value = "RETURN"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    public void getAttackDistanceScalingFactor(@Nullable Entity entity, CallbackInfoReturnable<Double> info, double d) {
        if (entity instanceof LivingEntity livingEntity && !this.world.isClient()) {
            if (livingEntity.hasStatusEffect(StatusEffects.BLINDNESS)) {
                int amplifier = livingEntity.getStatusEffect(StatusEffects.BLINDNESS).getAmplifier();
                for (int i = 0; i < amplifier + 1; i++) {
                    d *= (MBMod.CONFIG.rangePercentPerLevel / 100d);
                }
            }
        }
        info.setReturnValue(d);
    }

    @Shadow
    @Override
    protected void initDataTracker() {

    }

    @Shadow
    @Override
    public void readCustomDataFromNbt(NbtCompound tag) {

    }

    @Shadow
    @Override
    public void writeCustomDataToNbt(NbtCompound tag) {

    }
}
