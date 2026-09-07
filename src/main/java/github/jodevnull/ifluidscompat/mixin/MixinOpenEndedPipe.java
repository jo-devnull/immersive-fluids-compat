package github.jodevnull.ifluidscompat.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.fluids.OpenEndedPipe;
import com.simibubi.create.foundation.fluid.FluidHelper;
import io.github.SirWashington.features.CachedWater;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OpenEndedPipe.class)
public abstract class MixinOpenEndedPipe
{
    @Shadow
    public abstract BlockPos getPos();

    @Shadow
    public abstract BlockPos getOutputPos();

    @Shadow
    private Level world;

    @Shadow
    private BlockPos outputPos;

    @ModifyExpressionValue(method = "removeFluidFromSpace", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;isSource()Z"))
    public boolean ifc_addFlowingWaterCheck(boolean original, @Local(name = "state") BlockState state) {
        if (CachedWater.isWater(state)) {
            return true;
        }

        return original;
    }

    @Inject(method = "removeFluidFromSpace", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;createLegacyBlock()Lnet/minecraft/world/level/block/state/BlockState;"), cancellable = true)
    public void ifc_removeFlowingWater(boolean simulate, CallbackInfoReturnable<FluidStack> cir, @Local(name = "fluidState") FluidState fluidState, @Local(name = "stack") FluidStack stack) {
        if (FluidHelper.isWater(stack.getFluid()) && !CachedWater.isInfinite(getOutputPos())) {
            final int waterLevel = CachedWater.getWaterLevel(getOutputPos());

             if (waterLevel == 8) {
                 world.setBlock(getOutputPos(), Blocks.AIR.defaultBlockState(), 3);
                 cir.setReturnValue(stack);
             }
        }
    }

    @ModifyExpressionValue(method = "provideFluidToSpace", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;isSource()Z"))
    public boolean ifc_isSourceCheck(boolean original, @Local(name = "state") BlockState state) {
        if (CachedWater.isWater(state) && !CachedWater.isInfinite(getOutputPos()))
            return false;

        return original;
    }

    @WrapOperation(method = "provideFluidToSpace", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z", ordinal = 1))
    public boolean ifc_placeWater(Level instance, BlockPos pos, BlockState state, int p_46603_, Operation<Boolean> original, @Local(name = "fluid") FluidStack fluid) {
        final int addedLevel = Math.floorDiv(fluid.getAmount() * 8, 1000);
        CachedWater.addWater(addedLevel, getOutputPos());
        return true;
    }
}
