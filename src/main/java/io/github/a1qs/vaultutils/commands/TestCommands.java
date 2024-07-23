package io.github.a1qs.vaultutils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import iskallia.vault.config.gear.VaultGearWorkbenchConfig;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.world.data.DiscoveredWorkbenchModifiersData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;

import java.util.stream.Collectors;

public class TestCommands {

    public static final SuggestionProvider<CommandSourceStack> SUGGEST_MODIFIERS = (context, builder) ->
            SharedSuggestionProvider.suggestResource(ModConfigs.VAULT_GEAR_WORKBENCH_CONFIG.values().stream()
                    .flatMap(parentConfig -> parentConfig.getAllCraftableModifiers().stream())
                    .map(VaultGearWorkbenchConfig.CraftableModifierConfig::getWorkbenchCraftIdentifier)
                    .collect(Collectors.toList()), builder);

    public TestCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("vaultutils")
            .requires(sender -> sender.hasPermission(this.getRequiredPermissionLevel()))
                .then(Commands.literal("test")
                        .executes(this::test))
                .then(Commands.argument("theme", ResourceLocationArgument.id()).suggests(SUGGEST_MODIFIERS).executes(this::test)));

    }

    private int test(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
//        Iterator<Vault> var3 = ServerVaults.getAll().iterator();
//        while(var3.hasNext()) {
//            Vault vault = var3.next();
//            Objectives objectives = vault.get(Vault.OBJECTIVES);
//            String objID = objectives.get(Objectives.KEY).toUpperCase();
//            VaultCrateBlock.Type type = VaultCrateBlock.Type.valueOf(objID);
//            System.out.println(type);
//        }

//        ServerPlayer player = context.getSource().getPlayerOrException();
//        QuestState state = QuestStatesData.get().getState(player);
//        String inProgress = state.getInProgress().stream().findFirst().orElseThrow();
//        Optional<Quest> quest = state.getConfig(player.getLevel()).getQuestById(inProgress);
//        System.out.println(quest.get().getId());

        DiscoveredWorkbenchModifiersData discoveredModifiers = DiscoveredWorkbenchModifiersData.get(context.getSource().getServer());
        for (VaultGearWorkbenchConfig config : ModConfigs.VAULT_GEAR_WORKBENCH_CONFIG.values()) {

            for (VaultGearWorkbenchConfig.CraftableModifierConfig cfg : config.getAllCraftableModifiers()) {


            }
        }

        return 1;
    }




    public int getRequiredPermissionLevel() {
        return 2;
    }
}
