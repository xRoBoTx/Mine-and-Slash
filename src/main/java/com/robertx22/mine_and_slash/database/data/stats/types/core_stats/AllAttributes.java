package com.robertx22.mine_and_slash.database.data.stats.types.core_stats;

import com.robertx22.mine_and_slash.database.data.stats.Stat;
import com.robertx22.mine_and_slash.database.data.stats.StatScaling;
import com.robertx22.mine_and_slash.database.data.stats.types.core_stats.base.BaseCoreStat;
import com.robertx22.mine_and_slash.database.data.stats.types.core_stats.base.IAddToOtherStats;
import com.robertx22.mine_and_slash.uncommon.capability.entity.EntityCap;
import com.robertx22.mine_and_slash.uncommon.enumclasses.Elements;

import java.util.Arrays;
import java.util.List;

public class AllAttributes extends Stat implements IAddToOtherStats {

    private AllAttributes() {
    }

    @Override
    public boolean IsShownOnStatGui() {
        return false;
    }

    @Override
    public StatScaling getScaling() {
        return StatScaling.LINEAR;
    }

    public static AllAttributes getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public StatGroup statGroup() {
        return StatGroup.CoreStat;
    }

    @Override
    public boolean IsPercent() {
        return false;
    }

    @Override
    public Elements getElement() {
        return null;
    }

    public List<BaseCoreStat> coreStatsThatBenefit() {
        return Arrays.asList(Dexterity.INSTANCE, Intelligence.INSTANCE, Strength.INSTANCE);
    }

    @Override
    public String locDescForLangFile() {
        return "Adds to Dexterity, Intelligence and Strength.";
    }

    @Override
    public String locNameForLangFile() {
        return "All Attributes";
    }

    @Override
    public String GUID() {
        return "all_attributes";
    }

    @Override
    public void addToOtherStats(EntityCap.UnitData unit, float v1, float v2) {
        coreStatsThatBenefit().forEach(x -> {
            unit.getUnit()
                .getCreateStat(x)
                .addAlreadyScaledFlat(v1, v2);
        });
    }

    private static class SingletonHolder {
        private static final AllAttributes INSTANCE = new AllAttributes();
    }
}



