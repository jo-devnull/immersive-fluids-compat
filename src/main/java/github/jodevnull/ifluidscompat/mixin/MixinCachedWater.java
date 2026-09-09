package github.jodevnull.ifluidscompat.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import github.jodevnull.ifluidscompat.IFluidsCompat;
import io.github.SirWashington.WaterPhysics;
import io.github.SirWashington.features.CachedWater;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static github.jodevnull.ifluidscompat.IFluidsCompat.WORLDGEN;

@Mixin(value = CachedWater.class, remap = false)
public abstract class MixinCachedWater
{
    @Shadow
    public static BlockState getBlockState(BlockPos pos) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    public static void setBlockStateNoNeighbors(BlockPos pos, BlockState oldState, BlockState state) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    public static Level world;

    @Unique
    private static boolean ifc$isNaturalSource(BlockPos pos) {
        return IFluidsCompat.isWorldgen(CachedWater.getBlockState(pos));
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    private static void setWaterLevelDirect(int level, BlockPos pos) {
        BlockState prev = getBlockState(pos);

        assert prev.isAir() || prev.hasProperty(WaterPhysics.WATER_LEVEL) || !prev.getFluidState().isEmpty() || level < 0;

        if (prev.hasProperty(WaterPhysics.WATER_LEVEL)) {
            setBlockStateNoNeighbors(pos, prev, prev.setValue(WaterPhysics.WATER_LEVEL, level));
        } else if (level == 0) {
            setBlockStateNoNeighbors(pos, prev, Blocks.AIR.defaultBlockState());
        } else if (level >= 0) {
            if (level <= 8) {
                if (level == 8) {
                    if (!(prev.getBlock() instanceof LiquidBlockContainer))
                        setBlockStateNoNeighbors(pos, prev, Blocks.WATER.defaultBlockState().setValue(WORLDGEN, false));
                } else {
                    if (!(prev.getBlock() instanceof BucketPickup))
                        world.destroyBlock(pos, true);

                    setBlockStateNoNeighbors(pos, prev, Fluids.FLOWING_WATER.getFlowing(level, false).createLegacyBlock().setValue(WORLDGEN, false));
                }
            }
        }
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
