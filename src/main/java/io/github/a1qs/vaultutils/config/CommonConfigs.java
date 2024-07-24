package io.github.a1qs.vaultutils.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_ETERNAL_MIXIN;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_VAULT_TIMER_FREEZE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_VEINMINING_HAMMER;
    public static final ForgeConfigSpec.ConfigValue<Integer> VAULT_TIMER_FREEZE_TIME;


    static {
        BUILDER.push("VaultUtils config");

        ENABLE_ETERNAL_MIXIN = BUILDER.comment("Enable the Eternal Mixin which causes eternals to never spawn")
                .define("ENABLE_ETERNAL_MIXIN", false);

        ENABLE_VEINMINING_HAMMER = BUILDER.comment("EXPERIMENTAL! Re-enables Veinmining while using a tool with Hammering on it.")
                .define("ENABLE_VEINMINING_HAMMER", false);

        ENABLE_VAULT_TIMER_FREEZE = BUILDER.comment("Enable the Vault Timer being frozen upon login")
                        .define("ENABLE_VAULT_TIMER_FREEZE", false);

        VAULT_TIMER_FREEZE_TIME = BUILDER
                .comment("How long the Vault Timer stays frozen upon login (in ticks)\nNote that this may be abused by multiple players logging out to stop the Vault Timer")
                .defineInRange("VAULT_FREEZE_TIME", 100, 1, 1200);



        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
