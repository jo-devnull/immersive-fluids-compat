package github.jodevnull.ifluidscompat.event;

import github.jodevnull.ifluidscompat.IFluidsCompat;
import github.jodevnull.ifluidscompat.recipe.PickupWaterHandler;
import io.github.SirWashington.features.CachedWater;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class IFCPlayerEvents
{
    public static void register() {}

    @SubscribeEvent
    public static void glassBottleInteract(PlayerInteractEvent.RightClickBlock event)
    {
        if (event.getLevel().isClientSide())
            return;

        final var player = event.getEntity();
        final var hand = event.getHand();
        final var pos = event.getPos().relative(event.getHitVec().getDirection());
        final var blockState = event.getLevel().getBlockState(pos);

        if (CachedWater.isInfinite(pos) || !CachedWater.isWater(blockState))
            return;

        if (PickupWaterHandler.handle((ServerLevel) event.getLevel(), player, hand, pos)) {
            event.setCanceled(true);
            event.cancel();
        }
    }
}
