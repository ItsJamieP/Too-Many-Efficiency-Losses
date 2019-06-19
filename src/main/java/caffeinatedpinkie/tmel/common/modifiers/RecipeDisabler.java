package caffeinatedpinkie.tmel.common.modifiers;

import java.util.HashMap;
import java.util.Map;

import com.rwtema.extrautils2.ExtraUtils2;

import caffeinatedpinkie.tmel.ConfigTMEL;
import caffeinatedpinkie.tmel.LoggerTMEL;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryModifiable;

/**
 * Disables {@link IRecipe}s added by Extra Utilities based on
 * {@link ConfigTMEL#defaultRecipes}.
 *
 * @author CaffeinatedPinkie
 */
@ValueModifier.Annotation
public class RecipeDisabler extends ValueModifier {
    public static Map<String, IRecipe> recipes = new HashMap<>();
    public static IForgeRegistryModifiable<IRecipe> registry = (IForgeRegistryModifiable<IRecipe>) ForgeRegistries.RECIPES;

    public RecipeDisabler() {
	super("Recipe Disabler");
    }

    /**
     * {@inheritDoc} Fills {@link #recipes} with any {@link IRecipe}s added to the
     * Minecraft Forge {@link IRecipe} registry at {@link ForgeRegistries#RECIPES}.
     */
    @Override
    public void initiateModifier() {
	ForgeRegistries.RECIPES.getEntries().forEach(entry -> {
	    if (entry.getKey().getNamespace().equals(ExtraUtils2.MODID))
		RecipeDisabler.recipes.put(entry.getValue().getRecipeOutput().getDisplayName(), entry.getValue());
	});
    }

    /**
     * Disables any {@link IRecipe}s in {@link #registry} if they are set to false
     * in {@link ConfigTMEL#defaultRecipes}.
     */
    @Override
    public void setValues() {
	if (!registry.isLocked())
	    ConfigTMEL.defaultRecipes.forEach((name, enabled) -> {
		if (!enabled) {
		    registry.remove(recipes.get(name).getRegistryName());
		    LoggerTMEL.log("Disabled the default recipe(s) for " + name + '.');
		}
	    });
    }

    /**
     * Whether {@link IRecipe}s can be removed or added to {@link #registry}.
     *
     * @return true if {@link #registry} is not locked via
     *         {@link IForgeRegistryModifiable#isLocked()}, false otherwise
     */
    @Override
    public boolean shouldSetValues() {
	return !registry.isLocked();
    }
}