package io.github.a1qs.vaultutils.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.dynamodel.model.armor.ArmorModel;
import iskallia.vault.dynamodel.model.armor.ArmorPieceModel;
import iskallia.vault.init.ModDynamicModels;
import iskallia.vault.world.data.DiscoveredModelsData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.Map;

public class UnlockModelCommands {

    public UnlockModelCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        ModDynamicModels.Armor.MODEL_REGISTRY.forEach((setId, armorModel) -> {

            armorModel.getPiece(EquipmentSlot.HEAD).ifPresent((piece) -> dispatcher.register(Commands.literal("vaultutils")
                    .requires(sender -> sender.hasPermission(this.getRequiredPermissionLevel()))
                    .then(Commands.literal("unlockSpecificModel")
                            .then(Commands.argument("player", EntityArgument.player())
                                    .then(Commands.literal("HELMET")
                                            .then(Commands.literal(piece.getId().toString()).executes(context -> unlockSpecificModel(context, piece.getId(), piece)))
                                    )
                            )
                    )
            ));

            armorModel.getPiece(EquipmentSlot.CHEST).ifPresent((piece) -> dispatcher.register(Commands.literal("vaultutils")
                    .requires(sender -> sender.hasPermission(this.getRequiredPermissionLevel()))
                    .then(Commands.literal("unlockSpecificModel")
                            .then(Commands.argument("player", EntityArgument.player())
                                    .then(Commands.literal("CHESTPLATE")
                                            .then(Commands.literal(piece.getId().toString()).executes(context -> unlockSpecificModel(context, piece.getId(), piece)))
                                    )
                            )
                    )
            ));

            armorModel.getPiece(EquipmentSlot.LEGS).ifPresent((piece) -> dispatcher.register(Commands.literal("vaultutils")
                    .requires(sender -> sender.hasPermission(this.getRequiredPermissionLevel()))
                    .then(Commands.literal("unlockSpecificModel")
                            .then(Commands.argument("player", EntityArgument.player())
                                    .then(Commands.literal("LEGGINGS")
                                            .then(Commands.literal(piece.getId().toString()).executes(context -> unlockSpecificModel(context, piece.getId(), piece)))
                                    )
                            )
                    )
            ));

            armorModel.getPiece(EquipmentSlot.FEET).ifPresent((piece) -> dispatcher.register(Commands.literal("vaultutils")
                    .requires(sender -> sender.hasPermission(this.getRequiredPermissionLevel()))
                    .then(Commands.literal("unlockSpecificModel")
                            .then(Commands.argument("player", EntityArgument.player())
                                    .then(Commands.literal("BOOTS")
                                            .then(Commands.literal(piece.getId().toString()).executes(context -> unlockSpecificModel(context, piece.getId(), piece)))
                                    )
                            )
                    )
            ));

            dispatcher.register(Commands.literal("vaultutils")
                    .requires(sender -> sender.hasPermission(this.getRequiredPermissionLevel()))
                    .then(Commands.literal("unlockSpecificModel")
                            .then(Commands.argument("player", EntityArgument.player())
                                    .then(Commands.literal("ALL")
                                            .then(Commands.literal(armorModel.getId().toString()).executes(context -> unlockSpecificModelGroup(context, armorModel.getPieces(), armorModel)))
                                    )
                            )
                    )
            );

        });
    }



    private int unlockSpecificModel(CommandContext<CommandSourceStack> context, ResourceLocation setId, ArmorPieceModel piece) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        DiscoveredModelsData modelsData = DiscoveredModelsData.get(player.server);
        modelsData.discoverModel(player.getUUID(), setId);
        if(modelsData.getDiscoveredModels(player.getUUID()).contains(setId)) {
            context.getSource().sendFailure(new TextComponent("Model: " + piece.getDisplayName() + "is already unlocked!"));
            return 1;
        }
        context.getSource().sendSuccess(new TextComponent("Unlocked the Model: " + piece.getDisplayName()), true);

        return 0;
    }

    private int unlockSpecificModelGroup(CommandContext<CommandSourceStack> context, Map<EquipmentSlot, ArmorPieceModel> armorModelPieces, ArmorModel armorModel) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        DiscoveredModelsData modelsData = DiscoveredModelsData.get(player.server);

        armorModelPieces.forEach((slot, model) -> modelsData.discoverModel(player.getUUID(), model.getId()));
        context.getSource().sendSuccess(new TextComponent("Unlocked the Model: " + armorModel.getDisplayName()), true);
        return 0;
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }
}
