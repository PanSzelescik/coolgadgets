package panszelescik.coolgadgets.helper;

import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.cyclops.commoncapabilities.api.capability.wrench.DefaultWrench;
import org.cyclops.cyclopscore.modcompat.capabilities.DefaultCapabilityProvider;
import org.cyclops.integrateddynamics.Capabilities;

public class IntegratedDynamicsHelper {

    public static ICapabilityProvider capability() {
        return new DefaultCapabilityProvider<>(() -> Capabilities.WRENCH, new DefaultWrench());
    }
}