package io.github.a1qs.vaultutils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.block.VaultCrateBlock;
import iskallia.vault.core.random.JavaRandom;
import iskallia.vault.core.vault.CrateLootGenerator;
import iskallia.vault.core.vault.Vault;
import iskallia.vault.core.vault.objective.AwardCrateObjective;
import iskallia.vault.core.vault.objective.Objectives;
import iskallia.vault.core.vault.player.Completion;
import iskallia.vault.core.vault.player.Listener;
import iskallia.vault.core.vault.stat.StatCollector;
import iskallia.vault.world.data.ServerVaults;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class KickCommands {

    public KickCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("vaultutils")
                .requires(sender -> sender.hasPermission(this.getRequiredPermissionLevel()))
                .then(Commands.literal("kick")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.literal("COMPLETE").then(Commands.argument("grantCrate", BoolArgumentType.bool()).executes(context -> kickPlayer(context, Completion.COMPLETED))))
                                .then(Commands.literal("FAIL").executes(context -> kickPlayer(context, Completion.FAILED)))
                                .then(Commands.literal("BAIL").executes(context -> kickPlayer(context, Completion.BAILED)))
                        )
                )
        );
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    private int kickPlayer(CommandContext<CommandSourceStack> context, Completion completion) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        Iterator<Vault> var3 = ServerVaults.getAll().iterator();
        AtomicBoolean foundInVault = new AtomicBoolean(false);

        while(var3.hasNext()) {
            Vault vault = var3.next();
            vault.ifPresent(Vault.LISTENERS, (listeners) -> {
                Listener listener = listeners.get(player.getUUID());
                if (listener != null) {
                    foundInVault.set(true);
                    ServerVaults.getWorld(vault).ifPresent((world) -> {
                        listeners.remove(world, vault, listener);
                        vault.ifPresent(Vault.STATS, (collector) -> {
                            StatCollector stats = collector.get(listener.get(Listener.ID));

                            if(completion == Completion.COMPLETED && BoolArgumentType.getBool(context, "grantCrate")) {
                                int level = vault.get(Vault.LEVEL).get();
                                Objectives objectives = vault.get(Vault.OBJECTIVES);
                                String objID = objectives.get(Objectives.KEY).toUpperCase();
                                VaultCrateBlock.Type type = VaultCrateBlock.Type.valueOf(objID);
                                AwardCrateObjective objective = AwardCrateObjective.ofConfig(type, type.toString().toLowerCase(), level, true);
                                CrateLootGenerator crateLootGenerator = new CrateLootGenerator(objective.get(AwardCrateObjective.LOOT_TABLE), 0.0f, objective.has(AwardCrateObjective.ADD_ARTIFACT), objective.get(AwardCrateObjective.ARTIFACT_CHANCE).floatValue());
                                ItemStack crate = VaultCrateBlock.getCrateWithLoot(type, crateLootGenerator.createLootForCommand(JavaRandom.ofInternal((new Random()).nextLong()), level));
                                stats.get(StatCollector.REWARD).add(crate);
                            }

                            stats.set(StatCollector.COMPLETION, completion);
                        });
                    });
                }
            });
        }
        if(!foundInVault.get()) {
            context.getSource().sendFailure(new TextComponent(player.getName().getString() + " is not inside a Vault!"));
            return 1;
        }
        context.getSource().sendSuccess(new TextComponent(player.getName().getString() + " has been kicked from the Vault with Completion: " + completion.toString()), false);
        return 0;
    }
}
