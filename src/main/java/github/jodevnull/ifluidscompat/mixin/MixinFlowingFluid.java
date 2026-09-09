package github.jodevnull.ifluidscompat.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import github.jodevnull.ifluidscompat.IFluidsCompat;
import io.github.SirWashington.FlowWater;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlowingFluid.class)
public class MixinFlowingFluid
{
    @Inject(
        at = {@At("HEAD")},
        method = {"spread"},
        cancellable = true
    )
    private void tryFlow(Level world, BlockPos fluidPos, FluidState state, CallbackInfo bruh) {
        if (ifc$isWater(state.getType()) && !IFluidsCompat.isWorldgen(world.getBlockState(fluidPos))) {
            FlowWater.flowWater(world, fluidPos, state);
            bruh.cancel();
        }
    }

    @Inject(
        at = {@At("HEAD")},
        method = {"getNewLiquid"},
        cancellable = true
    )
    private void getUpdatedState(Level world, BlockPos pos, BlockState state, CallbackInfoReturnable<FluidState> bruh) {
        FluidState fluidstate = state.getFluidState();
        if (ifc$isWater(fluidstate.getType()) && !IFluidsCompat.isWorldgen(world.getBlockState(pos))) {
            bruh.setReturnValue(Fluids.FLOWING_WATER.getFlowing(state.getFluidState().getAmount(), false));
        }
    }

    @WrapMethod(method = "getFlow")
    public Vec3 ifc$getFlow(BlockGetter world, BlockPos pos, FluidState state, Operation<Vec3> original) {
        if (!IFluidsCompat.isWorldgen(world.getBlockState(pos)))
            return Vec3.ZERO;
        else
            return original.call(world, pos, state);
    }

    @Inject(method = "canConvertToSource(Lnet/minecraft/world/level/material/FluidState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z", at=@At("HEAD"), cancellable = true, remap = false)
    private void ifc$canConvertToSource(FluidState state, Level level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(IFluidsCompat.isWorldgen(level.getBlockState(pos)));
    }

    @Unique
    public boolean ifc$isWater(Fluid fluid) {
        return fluid == Fluids.WATER || fluid == Fluids.FLOWING_WATER;
    }
}