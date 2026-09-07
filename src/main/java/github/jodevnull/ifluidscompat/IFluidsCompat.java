package github.jodevnull.ifluidscompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import github.jodevnull.ifluidscompat.event.IFCPlayerEvents;
import github.jodevnull.ifluidscompat.recipe.ModRecipeSerializers;
import github.jodevnull.ifluidscompat.recipe.ModRecipeTypes;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(IFluidsCompat.MODID)
public class IFluidsCompat
{
    public static final String MODID = "immersive_fluids_compat";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public IFluidsCompat(FMLJavaModLoadingContext context) {
        final var modEventBus = context.getModEventBus();

        // context.registerConfig(ModConfig.Type.COMMON, IFCConfig.mSpec);

        IFCPlayerEvents.register();
        ModRecipeTypes.RECIPE_TYPES.register(modEventBus);
        ModRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
    }
}
