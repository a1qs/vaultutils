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
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.concurrent.atomic.AtomicBoolean;


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
        AtomicBoolean foundInVault = new AtomicBoolean(false);
        ServerVaults.get(player.level).ifPresent((vault) ->
                vault.ifPresent(Vault.OBJECTIVES, objectives -> {
                    foundInVault.set(true);
                    if(objectives.get(Objectives.KEY).equals("cake")) {
                        objectives.forEach(CakeObjective.class, objective -> {
                            BlockPos pos = objective.get(CakeObjective.CAKE_POS);
                            player.teleportTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                            context.getSource().sendSuccess(new TextComponent("Teleported " + player.getName().getString() + " to the Cake location"), true);
                            return true;
                        });
                    } else {
                        context.getSource().sendFailure(new TextComponent(player.getName().getString() + " is not inside a  Cake Vault!"));
                    }
        }));

        if(!foundInVault.get()) {
            context.getSource().sendFailure(new TextComponent(player.getName().getString() + " is not inside a Vault!"));
            return 1;
        }

        return 0;
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }
}
