package io.github.a1qs.vaultutils.init;

import iskallia.vault.gear.attribute.VaultGearModifier;
import iskallia.vault.init.ModGearAttributes;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.tool.JewelItem;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ModCreativeTab {
    public static final CreativeModeTab VAULTUTILS_JEWEL_TAB = new CreativeModeTab("vaultutils_jewel_tab") {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(ModItems.JEWEL);
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> pItems) {
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.HAMMER_SIZE, 10));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
        }
    };
}
