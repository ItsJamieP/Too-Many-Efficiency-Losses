package caffeinatedpinkie.tmel.common;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.rwtema.extrautils2.power.PowerManager.PowerFreq;

import caffeinatedpinkie.tmel.LoggerTMEL;

/**
 * Subclass of {@link PowerFreq} that disables GP usage.
 *
 * @author CaffeinatedPinkie
 */
public class AlwaysPoweredPowerFreq extends PowerFreq {
    /**
     * Constructs an instance of this subclass. Copies non-static {@link Field}
     * values from the given {@link PowerFreq} to this object.
     *
     * @param frequency the frequency on which this {@link PowerFreq} subclass is
     *                  broadcasted
     * @param powerFreq a {@link PowerFreq} that all non-static {@link Field}s will
     *                  be copied from
     */
    public AlwaysPoweredPowerFreq(int frequency, PowerFreq powerFreq) {
	super(frequency);

	for (Field field : PowerFreq.class.getDeclaredFields())
	    if (!Modifier.isStatic(field.getModifiers())) {
		field.setAccessible(true);
		try {
		    field.set(this, field.get(powerFreq));
		} catch (IllegalAccessException e) {
		    LoggerTMEL.warn("Failed to set Field " + field.toString()
			    + " in PowerFreqExtender. This may cause problems and should be reported to the developer.",
			    e);
		}
	    }
    }

    /**
     * Calls {@link PowerFreq#changeStatus(boolean)} always with true. This tells
     * the machines on this frequency they have enough GP.
     */
    @Override
    public void changeStatus(boolean powered) {
	super.changeStatus(true);
    }
}
