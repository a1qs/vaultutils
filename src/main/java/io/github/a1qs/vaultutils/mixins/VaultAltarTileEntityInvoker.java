package io.github.a1qs.vaultutils.mixins;

import iskallia.vault.altar.AltarInfusionRecipe;
import iskallia.vault.block.entity.VaultAltarTileEntity;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(VaultAltarTileEntity.class)
public interface VaultAltarTileEntityInvoker {
    @Invoker("resetAltar")
    void invokeResetAltar(ServerLevel world);

    @Invoker("updateDisplayedIndex")
    void invokeUpdateDisplayedIndex(AltarInfusionRecipe infusionRecipe);
}
