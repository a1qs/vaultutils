package io.github.a1qs.vaultutils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class TestCommands {

    public TestCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("vaultutils")
            .requires(sender -> sender.hasPermission(this.getRequiredPermissionLevel()))
                .then(Commands.literal("test")
                        .executes(this::test)
                )
        );
    }

    private int test(CommandContext<CommandSourceStack> context) {
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
        return 0;
    }




    public int getRequiredPermissionLevel() {
        return 2;
    }
}
