package io.github.a1qs.vaultutils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.a1qs.vaultutils.util.TimeUtil;
import io.github.a1qs.vaultutils.util.VaultPauseManager;
import iskallia.vault.core.vault.Vault;
import iskallia.vault.core.vault.time.TickClock;
import iskallia.vault.core.vault.time.modifier.PylonExtension;
import iskallia.vault.world.data.ServerVaults;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.concurrent.atomic.AtomicBoolean;

public class VaultTimerCommands {
    public VaultTimerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("vaultutils")
            .requires(sender -> sender.hasPermission(this.getRequiredPermissionLevel()))
                .then(Commands.literal("vaultTimer")
                        .then(Commands.literal("addTime").then(Commands.argument("timeInTicks", IntegerArgumentType.integer()).executes(this::addTime)))
                        .then(Commands.literal("stopTimer").then(Commands.argument("isPaused", BoolArgumentType.bool()).executes(this::stopTimer)))
                )
        );
    }

    private int addTime(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        int time = IntegerArgumentType.getInteger(context, "time in ticks");
        ServerPlayer serverPlayer = context.getSource().getPlayerOrException();
        AtomicBoolean foundInVault = new AtomicBoolean(false);

        ServerVaults.get(serverPlayer.level).ifPresent((vault) -> {
            vault.ifPresent(Vault.CLOCK, (clock) -> {
                foundInVault.set(true);
                clock.addModifier(new PylonExtension(serverPlayer, time));

                if(TimeUtil.isBelowSecond(time)) {
                    MutableComponent feedback1 = new TextComponent("Added ")
                            .append(new TextComponent(String.valueOf(time)).withStyle(ChatFormatting.YELLOW))
                            .append(new TextComponent(TimeUtil.ticksText(time) + " to the Vault Timer"));

                    context.getSource().sendSuccess(feedback1, true);
                } else {
                    String secondsText = TimeUtil.secondsText(time);
                    MutableComponent feedback2 = new TextComponent("Added ")
                            .append(new TextComponent(String.valueOf(time/20)).withStyle(ChatFormatting.YELLOW))
                            .append(new TextComponent(secondsText +" to the Vault Timer"))
                            .append(new TextComponent(" (" + time + TimeUtil.ticksText(time) + ")").withStyle(ChatFormatting.DARK_GRAY));

                    context.getSource().sendSuccess(feedback2, true);
                }
            });
        });

        if(!foundInVault.get()) {
            context.getSource().sendFailure(new TextComponent(serverPlayer.getName().getString() + " is not inside a Vault!"));
            return 1;
        }

        return 0;
    }

    private int stopTimer(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        boolean isPaused = BoolArgumentType.getBool(context, "isPaused");
        ServerPlayer serverPlayer = context.getSource().getPlayerOrException();
        AtomicBoolean foundInVault = new AtomicBoolean(false);



        ServerVaults.get(serverPlayer.level).ifPresent((vault) -> {
            vault.ifPresent(Vault.CLOCK, (clock) -> {
                foundInVault.set(true);

                if(clock.has(TickClock.PAUSED) && isPaused) {
                    context.getSource().sendFailure(new TextComponent("Timer is already paused!"));
                } else if (!clock.has(TickClock.PAUSED) && !isPaused) {
                    context.getSource().sendFailure(new TextComponent("Timer is already unpaused!"));
                } else if (clock.has(TickClock.PAUSED) && !isPaused) {
                    vault.get(Vault.CLOCK).remove(TickClock.PAUSED);
                    context.getSource().sendSuccess(new TextComponent("Timer has been unpaused").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC), true);
                } else {
                    clock.set(TickClock.PAUSED);
                    context.getSource().sendSuccess(new TextComponent("Timer has been paused").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC), true);
                }
                VaultPauseManager.vaultTimerManuallyPaused(vault, isPaused);
            });
        });

        if(!foundInVault.get()) {
            context.getSource().sendFailure(new TextComponent(serverPlayer.getName().getString() + " is not inside a Vault!"));
            return 1;
        }

        return 0;
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }
}
