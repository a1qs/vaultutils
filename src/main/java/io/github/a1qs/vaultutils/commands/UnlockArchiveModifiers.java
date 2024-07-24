package io.github.a1qs.vaultutils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import iskallia.vault.config.AlchemyTableConfig;
import iskallia.vault.config.gear.VaultGearWorkbenchConfig;
import iskallia.vault.gear.VaultGearRarity;
import iskallia.vault.gear.VaultGearState;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.gear.item.VaultGearItem;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.world.data.DiscoveredAlchemyEffectsData;
import iskallia.vault.world.data.DiscoveredWorkbenchModifiersData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.stream.Collectors;

public class UnlockArchiveModifiers {
    private static final List<ItemStack> GEAR_ITEMS = List.of(
            new ItemStack(ModItems.SWORD),
            new ItemStack(ModItems.AXE),
            new ItemStack(ModItems.BOOTS),
            new ItemStack(ModItems.LEGGINGS),
            new ItemStack(ModItems.CHESTPLATE),
            new ItemStack(ModItems.HELMET),
            new ItemStack(ModItems.MAGNET),
            new ItemStack(ModItems.SHIELD),
            new ItemStack(ModItems.WAND),
            new ItemStack(ModItems.FOCUS),
            new ItemStack(ModItems.IDOL_BENEVOLENT),
            new ItemStack(ModItems.IDOL_TIMEKEEPER),
            new ItemStack(ModItems.IDOL_MALEVOLENCE),
            new ItemStack(ModItems.IDOL_OMNISCIENT)

    );

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_CRAFTING_MODIFIERS = (context, builder) ->
            SharedSuggestionProvider.suggestResource(ModConfigs.VAULT_GEAR_WORKBENCH_CONFIG.values().stream()
                    .flatMap(parentConfig -> parentConfig.getAllCraftableModifiers().stream())
                    .map(VaultGearWorkbenchConfig.CraftableModifierConfig::getWorkbenchCraftIdentifier)
                    .collect(Collectors.toList()), builder);

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_POTION_CRAFTING_MODIFIERS = (context, builder) ->
        SharedSuggestionProvider.suggest(
                ModConfigs.VAULT_ALCHEMY_TABLE.getCraftableEffects().stream()
                        .map(AlchemyTableConfig.CraftableEffectConfig::getEffectId)
                        .collect(Collectors.toList()), builder);

    public static final SuggestionProvider<CommandSourceStack> SUGGEST_CRAFTING_GEAR = (context, builder) -> {
        List<ResourceLocation> itemIdentifiers = GEAR_ITEMS.stream()
                .map(itemStack -> itemStack.getItem().getRegistryName())
                .toList();

        return SharedSuggestionProvider.suggestResource(itemIdentifiers.stream(), builder);
    };




    public UnlockArchiveModifiers(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("vaultutils")
                .requires(sender -> sender.hasPermission(this.getRequiredPermissionLevel()))
                .then(Commands.literal("unlockArchiveModifiers")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.literal("vaultGear")
                                        .then(Commands.argument("modifierId", ResourceLocationArgument.id()).suggests(SUGGEST_CRAFTING_MODIFIERS)
                                                .then(Commands.argument("vaultGearType", ResourceLocationArgument.id())
                                                        .suggests(SUGGEST_CRAFTING_GEAR)
                                                        .executes(context -> {
                                                            ResourceLocation itemId = ResourceLocationArgument.getId(context, "vaultGearType");
                                                            ResourceLocation modifierId = ResourceLocationArgument.getId(context, "modifierId");
                                                            return unlockCraftingModifiers(context, itemId, modifierId);
                                                        })
                                                )
                                        )
                                )
                                .then(Commands.literal("potion")
                                        .then(Commands.argument("effectId", StringArgumentType.string()).suggests(SUGGEST_POTION_CRAFTING_MODIFIERS)
                                            .executes(context -> {
                                                String modifierId = StringArgumentType.getString(context, "effectId");
                                                return unlockPotionCraftingModifiers(context, modifierId);
                                            })
                                        )
                                )
                        )
                )
        );

    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    private int unlockCraftingModifiers(CommandContext<CommandSourceStack> context, ResourceLocation itemId, ResourceLocation modifierId) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        DiscoveredWorkbenchModifiersData discoveredModifiers = DiscoveredWorkbenchModifiersData.get(context.getSource().getServer());
        Item gearItem = ForgeRegistries.ITEMS.getValue(itemId);

        if(gearItem == Items.AIR) {
            context.getSource().sendFailure(new TextComponent("Invalid item id for vaultGearType: " + itemId));
            return 1;
        }


        VaultGearWorkbenchConfig.getConfig(gearItem).ifPresent((cfg) -> {
            VaultGearWorkbenchConfig.CraftableModifierConfig modifierCfg = cfg.getConfig(modifierId);

            if (modifierCfg != null) {
                modifierCfg.createModifier().ifPresent((modifier) -> {
                    ItemStack stack = new ItemStack(gearItem);
                    if (stack.getItem() instanceof VaultGearItem) {
                        VaultGearData vgData = VaultGearData.read(stack);
                        vgData.setState(VaultGearState.IDENTIFIED);
                        vgData.setRarity(VaultGearRarity.COMMON);
                        vgData.write(stack);
                    }
                    modifier.getConfigDisplay(stack).ifPresent((configDisplay) -> {
                        if(discoveredModifiers.compoundDiscoverWorkbenchCraft(player, gearItem, modifierId)) {
                            MutableComponent cmp = new TextComponent("Unlocked the Modifier: ")
                                    .append(configDisplay)
                                    .append(" for: ")
                                    .append(stack.getHoverName());

                            context.getSource().sendSuccess(cmp, true);

                        } else {
                            MutableComponent cmp = new TextComponent("Cannot Unlock. ")
                                    .append("Already Unlocked ")
                                    .append(configDisplay)
                                    .append(" for: ")
                                    .append(stack.getHoverName());
                            context.getSource().sendFailure(cmp);
                        }


                    });
                });
            } else {
                context.getSource().sendFailure(new TextComponent("Cannot unlock. Invalid id: ").append(modifierId.toString()).append("for type " + itemId));
            }
        });


        return 0;
    }

    private int unlockPotionCraftingModifiers(CommandContext<CommandSourceStack> context, String effectId) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        DiscoveredAlchemyEffectsData discoveredEffectsData = DiscoveredAlchemyEffectsData.get(context.getSource().getServer());

        AlchemyTableConfig.CraftableEffectConfig effectCfg = ModConfigs.VAULT_ALCHEMY_TABLE.getConfig(effectId);
        if(discoveredEffectsData.compoundDiscoverEffect(player, effectId)) {
            if (effectCfg != null) {
                context.getSource().sendSuccess(new TextComponent("Unlocked the Potion effect: ").append(effectCfg.getEffectName()), true);
            } else {
                context.getSource().sendFailure(new TextComponent("Cannot unlock. Invalid id: ").append(effectId));
            }
        } else {
            context.getSource().sendFailure(new TextComponent("Cannot unlock. Already Unlocked: ").append(effectId));
        }
        return 0;
    }
}
