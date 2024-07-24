package io.github.a1qs.vaultutils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.skill.ability.effect.spi.core.Ability;
import iskallia.vault.skill.base.SkillContext;
import iskallia.vault.skill.tree.AbilityTree;
import iskallia.vault.world.data.PlayerAbilitiesData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

public class ResetAbilityCooldownCommands {

    public ResetAbilityCooldownCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("vaultutils")
                .requires(sender -> sender.hasPermission(this.getRequiredPermissionLevel()))
                .then(Commands.literal("resetAbilityCooldown")
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(this::resetAbilityCooldown)
                        )
                )
        );
    }

    private int resetAbilityCooldown(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        PlayerAbilitiesData abilitiesData = PlayerAbilitiesData.get(player.getLevel());
        AbilityTree abilityTree = abilitiesData.getAbilities(player);

        abilityTree.iterate(Ability.class, (ability) ->
            ability.getCooldown().ifPresent(cooldown -> {
                ability.reduceCooldownBy(1000000);
                System.out.println("remainingTicks" + cooldown.getRemainingTicks());
                System.out.println("maxTicks: " + cooldown.getMaxTicks());
                System.out.println("delay: " + cooldown.getRemainingDelayTicks());
            }));

        abilityTree.sync(SkillContext.of(player));
        context.getSource().sendSuccess(new TextComponent("Reset the Cooldown of player: " + player.getName().getString() + "!"), true);
        return 0;
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }
}
