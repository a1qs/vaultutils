package io.github.a1qs.vaultutils.init;

import iskallia.vault.gear.VaultGearRarity;
import iskallia.vault.gear.VaultGearState;
import iskallia.vault.gear.attribute.VaultGearModifier;
import iskallia.vault.gear.data.VaultGearData;
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

            // Durability
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.DURABILITY, 100));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.DURABILITY, 500));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.DURABILITY, 1000));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.DURABILITY, 2500));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.DURABILITY, 10000));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));

            // Copiously
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.COPIOUSLY, 0.01F));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.COPIOUSLY, 0.05F));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.COPIOUSLY, 0.25F));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.COPIOUSLY, 1.0F));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.COPIOUSLY, 2.5F));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));

            //Item Quantity
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.ITEM_QUANTITY, 0.01F));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.ITEM_QUANTITY, 0.05F));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.ITEM_QUANTITY, 0.25F));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.ITEM_QUANTITY, 1.0F));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.ITEM_QUANTITY, 2.5F));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));

            //Item Rarity
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.ITEM_RARITY, 0.01F));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.ITEM_RARITY, 0.05F));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.ITEM_RARITY, 0.25F));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.ITEM_RARITY, 1.0F));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.ITEM_RARITY, 2.5F));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));

            //Trap Disarming
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.TRAP_DISARMING, 0.01F));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.TRAP_DISARMING, 0.05F));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.TRAP_DISARMING, 0.25F));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.TRAP_DISARMING, 1.0F));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.TRAP_DISARMING, 2.5F));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));

            //Vanilla Immortality
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.IMMORTALITY, 0.01F));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.IMMORTALITY, 0.05F));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.IMMORTALITY, 0.25F));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.IMMORTALITY, 1.0F));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.IMMORTALITY, 2.5F));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));

            //Reach
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.REACH, 0.01));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.REACH, 0.05));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.REACH, 0.25));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.REACH, 1.0));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.REACH, 2.5));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));

            //Mining Speed
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.MINING_SPEED, 0.1F));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.MINING_SPEED, 2.5F));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.MINING_SPEED, 10.0F));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.MINING_SPEED, 50.0F));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.MINING_SPEED, 250.0F));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));

            //Hammer Size
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.HAMMER_SIZE, 1));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.HAMMER_SIZE, 3));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.HAMMER_SIZE, 5));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.HAMMER_SIZE, 7));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));

            //Tool Modifiers
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.PICKING, true));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.AXING, true));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.SHOVELLING, true));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.REAPING, true));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));

            //Affinities
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.WOODEN_AFFINITY, true));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.ORNATE_AFFINITY, true));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.GILDED_AFFINITY, true));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.LIVING_AFFINITY, true));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.COIN_AFFINITY, true));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));

            //Miscellaneous
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.SMELTING, true));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.PULVERIZING, true));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.HYDROVOID, true));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));
            pItems.add(JewelItem.create(data -> {
                data.addModifier(VaultGearModifier.AffixType.SUFFIX, new VaultGearModifier<>(ModGearAttributes.SOULBOUND, true));
                data.addModifier(VaultGearModifier.AffixType.IMPLICIT, new VaultGearModifier<>(ModGearAttributes.JEWEL_SIZE, 1));
            }));


            pItems.forEach(items -> {
                VaultGearData data2 = VaultGearData.read(items);
                data2.setRarity(VaultGearRarity.UNIQUE);
                data2.setState(VaultGearState.IDENTIFIED);
                data2.write(items);
            });
        }
    };
}
