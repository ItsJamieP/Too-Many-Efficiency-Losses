package caffeinatedpinkie.tmel.common;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.rwtema.extrautils2.blocks.BlockPassiveGenerator.GeneratorType;
import com.rwtema.extrautils2.power.IWorldPowerMultiplier;

import caffeinatedpinkie.tmel.ConfigTMEL;
import caffeinatedpinkie.tmel.GeneratorHelper;
import net.minecraft.world.World;

/**
 * Wrapper for {@link IWorldPowerMultiplier} using {@link InvocationHandler}.
 * Multiplies the GP production rate based on
 * {@link ConfigTMEL#gpRateMultipliers}.
 *
 * @author CaffeinatedPinkie
 */
public class IWPMWrapper implements InvocationHandler {
    public static Map<String, IWPMWrapper> proxyMap = new HashMap<>();
    public IWorldPowerMultiplier powerMultiplier;
    public float rateMultiplier;

    /**
     * Constructs this wrapper and sets {@link #powerMultiplier} to
     * {@link GeneratorType#powerMultiplier}. Also {@link Map#put(Object, Object)
     * puts} this object to {@link #proxyMap}.
     *
     * @param generator the associated {@link GeneratorType}
     */
    public IWPMWrapper(GeneratorType generator) {
	proxyMap.put(GeneratorHelper.getName(generator), this);
	powerMultiplier = generator.powerMultiplier;
    }

    /**
     * Invokes the given {@link Method} and {@link Object Object[]} args on the
     * {@link #powerMultiplier} object unless {@link Method#getName()}
     * {@link String#equals(Object) equals} "multiplier".
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
	return method.getName().equals("multiplier") ? multiplier((World) args[0]) : method.invoke(proxy, args);
    }

    /**
     * Multiplies {@link IWorldPowerMultiplier#multiplier(World)} from
     * {@link #powerMultiplier} by {@link #rateMultiplier}. This has the effect of
     * multiplying the GP production rate for the associated {@link GeneratorType}.
     *
     * @param world the {@link World} object the wrapped
     *              {@link IWorldPowerMultiplier} is in
     * @return the multiplied GP production rate
     */
    public float multiplier(@Nullable World world) {
	return powerMultiplier.multiplier(world) * rateMultiplier;
    }
}
