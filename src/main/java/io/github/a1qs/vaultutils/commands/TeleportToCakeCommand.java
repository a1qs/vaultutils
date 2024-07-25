package io.github.a1qs.vaultutils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.core.vault.Vault;
import iskallia.vault.core.vault.objective.CakeObjective;
import iskallia.vault.core.vault.objective.Objectives;
import iskallia.vault.world.data.ServerVaults;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;


public class TeleportToCakeCommand {
    public TeleportToCakeCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("vaultutils")
                .requires(sender -> sender.hasPermission(this.getRequiredPermissionLevel()))
                .then(Commands.literal("tpToCake")
                        .then(Commands.argument("player", EntityArgument.player())
                            .executes(this::tpToCake)
                        )
                )
        );
    }

    private int tpToCake(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        ServerVaults.get(player.level).ifPresent((vault) ->
                vault.ifPresent(Vault.OBJECTIVES, objectives -> {
                    if(objectives.get(Objectives.KEY).equals("cake")) {
                        objectives.forEach(CakeObjective.class, objective -> {
                            BlockPos pos = objective.get(CakeObjective.CAKE_POS);
                            player.teleportTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                            return true;
                        });
                    }
        }));

        return 0;
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }
}
