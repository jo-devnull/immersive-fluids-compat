package github.jodevnull.ifluidscompat.recipe;

import github.jodevnull.ifluidscompat.IFluidsCompat;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipeSerializers
{
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, IFluidsCompat.MODID);

    public static final Supplier<RecipeSerializer<?>> CUTTING =
        RECIPE_SERIALIZERS.register("pickup_water", PickupWaterRecipe.Serializer::new);
}
