package caffeinatedpinkie.tmel;

import java.util.function.Consumer;

import com.rwtema.extrautils2.blocks.BlockPassiveGenerator.GeneratorType;

/**
 * Contains common code used for {@link GeneratorType}s throughout the mod.
 *
 * @author CaffeinatedPinkie
 */
public class GeneratorHelper {
    /**
     * Applies a {@link Consumer} to all {@link GeneratorType}s.
     *
     * @param consumer a {@link Consumer} applied to all {@link GeneratorType}s
     */
    public static void forEachGenerator(Consumer<GeneratorType> consumer) {
	for (GeneratorType generator : GeneratorType.values())
	    consumer.accept(generator);
    }

    /**
     * Returns a {@link String} representing the given {@link GeneratorType}. This
     * is just so any changes to how {@link GeneratorType}s are stored in
     * {@link ConfigTMEL#gpRateMultipliers} are consistent.
     *
     * @param generator the {@link GeneratorType} to get the name of
     * @return a {@link String} representing the given {@link GeneratorType}
     */
    public static String getName(GeneratorType generator) {
	return generator.get().getDisplayName();
    }
}