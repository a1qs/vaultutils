package io.github.a1qs.vaultutils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import iskallia.vault.config.gear.VaultGearWorkbenchConfig;
import iskallia.vault.init.ModConfigs;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;

import java.util.stream.Collectors;

public class UnlockArchiveModifiers {
    private static final SuggestionProvider<CommandSourceStack> SUGGEST_CRAFTING_MODIFIERS = (context, builder) ->
            SharedSuggestionProvider.suggestResource(ModConfigs.VAULT_GEAR_WORKBENCH_CONFIG.values().stream()
                    .flatMap(parentConfig -> parentConfig.getAllCraftableModifiers().stream())
                    .map(VaultGearWorkbenchConfig.CraftableModifierConfig::getWorkbenchCraftIdentifier)
                    .collect(Collectors.toList()), builder);


    public UnlockArchiveModifiers(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("vaultutils")
                .requires(sender -> sender.hasPermission(this.getRequiredPermissionLevel()))
                .then(Commands.literal("unlockCraftingModifiers")
                        .then(Commands.argument("modifierId", ResourceLocationArgument.id()).suggests(SUGGEST_CRAFTING_MODIFIERS).executes(this::unlockCraftingModifiers))
                )
        );

    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    private int unlockCraftingModifiers(CommandContext<CommandSourceStack> context) {
        return 0;
    }
}
