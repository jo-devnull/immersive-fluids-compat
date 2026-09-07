package github.jodevnull.ifluidscompat;

import net.minecraftforge.common.ForgeConfigSpec;

public class IFCConfig
{
    public static final ForgeConfigSpec mSpec;
    public static final ForgeConfigSpec.Builder mBuilder = new ForgeConfigSpec.Builder();

    static {
        mSpec = mBuilder.build();
    }
}
