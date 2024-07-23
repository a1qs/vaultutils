package io.github.a1qs.vaultutils.events;

import io.github.a1qs.vaultutils.config.CommonConfigs;
import io.github.a1qs.vaultutils.util.VaultPauseManager;
import iskallia.vault.core.vault.Vault;
import iskallia.vault.core.vault.time.TickClock;
import iskallia.vault.world.data.ServerVaults;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;
import java.util.UUID;

public class PlayerTickEvent {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        //Check if the config option has been enabled
        if (CommonConfigs.ENABLE_VAULT_TIMER_FREEZE.get()) {
            if(event.phase == TickEvent.Phase.END) {
                UUID playerUUID = event.player.getUUID();
                Map<UUID, Integer> playerTickCounters = PlayerLoginHandler.getPlayerTickCounters();

                //Check if player is in a valid vault then get the timer instance
                ServerVaults.get(event.player.level).ifPresent((vault) -> {
                    vault.ifPresent(Vault.CLOCK, (clock) -> {
                        if(playerTickCounters.containsKey(playerUUID)) {
                            int counter = playerTickCounters.get(playerUUID);

                            //stop the vault timer for the configured time & increment the tick counter
                            if (counter < CommonConfigs.VAULT_TIMER_FREEZE_TIME.get()) {
                                playerTickCounters.put(playerUUID, counter + 1);
                                if (!clock.has(TickClock.PAUSED)) {
                                    clock.set(TickClock.PAUSED);
                                    VaultPauseManager.vaultTimerManuallyPaused(vault, true);
                                    event.player.sendMessage(new TextComponent("Timer has been paused").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC), Util.NIL_UUID);
                                }
                            }
                        } else {
                            //remove the TickClock.PAUSED field key after the tick counter is beyond the configured time
                            vault.get(Vault.CLOCK).remove(TickClock.PAUSED);
                            event.player.sendMessage(new TextComponent("Timer has been unpaused").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC), Util.NIL_UUID);
                            VaultPauseManager.vaultTimerManuallyPaused(vault, false);

                            //remove the player from the map
                            playerTickCounters.remove(playerUUID);
                        }
                    });
                });
            }
        }
    }
}
