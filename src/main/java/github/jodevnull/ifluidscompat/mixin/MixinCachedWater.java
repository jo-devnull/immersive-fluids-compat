package github.jodevnull.ifluidscompat.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import github.jodevnull.ifluidscompat.IFluidsCompat;
import io.github.SirWashington.features.CachedWater;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static github.jodevnull.ifluidscompat.IFluidsCompat.WORLDGEN;

@Mixin(value = CachedWater.class, remap = false)
public class MixinCachedWater
{
    @Unique
    private static boolean ifc$isNaturalSource(BlockPos pos) {
        return IFluidsCompat.isWorldgen(CachedWater.getBlockState(pos));
    }

    @WrapOperation(method = "setWaterLevelDirect", at= @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;defaultBlockState()Lnet/minecraft/world/level/block/state/BlockState;", ordinal = 1))
    private static BlockState ifc$setWaterLevelDirect(Block instance, Operation<BlockState> original) {
        return Blocks.WATER.defaultBlockState().setValue(WORLDGEN, false);
    }

    @WrapOperation(method = "setWaterLevelDirect", at= @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;createLegacyBlock()Lnet/minecraft/world/level/block/state/BlockState;"))
    private static BlockState ifc$setWaterLevelDirect2(FluidState instance, Operation<BlockState> original) {
        return original.call(instance).setValue(WORLDGEN, false);
    }

    @WrapMethod(method = "isInfinite")
    private static boolean ifc$isInfinite(BlockPos pos, Operation<Boolean> original) {
        return ifc$isNaturalSource(pos) || original.call(pos);
    }

    @Inject(method = "setWaterLevel", at=@At("HEAD"), cancellable = true)
    private static void ifc$setWaterLevel(int level, BlockPos pos, CallbackInfo ci) {
        if (ifc$isNaturalSource(pos)) {
            ci.cancel();
        }
    }

    @Inject(method = "addWater", at=@At("HEAD"), cancellable = true)
    private static void ifc$addWater(int level, BlockPos pos, CallbackInfo ci) {
        if (ifc$isNaturalSource(pos)) {
            ci.cancel();
        }
    }
}
