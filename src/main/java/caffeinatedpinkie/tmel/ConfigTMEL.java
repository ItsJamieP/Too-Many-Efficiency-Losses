package caffeinatedpinkie.tmel;

import java.util.HashMap;
import java.util.Map;

import caffeinatedpinkie.tmel.common.modifiers.RecipeDisabler;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Configuration file for the mod.
 *
 * @author CaffeinatedPinkie
 */
@Config(modid = TMEL.MODID)
public class ConfigTMEL {
    /**
     * Handler for {@link ConfigTMEL} being updated through the in game UI.
     *
     * @author CaffeinatedPinkie
     */
    @Mod.EventBusSubscriber
    public static class EventHandler {
	/**
	 * Called when config values are changed. Refreshes what features are enabled in
	 * the mod.
	 *
	 * @param event the {@link OnConfigChangedEvent}
	 */
	@SubscribeEvent
	public static void onConfigChangedEvent(OnConfigChangedEvent event) {
	    if (event.getModID().equals(TMEL.MODID)) {
		ConfigManager.sync(TMEL.MODID, Type.INSTANCE);
		TMEL.refresh();
	    }
	}
    }

    @Name("Default Recipes")
    @Comment("Disable or enable any recipes added in by Extra Utilities. By disabling an item in the list, all recipes that are added by Extra Utilities to craft that item are disabled. The same is true for enabling one. Doing so requires a restart of the game.")
    @RequiresMcRestart
    public static Map<String, Boolean> defaultRecipes = new HashMap<>();

    @Name("Disable Efficiency Loss")
    @Comment("Enabling this disables efficiency loss in all generators.")
    public static boolean disableEL = true;

    @Name("Disable Grid Power")
    @Comment("Enabling this disables the use of GP in all machines and makes it impossible to overload the grid.")
    public static boolean disableGP;

    @Name("Grid Power Multipliers")
    @Comment("Multipliers for the GP production rates. All values must be at least 0 and less than " + Float.MAX_VALUE
	    + '.')
    public static Map<String, Float> gpRateMultipliers = new HashMap<>();

    @Name("Verbose?")
    @Comment("If true, debug messages will be sent to the client or server console.")
    public static boolean verbose;

    /**
     * Initializes {@link Map}s in this config file and then saves them to the disk
     * with {@link ConfigManager#sync(String, Config.Type)}.
     *
     * @param event the {@link FMLPostInitializationEvent}
     */
    public static void postInit(FMLPostInitializationEvent event) {
	LoggerTMEL.log(() -> {
	    RecipeDisabler.recipes.forEach((name, recipe) -> defaultRecipes.put(name, true));
	    GeneratorHelper
		    .forEachGenerator(generator -> gpRateMultipliers.put(GeneratorHelper.getName(generator), 1F));
	    ConfigManager.sync(TMEL.MODID, Type.INSTANCE);
	}, "Config setup");
    }
}
