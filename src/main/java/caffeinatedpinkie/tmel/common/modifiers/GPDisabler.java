package caffeinatedpinkie.tmel.common.modifiers;

import com.rwtema.extrautils2.power.PowerManager;
import com.rwtema.extrautils2.power.PowerManager.PowerFreq;

import caffeinatedpinkie.tmel.ConfigTMEL;
import caffeinatedpinkie.tmel.common.AlwaysPoweredPowerFreq;
import gnu.trove.map.hash.TIntObjectHashMap;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

/**
 * Disables GP for generators.
 *
 * @author CaffeinatedPinkie
 */
@ValueModifier.Annotation
public class GPDisabler extends ValueModifier {
    public TIntObjectHashMap<PowerFreq> initFrequencyHolders;

    public GPDisabler() {
	super("GP disabler");
    }

    /**
     * {@inheritDoc} Stores the normal {@link PowerManager#frequencyHolders} in
     * {@link #initFrequencyHolders} to be restored if needed.
     */
    @Override
    public void initiateModifier() {
	initFrequencyHolders = PowerManager.instance.frequencyHolders;
    }

    /**
     * Replaces or restores the normal {@link PowerManager#frequencyHolders} with
     * {@link #initFrequencyHolders} based on {@link ConfigTMEL#disableGP}. The new
     * {@link PowerManager#frequencyHolders} stores added values to
     * {@link #initFrequencyHolders} and replaces the {@link PowerFreq} with a
     * {@link AlwaysPoweredPowerFreq}.
     */
    @Override
    public void setValues() {
	ObfuscationReflectionHelper.setPrivateValue(PowerManager.class, PowerManager.instance,
		ConfigTMEL.disableGP ? new TIntObjectHashMap<PowerFreq>(PowerManager.instance.frequencyHolders) {
		    @Override
		    public PowerFreq put(int key, PowerFreq value) {
			initFrequencyHolders.put(key, value);
			return super.put(key, new AlwaysPoweredPowerFreq(key, value));
		    }
		} : initFrequencyHolders, "frequencyHolders");
	PowerManager.instance.reassignValues();
    }
}
