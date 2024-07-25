package io.github.a1qs.vaultutils.commands;

import com.mojang.brigadier.CommandDispatcher;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import io.github.a1qs.vaultutils.util.TimeUtil;
import iskallia.vault.core.data.key.ThemeKey;
import iskallia.vault.core.vault.VaultRegistry;
import iskallia.vault.core.world.roll.IntRoll;
import iskallia.vault.item.crystal.CrystalData;
import iskallia.vault.item.crystal.VaultCrystalItem;
import iskallia.vault.item.crystal.layout.*;
import iskallia.vault.item.crystal.objective.*;
import iskallia.vault.item.crystal.theme.ValueCrystalTheme;
import iskallia.vault.item.crystal.time.ValueCrystalTime;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

import java.util.stream.Collectors;

public class CrystalCommands {
    private static final SuggestionProvider<CommandSourceStack> SUGGEST_THEMES = (context, builder) ->
            SharedSuggestionProvider.suggestResource(VaultRegistry.THEME.getKeys().stream().map(ThemeKey::getId).collect(Collectors.toList()), builder);

    public CrystalCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("vaultutils")
                .requires(sender -> sender.hasPermission(this.getRequiredPermissionLevel()))
                .then(Commands.literal("crystal")
                        .then(Commands.literal("setLayout")
                                .then(Commands.literal("INFINITE").executes(context -> setLayout(context, new ClassicInfiniteCrystalLayout())))
                                .then(Commands.literal("PARADOX").executes(context -> setLayout(context, new ParadoxCrystalLayout())))
                                .then(Commands.literal("CIRCLE").executes(context -> setLayout(context, new ClassicCircleCrystalLayout())))
                                .then(Commands.literal("SQUARE")
                                        .then(Commands.argument("tunnelSpan", IntegerArgumentType.integer(0))
                                                .then(Commands.argument("vertices", IntegerArgumentType.integer(0, 32))
                                                        .executes(context -> {
                                                            int tunnelSpan = IntegerArgumentType.getInteger(context, "tunnelSpan");
                                                            int vertices = IntegerArgumentType.getInteger(context, "vertices");
                                                            return setLayout(context, new ClassicPolygonCrystalLayout(tunnelSpan, new int[]{-vertices, vertices, vertices, vertices, vertices, -vertices, -vertices, -vertices}));
                                                        })
                                                )
                                        )
                                )
                                .then(Commands.literal("DIAMOND")
                                        .then(Commands.argument("tunnelSpan", IntegerArgumentType.integer(0))
                                                .then(Commands.argument("vertices", IntegerArgumentType.integer(0, 32))
                                                        .executes(context -> {
                                                            int tunnelSpan = IntegerArgumentType.getInteger(context, "tunnelSpan");
                                                            int vertices = IntegerArgumentType.getInteger(context, "vertices");
                                                            return setLayout(context, new ClassicPolygonCrystalLayout(tunnelSpan, new int[]{-vertices, 0, 0, vertices, vertices, 0, 0, -vertices})); //new int[]{-4, 0, 0, 4, 4, 0, 0, -4}
                                                        }))
                                        )
                                )
                        )
                        .then(Commands.literal("setTime").then(Commands.argument("time in ticks", IntegerArgumentType.integer(0)).executes(this::setTime)))
                        .then(Commands.literal("setTheme").then(Commands.argument("theme", ResourceLocationArgument.id()).suggests(SUGGEST_THEMES).executes(this::setTheme)))
                        .then(Commands.literal("setObjective")
                                .then(Commands.literal("SCAVENGER").then(Commands.argument("objectiveProbability", FloatArgumentType.floatArg(0.0F, 1.0F)).executes(context -> setObjective(context, new ScavengerCrystalObjective(FloatArgumentType.getFloat(context, "objectiveProbability"))))))
                                .then(Commands.literal("GUARDIAN").then(Commands.argument("objectiveCountMin", IntegerArgumentType.integer(1))).then(Commands.argument("objectiveCountMax", IntegerArgumentType.integer(1))).then(Commands.argument("waveCountMin", IntegerArgumentType.integer(1))).then(Commands.argument("waveCountMax", IntegerArgumentType.integer(1))).then(Commands.argument("objectiveProbability", FloatArgumentType.floatArg(0.0F, 1.0F)).executes(context -> setObjective(context, new BossCrystalObjective(IntRoll.ofUniform(IntegerArgumentType.getInteger(context, "objectiveCountMin"), IntegerArgumentType.getInteger(context, "objectiveCountMax")), IntRoll.ofUniform(IntegerArgumentType.getInteger(context, "waveCountMin"), IntegerArgumentType.getInteger(context, "waveCountMax")), FloatArgumentType.getFloat(context, "objectiveProbability"))))))
                                .then(Commands.literal("ELIXIR").executes(context -> setObjective(context, new ElixirCrystalObjective())))
                                .then(Commands.literal("CAKE").executes(context -> setObjective(context, new CakeCrystalObjective())))
                                .then(Commands.literal("BRAZIER").then(Commands.argument("objectiveCountMin", IntegerArgumentType.integer(1))).then(Commands.argument("objectiveCountMax", IntegerArgumentType.integer(1))).then(Commands.argument("objectiveProbability", FloatArgumentType.floatArg(0.0F, 1.0F)).executes(context -> setObjective(context, new MonolithCrystalObjective(IntRoll.ofUniform(IntegerArgumentType.getInteger(context, "objectiveCountMin"), IntegerArgumentType.getInteger(context, "objectiveCountMax")), FloatArgumentType.getFloat(context, "objectiveProbability"))))))
                        )
                )
        );
    }

    private int setTime(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        int time = IntegerArgumentType.getInteger(context, "time in ticks");
        ItemStack crystal = this.getCrystal(context);
        if(crystal == null) {
            context.getSource().sendFailure(new TextComponent(player.getName().getString() + " is not holding a Crystal!"));
            return 1;
        }
        CrystalData data = CrystalData.read(crystal);
        data.setTime(new ValueCrystalTime(IntRoll.ofConstant(time)));
        data.write(crystal);

        if(TimeUtil.isBelowSecond(time)) {
            MutableComponent feedback1 = new TextComponent("Set the Crystal time to ")
                    .append(new TextComponent(String.valueOf(time)).withStyle(ChatFormatting.YELLOW))
                    .append(new TextComponent(TimeUtil.ticksText(time)));

            context.getSource().sendSuccess(feedback1, true);
        } else {
            MutableComponent feedback2 = new TextComponent("Set the Crystal time to ")
                    .append(new TextComponent(String.valueOf(TimeUtil.timeInSeconds(time))).withStyle(ChatFormatting.YELLOW))
                    .append(new TextComponent(TimeUtil.secondsText(time)))
                    .append(new TextComponent(" (" + time + TimeUtil.ticksText(time) + ")").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));

            context.getSource().sendSuccess(feedback2, true);
        }

        return 0;
    }


    private int setLayout(CommandContext<CommandSourceStack> context, CrystalLayout layout) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack crystal = this.getCrystal(context);
        if(crystal == null) {
            context.getSource().sendFailure(new TextComponent(player.getName().getString() + " is not holding a Crystal!"));
            return 1;
        }
        CrystalData data = CrystalData.read(crystal);
        data.setLayout(layout);
        data.write(crystal);

        return 0;
    }

    private int setTheme(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack crystal = this.getCrystal(context);
        ResourceLocation id = ResourceLocationArgument.getId(context, "theme");

        if(crystal == null) {
            context.getSource().sendFailure(new TextComponent(player.getName().getString() + " is not holding a Crystal!"));
            return 1;
        }
        if (VaultRegistry.THEME.getKey(id) == null) {
            context.getSource().sendFailure(new TextComponent("The theme " + id + " does not exist!"));
            return 1;
        }

        CrystalData data = CrystalData.read(crystal);
        data.setTheme(new ValueCrystalTheme(id));
        data.write(crystal);
        context.getSource().sendSuccess(new TextComponent("Set the Crystal theme to ").append(new TextComponent(VaultRegistry.THEME.getKey(id).getName()).setStyle(Style.EMPTY.withColor(VaultRegistry.THEME.getKey(id).getColor()))), true);
        return 0;
    }

    private int setObjective(CommandContext<CommandSourceStack> context, CrystalObjective objective) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack crystal = this.getCrystal(context);
        if(crystal == null) {
            context.getSource().sendFailure(new TextComponent(player.getName().getString() + " is not holding a Crystal!"));
            return 1;
        }
        CrystalData data = CrystalData.read(crystal);
        data.setObjective(objective);
        data.write(crystal);

        return 0;
    }


    private ItemStack getCrystal(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack held = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!held.isEmpty() && held.getItem() instanceof VaultCrystalItem) {
            return held;
        }
        return null;
    }



    public int getRequiredPermissionLevel() {
        return 2;
    }


}
