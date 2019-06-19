package caffeinatedpinkie.tmel.common.modifiers;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.tuple.Pair;

import com.rwtema.extrautils2.blocks.BlockPassiveGenerator.GeneratorType;

import caffeinatedpinkie.tmel.ConfigTMEL;
import caffeinatedpinkie.tmel.GeneratorHelper;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

/**
 * Disables efficiency loss for generators.
 *
 * @author CaffeinatedPinkie
 */
@ValueModifier.Annotation
public class EfficiencyLossDisabler extends ValueModifier {
    public Map<GeneratorType, TreeMap<Float, Pair<Float, Float>>> initCaps;

    public EfficiencyLossDisabler() {
	super("Efficiency loss disabler");
	initCaps = new HashMap<>();
    }

    /**
     * {@inheritDoc} Stores the normal energy caps in {@link #initCaps} from
     * generators which can be restored if needed.
     */
    @Override
    public void initiateModifier() {
	GeneratorHelper.forEachGenerator(generator -> initCaps.put(generator,
		ObfuscationReflectionHelper.getPrivateValue(GeneratorType.class, generator, "caps")));
    }

    /**
     * Either removes or restores the normal energy caps for generators based on
     * {@link ConfigTMEL#disableEL}.
     */
    @Override
    public void setValues() {
	GeneratorHelper.forEachGenerator(generator -> ObfuscationReflectionHelper.setPrivateValue(GeneratorType.class,
		generator, ConfigTMEL.disableEL ? null : initCaps.get(generator), "caps"));
    }
}
