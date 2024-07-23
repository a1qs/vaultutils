package io.github.a1qs.vaultutils;

import com.mojang.logging.LogUtils;
import io.github.a1qs.vaultutils.config.CommonConfigs;
import io.github.a1qs.vaultutils.events.PlayerLoginHandler;
import io.github.a1qs.vaultutils.events.PlayerTickEvent;
import io.github.a1qs.vaultutils.init.ModCreativeTab;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(VaultUtils.MOD_ID)
public class VaultUtils {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "vaultutils";
    public VaultUtils() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.register(ModCreativeTab.VAULTUTILS_JEWEL_TAB);
        eventBus.addListener(this::setup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfigs.SPEC, "vaultutils-common.toml");
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(PlayerLoginHandler::onPlayerLogin);
        MinecraftForge.EVENT_BUS.addListener(PlayerTickEvent::onPlayerTick);

    }


    private void setup(final FMLCommonSetupEvent event) {
    }


    //todo: make arguments based on players for console execution
    //LEvel cap command
    //modifier archive thing
    //config for disabling entity radar and map block -> IntegrationWorldMap
    //config for changing reach cap
    //modify crystal attributes
    //reset cooldown for a player
    //unlock speific gear model command
    //set specific jewel size
    //add specific modifiers to gear command (god help me)
    //reroll vault altar recipe command
    //add vault portal command
    //tp to cake
    //pauseAll vaults
}
