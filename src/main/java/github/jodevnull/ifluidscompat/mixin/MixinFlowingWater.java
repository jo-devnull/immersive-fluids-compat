package github.jodevnull.ifluidscompat.mixin;

import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.WaterFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static github.jodevnull.ifluidscompat.IFluidsCompat.WORLDGEN;

@Mixin(WaterFluid.Flowing.class)
public class MixinFlowingWater
{
    @Inject(
        at = {@At("HEAD")},
        method = {"createFluidStateDefinition"}
    )
    protected void appendProperties(StateDefinition.Builder<Fluid, FluidState> builder, CallbackInfo Ci) {
        builder.add(WORLDGEN);
    }
}
