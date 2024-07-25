package io.github.a1qs.vaultutils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.gear.attribute.VaultGearAttributeInstance;
import iskallia.vault.gear.data.VaultGearData;
import iskallia.vault.init.ModGearAttributes;
import iskallia.vault.item.gear.DataInitializationItem;
import iskallia.vault.item.tool.JewelItem;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class JewelCommands {
    public JewelCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("vaultutils")
                .requires(sender -> sender.hasPermission(this.getRequiredPermissionLevel()))
                .then(Commands.literal("jewel")
                        .then(Commands.literal("modifySize").then(Commands.argument("jewelSize", IntegerArgumentType.integer(1, 35)).executes(this::modifySize)))
                )
        );
    }

    private int modifySize(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack playerHand = player.getMainHandItem();
        int jewelSize = IntegerArgumentType.getInteger(context, "jewelSize");
        if(playerHand.getItem() instanceof JewelItem) {
            VaultGearData data = VaultGearData.read(playerHand);


            for (VaultGearAttributeInstance<Integer> integerVaultGearAttributeInstance : data.getModifiers(ModGearAttributes.JEWEL_SIZE, VaultGearData.Type.ALL_MODIFIERS)) {
                integerVaultGearAttributeInstance.setValue(jewelSize);

                data.write(player.getMainHandItem());
                DataInitializationItem.doInitialize(playerHand);
            }
            context.getSource().sendSuccess(new TextComponent("Set the Jewel size to: " + jewelSize), true);
        } else {
            context.getSource().sendFailure(new TextComponent("Player is not holding a Jewel!"));
            return 1;
        }
        return 0;
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }


}
