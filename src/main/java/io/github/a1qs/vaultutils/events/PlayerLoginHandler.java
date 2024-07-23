package io.github.a1qs.vaultutils.events;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerLoginHandler {

    private static final Map<UUID, Integer> playerTickCounters = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        //store the player UUID
        playerTickCounters.put(event.getPlayer().getUUID(), 0);
    }

    public static Map<UUID, Integer> getPlayerTickCounters() {
        return playerTickCounters;
    }


}
