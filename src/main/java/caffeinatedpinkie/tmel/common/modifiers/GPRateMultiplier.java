package caffeinatedpinkie.tmel.common.modifiers;

import java.lang.reflect.Proxy;

import com.rwtema.extrautils2.blocks.BlockPassiveGenerator.GeneratorType;
import com.rwtema.extrautils2.power.IWorldPowerMultiplier;

import caffeinatedpinkie.tmel.ConfigTMEL;
import caffeinatedpinkie.tmel.GeneratorHelper;
import caffeinatedpinkie.tmel.common.IWPMWrapper;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

/**
 * Sets the individual GP rates for each generator.
 *
 * @author CaffeinatedPinkie
 */
@ValueModifier.Annotation
public class GPRateMultiplier extends ValueModifier {
    public GPRateMultiplier() {
	super("GP rate multiplier");
    }

    /**
     * {@inheritDoc} Replaces {@link GeneratorType#powerMultiplier} for every
     * generator with the wrapper class, {@link IWPMWrapper}, which allows a
     * multiplication rate to be set.
     */
    @Override
    public void initiateModifier() {
	GeneratorHelper.forEachGenerator(
		generator -> ObfuscationReflectionHelper.setPrivateValue(GeneratorType.class, generator,
			Proxy.newProxyInstance(IWPMWrapper.class.getClassLoader(),
				new Class[] { IWorldPowerMultiplier.class }, new IWPMWrapper(generator)),
			"powerMultiplier"));
    }

    /**
     * Updates {@link IWPMWrapper#rateMultiplier} to the current configuration
     * values at {@link ConfigTMEL#gpRateMultipliers}.
     */
    @Override
    public void setValues() {
	ConfigTMEL.gpRateMultipliers.forEach((generatorName,
		rateMultiplier) -> IWPMWrapper.proxyMap.get(generatorName).rateMultiplier = rateMultiplier);
    }
}