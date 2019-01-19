package panszelescik.coolgadgets.items;

import panszelescik.coolgadgets.CoolGadgets;
import panszelescik.morelibs.register.Register;
import panszelescik.morelibs.register.Register.RegisterItem;

@Register(modid = CoolGadgets.MODID)
public class ModItems {

    @RegisterItem(registryName = "calculator")
    public static ItemCalculator calculator;

    @RegisterItem(registryName = "lunette")
    public static ItemLunette lunette;

    @RegisterItem(registryName = "superwrench")
    public static ItemSuperWrench superWrench;
}