package io.github.a1qs.vaultutils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.a1qs.vaultutils.mixins.VaultAltarTileEntityInvoker;
import iskallia.vault.altar.AltarInfusionRecipe;
import iskallia.vault.block.entity.VaultAltarTileEntity;
import iskallia.vault.world.data.PlayerVaultAltarData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class VaultAltarCommands {

    public VaultAltarCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("vaultutils")
                .requires(sender -> sender.hasPermission(this.getRequiredPermissionLevel()))
                .then(Commands.literal("vaultAltar")
                        .then(Commands.literal("resetRecipe")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(this::resetAltarRecipe)
                                )
                        )
                        .then(Commands.literal("rerollRecipe")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(this::rerollAltarRecipe)
                                )
                        )
                        .then(Commands.literal("completeRecipe")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(this::completeAltarRecipe)
                                )
                        )
                )
        );
    }



    private int resetAltarRecipe(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        ServerLevel level = context.getSource().getLevel();
        PlayerVaultAltarData data = PlayerVaultAltarData.get(level);
        List<BlockPos> altars = data.getAltars(player.getUUID());
        if(data.getRecipe(player) == null) {
            context.getSource().sendFailure(new TextComponent("Cannot Reset. Player does not have a valid Altar Recipe"));
            return 1;
        } else {
            this.modifyAltarRecipe(player, altars, false);
            context.getSource().sendSuccess(new TextComponent("Successfully reset Player's Altar Recipe"), true);
            return 0;
        }



    }

    private int rerollAltarRecipe(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        ServerLevel level = context.getSource().getLevel();
        PlayerVaultAltarData data = PlayerVaultAltarData.get(level);
        List<BlockPos> altars = data.getAltars(player.getUUID());
        if(data.getRecipe(player) == null) {
            context.getSource().sendFailure(new TextComponent("Cannot Reroll. Player does not have a valid Altar Recipe to reroll!"));
            return 1;
        } else {
            this.modifyAltarRecipe(player, altars, true);
            context.getSource().sendSuccess(new TextComponent("Successfully rerolled Player's Altar Recipe"), true);
            return 0;
        }

    }

    //doesnt update properly for all altars if multiple are used
    private int completeAltarRecipe(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        ServerLevel level = context.getSource().getLevel();
        PlayerVaultAltarData data = PlayerVaultAltarData.get(level);
        List<BlockPos> altars = data.getAltars(player.getUUID());
        AtomicBoolean sentFailure = new AtomicBoolean(false);
        AtomicBoolean sentSuccess = new AtomicBoolean(false);


        altars.forEach(blockPos -> {
            if(player.getLevel().isLoaded(blockPos) && player.getLevel().getBlockEntity(blockPos) instanceof VaultAltarTileEntity altarTile) {
                if(altarTile.getRecipe() == null && !sentFailure.get() && !sentSuccess.get()) {
                    context.getSource().sendFailure(new TextComponent("Cannot Complete. No Altar Recipes to Complete!"));
                    sentFailure.set(true);
                    altarTile.sendUpdates();
                    return;
                }
                if(altarTile.getRecipe() != null) {
                    if (altarTile.getRecipe().getIncompleteRequiredItems().isEmpty() && !sentFailure.get() && !sentSuccess.get()) {
                        context.getSource().sendFailure(new TextComponent("Cannot Complete. Recipe is Already Completed!"));
                        sentFailure.set(true);
                        altarTile.sendUpdates();
                        return;
                    }

                    if (!sentFailure.get() && !sentSuccess.get()) {
                        altarTile.getRecipe().getIncompleteRequiredItems().forEach(items -> items.setCurrentAmount(2147483000));
                        context.getSource().sendSuccess(new TextComponent("Successfully completed Player's Altar Recipe"), true);

                        altarTile.sendUpdates();
                        PlayerVaultAltarData.get().setDirty();
                        sentSuccess.set(true);
                    }
                }
            }
        });
        return 0;
    }

    //breaks with multiple altars, dunno how to fix
    private void modifyAltarRecipe(ServerPlayer player, List<BlockPos> altars, boolean shouldReroll) {
        PlayerVaultAltarData data = PlayerVaultAltarData.get(player.getLevel());
        data.removeRecipe(player.getUUID());
        altars.forEach(blockPos -> {
            if(player.getLevel().isLoaded(blockPos) && player.getLevel().getBlockEntity(blockPos) instanceof VaultAltarTileEntity altarTile) {
                if(shouldReroll && altarTile.getAltarState() == VaultAltarTileEntity.AltarState.ACCEPTING) {
                    ((VaultAltarTileEntityInvoker) altarTile).invokeResetAltar(player.getLevel());
                    AltarInfusionRecipe newRecipe = data.getRecipe(player, blockPos);
                    altarTile.setRecipe(newRecipe);
                    ((VaultAltarTileEntityInvoker) altarTile).invokeUpdateDisplayedIndex(newRecipe);
                    altarTile.setAltarState(VaultAltarTileEntity.AltarState.ACCEPTING);
                    altarTile.sendUpdates();

                } else {
                    ((VaultAltarTileEntityInvoker) altarTile).invokeResetAltar(player.getLevel());
                    altarTile.sendUpdates();
                }
            }
        });
    }


    public int getRequiredPermissionLevel() {
        return 2;
    }

}
