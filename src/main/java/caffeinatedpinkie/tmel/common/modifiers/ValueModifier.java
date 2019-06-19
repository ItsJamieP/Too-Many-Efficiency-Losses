package caffeinatedpinkie.tmel.common.modifiers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import caffeinatedpinkie.tmel.ConfigTMEL;
import caffeinatedpinkie.tmel.LoggerTMEL;
import caffeinatedpinkie.tmel.TMEL;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

/**
 * Generic class that is extended by all classes that modify Extra Utilities or
 * in game values.
 *
 * @author CaffeinatedPinkie
 */
public abstract class ValueModifier {
    /**
     * Used for all classes that extend {@link ValueModifier}. Apply this annotation
     * to the entire class object. The target MUST extend {@link ValueModifier} and
     * have a default constructor.
     *
     * @author CaffeinatedPinkie
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public static @interface Annotation {
    }

    public static List<ValueModifier> modifiers = new ArrayList<>();

    /**
     * Adds the class in the given {@link ASMData} that is annotated with
     * {@link Annotation} and extends {@link ValueModifier}.
     *
     * @param data {@link ASMData} for a class annotated with {@link Annotation}
     */
    public static void add(ASMData data) {
	String className = data.getClassName();
	try {
	    modifiers.add((ValueModifier) Class.forName(className).newInstance());
	} catch (ClassCastException e) {
	    LoggerTMEL.warn(
		    "Annotated class " + className + " does not extend " + ValueModifier.class.getSimpleName() + '!',
		    e);
	} catch (InstantiationException e) {
	    LoggerTMEL.warn("Couldn't create an instance of " + className + " due to it missing a default constructor.",
		    e);
	} catch (Exception e) {
	    LoggerTMEL.warn("Unable to add modifier class to the load list: " + className + ".", e);
	}
    }

    /**
     * Calls {@link ValueModifier#initateModifierInternal()} on all elements of
     * {@link #modifiers}.<br>
     * <br>
     * Runs at the start of {@link TMEL#postInit(FMLPostInitializationEvent)} before
     * anything else.
     *
     * @param event the {@link FMLPostInitializationEvent}
     */
    public static void postInitEarly(FMLPostInitializationEvent event) {
	modifiers.forEach(modifier -> modifier.initateModifierInternal());
    }

    /**
     * Calls {@link #refresh()}. Sort of a placeholder for if/when more code is
     * needed here that shouldn't be run during a mod refresh.<br>
     * <br>
     * Runs at the end of {@link TMEL#postInit(FMLPostInitializationEvent)} after
     * everything else.
     *
     * @param event the {@link FMLPostInitializationEvent}
     */
    public static void postInitLate(FMLPostInitializationEvent event) {
	refresh();
    }

    /**
     * Calls {@link ValueModifier#setValuesInternal()} on all elements of
     * {@link #modifiers}.
     */
    public static void refresh() {
	modifiers.forEach(modifier -> modifier.setValuesInternal());
    }

    public String name;

    public ValueModifier(String name) {
	this.name = name;
    }

    /**
     * Calls {@link #initiateModifier()} and logs the time elapsed. This method
     * shouldn't be extended by child classes.
     */
    public void initateModifierInternal() {
	LoggerTMEL.log(() -> initiateModifier(), "Initiated " + name);
    }

    /**
     * Runs any code needed to later execute {@link #setValues()}.
     */
    public abstract void initiateModifier();

    /**
     * Sets values in Extra Utilities or Minecraft classes based on
     * {@link ConfigTMEL}.
     */
    public abstract void setValues();

    /**
     * Calls {@link #setValues()} and logs the time elapsed. This method shouldn't
     * be extended by child classes.
     */
    public void setValuesInternal() {
	if (shouldSetValues())
	    LoggerTMEL.log(() -> setValues(), "Set " + name + " values");
    }

    /**
     * Whether to execute {@link #setValues()} for this {@link ValueModifier}
     * whenever {@link #setValuesInternal()} is called. Currently used for
     * subclasses that should not be called during a mod refresh, however it can
     * also be used to have subclasses only be called during a mod refresh.
     *
     * @return true if {@link #setValues()} should be called at the time this method
     *         is called, false otherwise
     */
    public boolean shouldSetValues() {
	return true;
    }
}
