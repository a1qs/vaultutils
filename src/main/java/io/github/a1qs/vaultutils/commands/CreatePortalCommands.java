package io.github.a1qs.vaultutils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.block.VaultPortalBlock;
import iskallia.vault.block.entity.VaultPortalTileEntity;
import iskallia.vault.init.ModBlocks;
import iskallia.vault.init.ModItems;
import iskallia.vault.item.crystal.CrystalData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CreatePortalCommands {

    public CreatePortalCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("vaultutils")
                .requires(sender -> sender.hasPermission(this.getRequiredPermissionLevel()))
                    .then(Commands.literal("createPortal")
                            .then(Commands.argument("blockPos", BlockPosArgument.blockPos())
                                    .executes(this::createPortal))));
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public int createPortal(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "blockPos");

        ServerLevel level = context.getSource().getLevel();
        for (int x = -1; x <= 2; x++) {
            for (int y = 0; y <= 4; y++) {
                BlockPos framePos = pos.offset(x, y, 0); //
                if (x == -1 || x == 2  || y == 0 || y == 4) {
                    level.setBlock(framePos, ModBlocks.VAULT_STONE.defaultBlockState(), 3);
                } else {
                    level.setBlock(framePos, Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }

        for (int x = 0; x <= 1; x++) {
            for (int y = 1; y <= 3; y++) {
                BlockPos portalPos = pos.offset(x, y, 0);
                CrystalData crystal = CrystalData.read(new ItemStack(ModItems.VAULT_CRYSTAL));
                level.setBlock(portalPos, ModBlocks.VAULT_PORTAL.defaultBlockState().setValue(VaultPortalBlock.AXIS, Direction.Axis.X), 3);
                BlockEntity te = level.getBlockEntity(portalPos);
                if (te instanceof VaultPortalTileEntity tile) {
                    tile.setCrystalData(crystal);
                }
            }
        }
        context.getSource().sendSuccess(new TextComponent("Set a new Vault Portal at Position: X: " + pos.getX() + " Y: " + pos.getY() + " Z: " + pos.getZ()), true);
        return 0;
    }
}
