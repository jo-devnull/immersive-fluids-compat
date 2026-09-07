package github.jodevnull.ifluidscompat.recipe;

import github.jodevnull.ifluidscompat.IFluidsCompat;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipeTypes
{
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, IFluidsCompat.MODID);

    public static final Supplier<RecipeType<PickupWaterRecipe>> PICKUP_WATER = RECIPE_TYPES.register("pickup_water", () -> new RecipeType<>()
    {
        public String toString() {
            return IFluidsCompat.MODID + ":" + "pickup_water";
        }
    });
}
