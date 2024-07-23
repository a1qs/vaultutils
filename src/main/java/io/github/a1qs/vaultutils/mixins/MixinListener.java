package io.github.a1qs.vaultutils.mixins;

import io.github.a1qs.vaultutils.util.VaultPauseManager;
import iskallia.vault.core.data.sync.SyncMode;
import iskallia.vault.core.event.CommonEvents;
import iskallia.vault.core.vault.ClassicPortalLogic;
import iskallia.vault.core.vault.Vault;
import iskallia.vault.core.vault.WorldManager;
import iskallia.vault.core.vault.player.Listener;

import iskallia.vault.core.vault.time.TickClock;
import iskallia.vault.core.world.storage.VirtualWorld;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.network.message.VaultMessage;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkDirection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = Listener.class, remap = false)
public class MixinListener {

    /**
     * @author a1qs
     * @reason make work with commands
     */
    @Overwrite
    public void tickServer(VirtualWorld world, Vault vault) {
        Listener thisListener = (Listener) (Object) this;

        CommonEvents.LISTENER_TICK.invoke(vault, thisListener, world);
        thisListener.getPlayer().ifPresent((player) -> {
            if ((vault.get(Vault.CLOCK)).has(TickClock.PAUSED) && !VaultPauseManager.timerIsPausedManually(vault)) {
                vault.ifPresent(Vault.WORLD, (manager) -> {
                    Object patt3325$temp = manager.get(WorldManager.PORTAL_LOGIC);
                    if (patt3325$temp instanceof ClassicPortalLogic logic) {
                        if (logic.getPlayerStartPos(vault).map((start) -> {
                            return player.level.dimension().equals(world.dimension()) && player.distanceToSqr(Vec3.atCenterOf(start)) > 225.0;
                        }).orElse(false)) {
                            (vault.get(Vault.CLOCK)).remove(TickClock.PAUSED);
                        }
                    }
                });
            }

            ModNetwork.CHANNEL.sendTo(new VaultMessage.Sync(player, vault, SyncMode.FULL), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        });
    }



}
