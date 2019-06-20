package caffeinatedpinkie.tmel;

import caffeinatedpinkie.tmel.common.modifiers.ValueModifier;
import caffeinatedpinkie.tmel.common.modifiers.ValueModifier.Annotation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * The base mod file for Too Many Efficiency Losses.
 *
 * @author CaffeinatedPinkie
 */
@Mod(modid = TMEL.MODID, version = TMEL.VERSION, useMetadata = true, acceptableRemoteVersions = "*")
public class TMEL {
    public static final String MODID = "tmel", VERSION = "1.12.2-1.4.0.0-beta3";

    /**
     * The main {@link FMLPostInitializationEvent} handler.
     *
     * @param event the {@link FMLPostInitializationEvent}
     */
    @EventHandler
    public static void postInit(FMLPostInitializationEvent event) {
	LoggerTMEL.log(() -> {
	    ValueModifier.postInitEarly(event);
	    ConfigTMEL.postInit(event);
	    ValueModifier.postInitLate(event);
	}, "Post-init completed");
    }

    /*
     * The main {@link FMLPreInitializationEvent} handler.
     *
     * @param event the {@link FMLPreInitializationEvent}
     */
    @EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
	event.getAsmData().getAll(Annotation.class.getName()).forEach(data -> ValueModifier.add(data));
    }

    /**
     * Refreshes any data that is generated at initialization.
     */
    public static void refresh() {
	LoggerTMEL.log(() -> ValueModifier.refresh(), "Refreshed mod");
    }
}
