package github.jodevnull.ifluidscompat.event;

import github.jodevnull.ifluidscompat.IFluidsCompat;
import github.jodevnull.ifluidscompat.recipe.PickupWaterHandler;
import io.github.SirWashington.features.CachedWater;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod.EventBusSubscriber
public class IFCPlayerEvents
{
    public static void register() {}

    @SubscribeEvent
    public static void waterContainerInteract(PlayerInteractEvent.RightClickBlock event)
    {
        if (event.getLevel().isClientSide())
            return;

        final var player = event.getEntity();
        final var hand = event.getHand();
        final var pos = event.getPos().relative(event.getHitVec().getDirection());
        final var state = event.getLevel().getBlockState(pos);

        // if (!FMLEnvironment.production) {
        //     if (player.getItemInHand(hand).isEmpty()) {
        //         player.sendSystemMessage(Component.literal(state.toString()));
        //     }
        // }

        if (!CachedWater.isWater(state) || IFluidsCompat.isNatural(state))
            return;

        if (PickupWaterHandler.handle((ServerLevel) event.getLevel(), player, hand, pos)) {
            event.setCanceled(true);
            event.cancel();
        }
    }
}
