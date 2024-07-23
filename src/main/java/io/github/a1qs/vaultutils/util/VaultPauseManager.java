package io.github.a1qs.vaultutils.util;

import iskallia.vault.core.vault.Vault;

import java.util.WeakHashMap;

public class VaultPauseManager {
    private static final WeakHashMap<Vault, Boolean> pausedManuallyMap = new WeakHashMap<>();

    public static boolean timerIsPausedManually(Vault vault) {
        return pausedManuallyMap.getOrDefault(vault, false);
    }

    public static void vaultTimerManuallyPaused(Vault vault, boolean paused) {
        pausedManuallyMap.put(vault, paused);
    }
}
