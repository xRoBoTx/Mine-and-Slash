package com.robertx22.mine_and_slash.database.data.spells.spell_classes.bases;

import com.robertx22.exiled_lib.registry.ISlashRegistryEntry;
import com.robertx22.exiled_lib.registry.SlashRegistryType;
import com.robertx22.mine_and_slash.database.base.Rarities;
import com.robertx22.mine_and_slash.database.data.gearitemslots.bases.BaseGearType;
import com.robertx22.mine_and_slash.database.data.spells.spell_classes.bases.configs.ImmutableSpellConfigs;
import com.robertx22.mine_and_slash.database.data.spells.spell_classes.bases.configs.SC;
import com.robertx22.mine_and_slash.database.data.stats.types.resources.Mana;
import com.robertx22.mine_and_slash.mmorpg.MMORPG;
import com.robertx22.mine_and_slash.mmorpg.Ref;
import com.robertx22.mine_and_slash.mmorpg.registers.common.ModItems;
import com.robertx22.mine_and_slash.saveclasses.gearitem.gear_bases.ITooltipList;
import com.robertx22.mine_and_slash.saveclasses.gearitem.gear_bases.Rarity;
import com.robertx22.mine_and_slash.saveclasses.gearitem.gear_bases.TooltipInfo;
import com.robertx22.mine_and_slash.saveclasses.spells.IAbility;
import com.robertx22.mine_and_slash.saveclasses.spells.calc.SpellCalcData;
import com.robertx22.mine_and_slash.saveclasses.unit.ResourcesData;
import com.robertx22.mine_and_slash.uncommon.capability.entity.EntityCap.UnitData;
import com.robertx22.mine_and_slash.uncommon.capability.player.PlayerSpellCap;
import com.robertx22.mine_and_slash.uncommon.datasaving.Load;
import com.robertx22.mine_and_slash.uncommon.enumclasses.Elements;
import com.robertx22.mine_and_slash.uncommon.interfaces.data_items.IRarity;
import com.robertx22.mine_and_slash.uncommon.localization.Words;
import com.robertx22.mine_and_slash.uncommon.utilityclasses.TooltipUtils;
import com.robertx22.mine_and_slash.uncommon.wrappers.SText;
import com.robertx22.mine_and_slash.vanilla_mc.packets.NoManaPacket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseSpell implements ISlashRegistryEntry<BaseSpell>, ITooltipList, IAbility {

    public List<EffectChance> onDamageEffects = new ArrayList<>();

    private final ImmutableSpellConfigs immutableConfigs;

    public BaseSpell(ImmutableSpellConfigs immutable) {
        this.immutableConfigs = immutable;
    }

    public final ImmutableSpellConfigs getImmutableConfigs() {
        return immutableConfigs;
    }

    public final void onCastingTick(SpellCastContext ctx) {

        int timesToCast = (int) ctx.getConfigFor(this)
            .get(SC.TIMES_TO_CAST)
            .get(ctx.spellsCap, this);

        if (timesToCast > 1) {

            int castTimeTicks = (int) ctx.getConfigFor(this)
                .get(SC.CAST_TIME_TICKS)
                .get(ctx.spellsCap, this);

            // if i didnt do this then cast time reduction would reduce amount of spell hits.
            int castEveryXTicks = castTimeTicks / timesToCast;

            if (ctx.isLastCastTick) {
                this.cast(ctx);
            } else {
                if (ctx.ticksInUse > 0 && ctx.ticksInUse % castEveryXTicks == 0) {
                    this.cast(ctx);
                }
            }

        } else if (timesToCast < 1) {
            System.out.println("Times to cast spell is: " + timesToCast + " . this seems like a bug.");
        }

    }

    public abstract BaseGearType.PlayStyle getPlayStyle();

    public Item getItem() {

        if (getPlayStyle().isINT()) {
            return ModItems.INT_SKILL_GEM.get();
        }
        if (getPlayStyle().isDEX()) {
            return ModItems.DEX_SKILL_GEM.get();
        }
        return ModItems.STR_SKILL_GEM.get();

    }

    @Override
    public ITextComponent getLocName() {
        return this.getName()
            .locName();
    }

    public void spawnParticles(SpellCastContext ctx) {

    }

    @Override
    public BaseSpell getSpell() {
        return this;
    }

    public final int getMaxSpellLevelNormal() {
        return getPreCalcConfig().maxSpellLevel;
    }

    public final int getMaxSpellLevelBuffed() {
        return getMaxSpellLevelNormal() + 5;
    }

    public boolean shouldActivateCooldown(PlayerEntity player, PlayerSpellCap.ISpellsCap spells) {
        return true;
    }

    public enum AllowedAsRightClickOn {
        MAGE_WEAPON, MELEE_WEAPON, NONE
    }

    @Override
    public Type getAbilityType() {
        return Type.SPELL;
    }

    public boolean isAllowedAsRightClickFor(BaseGearType slot) {
        switch (immutableConfigs.allowedAsRightClickOn()) {
            case NONE: {
                return false;
            }
            case MELEE_WEAPON: {
                return slot.isMeleeWeapon();
            }
            case MAGE_WEAPON: {
                return slot.isMageWeapon();
            }
        }
        return false;
    }

    @Override
    public int getRarityRank() {
        return IRarity.Magical;
    }

    @Override
    public Rarity getRarity() {
        return Rarities.Gears.get(getRarityRank());
    }

    public final boolean goesOnCooldownIfCastCanceled() {
        // override for spells that do oncastingtick
        return immutableConfigs.goesOnCooldownIfCanceled();
    }

    @Override
    public final ResourceLocation getIconLoc() {
        return new ResourceLocation(Ref.MODID, "textures/gui/spells/icons/" + GUID() + ".png");
    }

    public int getCooldownInTicks(SpellCastContext ctx) {
        int ticks = (int) ctx.getConfigFor(this)
            .get(SC.COOLDOWN_TICKS)
            .get(ctx.spellsCap, this);

        int seconds = (int) ctx.getConfigFor(this)
            .get(SC.COOLDOWN_SECONDS)
            .get(ctx.spellsCap, this);

        return (int) ((seconds * 20F) + ticks);
    }

    public final int getCooldownInSeconds(SpellCastContext ctx) {
        return getCooldownInTicks(ctx) / 20;
    }

    @Override
    public SlashRegistryType getSlashRegistryType() {
        return SlashRegistryType.SPELL;
    }

    public abstract String GUID();

    public final int getCalculatedManaCost(SpellCastContext ctx) {
        return (int) Mana.getInstance()
            .scale(ctx.getConfigFor(this)
                .get(SC.MANA_COST)
                .get(ctx.spellsCap, this), ctx.spellsCap.getEffectiveAbilityLevel(ctx.data, this));
    }

    public final int useTimeTicks(SpellCastContext ctx) {
        return (int) ctx.getConfigFor(this)
            .get(SC.CAST_TIME_TICKS)
            .get(ctx.spellsCap, this);
    }

    public final float getUseDurationInSeconds(SpellCastContext ctx) {
        return (float) useTimeTicks(ctx) / 20;
    }

    public final SpellCalcData getCalculation(SpellCastContext ctx) {
        return ctx.getConfigFor(this)
            .getCalc(ctx.spellsCap, this);
    }

    public final Elements getElement() {
        return this.immutableConfigs.element();
    }

    public abstract List<ITextComponent> GetDescription(TooltipInfo info, SpellCastContext ctx);

    public abstract Words getName();

    public final boolean cast(SpellCastContext ctx) {
        boolean bool = immutableConfigs.castType()
            .cast(ctx);

        ctx.castedThisTick = true;

        if (getImmutableConfigs().getSwingsArmOnCast()) {
            ctx.caster.func_226292_a_(Hand.MAIN_HAND, true);
        }

        castExtra(ctx);
        return bool;
    }

    public void castExtra(SpellCastContext ctx) {

    }

    public void spendResources(SpellCastContext ctx) {
        ctx.data.getResources()
            .modify(getManaCostCtx(ctx));
    }

    public ResourcesData.Context getManaCostCtx(SpellCastContext ctx) {

        float cost = 0;

        cost += this.getCalculatedManaCost(ctx);

        return new ResourcesData.Context(
            ctx.data, ctx.caster, ResourcesData.Type.MANA, cost, ResourcesData.Use.SPEND);
    }

    public boolean canCast(SpellCastContext ctx) {

        LivingEntity caster = ctx.caster;

        if (caster instanceof PlayerEntity == false) {
            return true;
        }

        PlayerEntity player = (PlayerEntity) caster;

        if (!caster.world.isRemote) {

            UnitData data = Load.Unit(caster);

            if (data != null) {

                ResourcesData.Context rctx = getManaCostCtx(ctx);

                if (data.getResources()
                    .hasEnough(rctx)) {

                    if (immutableConfigs.castRequirements()
                        .stream()
                        .anyMatch(x -> !x.predicate.test(player))) {
                        return false;
                    }

                    return true;
                } else {
                    if (caster instanceof ServerPlayerEntity) {
                        MMORPG.sendToClient(new NoManaPacket(), (ServerPlayerEntity) caster);
                    }

                }
            }
        }
        return false;

    }

    @Override
    public final List<ITextComponent> GetTooltipString(TooltipInfo info) {

        SpellCastContext ctx = new SpellCastContext(info.player, 0, this);

        UnitData data = info.unitdata;

        List<ITextComponent> list = new ArrayList<>();

        TooltipUtils.addEmpty(list);

        list.addAll(GetDescription(info, ctx));

        TooltipUtils.addEmpty(list);

        list.add(new StringTextComponent(TextFormatting.BLUE + "Mana Cost: " + getCalculatedManaCost(ctx)));
        list.add(new StringTextComponent(TextFormatting.YELLOW + "Cooldown: " + getCooldownInSeconds(ctx) + "s"));
        list.add(new StringTextComponent(TextFormatting.GREEN + "Cast time: " + getUseDurationInSeconds(ctx) + "s"));

        TooltipUtils.addEmpty(list);

        this.immutableConfigs.castRequirements()
            .forEach(x -> list.add(x.text));

        TooltipUtils.addEmpty(list);
        this.onDamageEffects.forEach(x -> {
            list.add(new SText(x.chance + "% Chance to apply " + x.effect.locNameForLangFile()));
        });

        TooltipUtils.addEmpty(list);

        finishTooltip(list, ctx, info);

        return list;

    }

}
