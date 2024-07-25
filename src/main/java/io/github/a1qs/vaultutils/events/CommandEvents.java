package io.github.a1qs.vaultutils.events;

import io.github.a1qs.vaultutils.VaultUtils;
import io.github.a1qs.vaultutils.commands.*;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

@Mod.EventBusSubscriber(modid = VaultUtils.MOD_ID)
public class CommandEvents {
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new VaultTimerCommands(event.getDispatcher());
        new KickCommands(event.getDispatcher());
        new TestCommands(event.getDispatcher());
        new CrystalCommands(event.getDispatcher());
        new CreatePortalCommands(event.getDispatcher());
        new UnlockArchiveModifiersCommands(event.getDispatcher());
        new VaultAltarCommands(event.getDispatcher());
        new ResetAbilityCooldownCommands(event.getDispatcher());
        new UnlockModelCommands(event.getDispatcher());
        new JewelCommands(event.getDispatcher());


        ConfigCommand.register(event.getDispatcher());
    }

}
