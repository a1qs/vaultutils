package io.github.a1qs.vaultutils.mixins;

import io.github.a1qs.vaultutils.config.CommonConfigs;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.init.ModGearAttributes;
import iskallia.vault.init.ModItems;
import iskallia.vault.skill.ability.effect.spi.AbstractVeinMinerAbility;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(value = AbstractVeinMinerAbility.class, remap = false)
public class MixinAbstractVeinMiningAbility {

    @ModifyVariable(method = "onBlockMined", at = @At(value = "STORE", ordinal = 0), name = "gearData")
    private static VaultGearData modifyGearDataCheck(VaultGearData gearData, BlockEvent.BreakEvent event) {
        if(CommonConfigs.ENABLE_VEINMINING_HAMMER.get()) {
            if (gearData.has(ModGearAttributes.HAMMERING)) {
                return VaultGearData.read(new ItemStack(ModItems.TOOL));
            }
            return gearData;
        }
        return gearData;
    }
}
