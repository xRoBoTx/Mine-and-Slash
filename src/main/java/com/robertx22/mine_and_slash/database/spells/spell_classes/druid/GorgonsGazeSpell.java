package com.robertx22.mine_and_slash.database.spells.spell_classes.druid;

import com.robertx22.mine_and_slash.database.spells.spell_classes.bases.BaseSpell;
import com.robertx22.mine_and_slash.mmorpg.registers.common.Sounds;
import com.robertx22.mine_and_slash.potion_effects.bases.PotionEffectUtils;
import com.robertx22.mine_and_slash.potion_effects.druid.PetrifyEffect;
import com.robertx22.mine_and_slash.saveclasses.gearitem.gear_bases.TooltipInfo;
import com.robertx22.mine_and_slash.saveclasses.spells.calc.SpellCalcData;
import com.robertx22.mine_and_slash.uncommon.enumclasses.Elements;
import com.robertx22.mine_and_slash.uncommon.enumclasses.SpellSchools;
import com.robertx22.mine_and_slash.uncommon.localization.Words;
import com.robertx22.mine_and_slash.uncommon.utilityclasses.EntityFinder;
import com.robertx22.mine_and_slash.uncommon.utilityclasses.SoundUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class GorgonsGazeSpell extends BaseSpell {

    private GorgonsGazeSpell() {
    }

    public static GorgonsGazeSpell getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public SpellSchools getSchool() {
        return SpellSchools.DRUID;
    }

    @Override
    public int getCooldownInSeconds() {
        return 50;
    }

    @Override
    public BaseSpell.SpellType getSpellType() {
        return SpellType.Aoe_Debuff;
    }

    @Override
    public String GUID() {
        return "gorgons_gaze";
    }

    @Override
    public int getManaCost() {
        return 30;
    }

    @Override
    public int useTimeTicks() {
        return 20;
    }

    @Override
    public SpellCalcData getCalculation() {
        return PetrifyEffect.CALC;
    }

    @Override
    public Elements getElement() {
        return Elements.Nature;
    }

    @Override
    public List<ITextComponent> GetDescription(TooltipInfo info) {

        List<ITextComponent> list = new ArrayList<>();

        list.add(new StringTextComponent("Turn all enemies before you into stone: "));
        list.add(new StringTextComponent("Applies debuff: "));
        list.add(new StringTextComponent(""));

        list.addAll(PetrifyEffect.INSTANCE.GetTooltipString(info));

        return list;

    }

    @Override
    public Words getName() {
        return Words.GorgonsGaze;
    }

    @Override
    public boolean cast(LivingEntity caster, int ticksInUse) {

        World world = caster.world;

        SoundUtils.playSound(caster, Sounds.STONE_CRACK, 1, 1);

        EntityFinder.start(caster, LivingEntity.class, caster.getPositionVector())
            .radius(3)
            .distance(15)
            .finder(EntityFinder.Finder.IN_FRONT)
            .build()
            .forEach(x -> PotionEffectUtils.apply(PetrifyEffect.INSTANCE, caster, x));

        return true;
    }

    private static class SingletonHolder {
        private static final GorgonsGazeSpell INSTANCE = new GorgonsGazeSpell();
    }
}
