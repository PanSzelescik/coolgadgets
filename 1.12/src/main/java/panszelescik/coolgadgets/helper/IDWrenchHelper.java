package panszelescik.coolgadgets.helper;

import org.cyclops.commoncapabilities.api.capability.wrench.DefaultWrench;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityProvider;
import org.cyclops.integrateddynamics.Capabilities;

import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class IDWrenchHelper {
	
	public static ICapabilityProvider capability() {
		return new DefaultCapabilityProvider<>(() -> Capabilities.WRENCH, new DefaultWrench());
	}
}