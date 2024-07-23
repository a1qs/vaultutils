package io.github.a1qs.vaultutils.mixins;

import io.github.a1qs.vaultutils.config.CommonConfigs;
import iskallia.vault.skill.ability.effect.SummonEternalAbility;
import iskallia.vault.skill.ability.effect.spi.core.InstantManaAbility;
import iskallia.vault.skill.base.SkillContext;
import iskallia.vault.world.data.EternalsData;
import iskallia.vault.world.data.ServerVaults;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;

import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = SummonEternalAbility.class, remap = false)
public abstract class MixinSummonEternalAbility extends InstantManaAbility {

    /**
     * @author a1qs
     * @reason make eternal ability summon 20 TNT instead of eternals
     */
    @Overwrite
    protected boolean canDoAction(SkillContext context) {
        if(CommonConfigs.ENABLE_ETERNAL_MIXIN.get()) {
            return (Boolean) context.getSource().as(ServerPlayer.class).map((player) -> {
                Level level = player.getServer().getLevel(player.getCommandSenderWorld().dimension());
                for (int i = 0; i < 20; i++) {
                    PrimedTnt tntEntity = EntityType.TNT.create(level);
                    if (tntEntity != null) {
                        BlockPos playerPos = player.blockPosition();
                        tntEntity.moveTo(playerPos.getX() + 0.5, playerPos.getY(), playerPos.getZ() + 0.5, 0, 0);
                        tntEntity.setFuse(1);
                        level.addFreshEntity(tntEntity);
                    }
                }
                player.sendMessage((new TextComponent("dont do that")).withStyle(ChatFormatting.RED), Util.NIL_UUID);
                return false;
            }).orElse(false);
        } else {
            return (Boolean)context.getSource().as(ServerPlayer.class).map((player) -> {
                if (!player.getCommandSenderWorld().isClientSide()) {
                    Level level = player.getCommandSenderWorld();
                    if (level instanceof ServerLevel serverLevel) {
                        EternalsData.EternalGroup var5 = EternalsData.get(serverLevel).getEternals(player);
                        if (var5.getEternals().isEmpty()) {
                            player.sendMessage((new TextComponent("You have no eternals to summon.")).withStyle(ChatFormatting.RED), Util.NIL_UUID);
                            return false;
                        }

                        if (ServerVaults.get(player.level).isEmpty()) {
                            player.sendMessage((new TextComponent("You can only summon eternals in the Vault!")).withStyle(ChatFormatting.RED), Util.NIL_UUID);
                            return false;
                        }

                        return super.canDoAction(context);
                    }
                }

                return false;
            }).orElse(false);
        }
    }
}
