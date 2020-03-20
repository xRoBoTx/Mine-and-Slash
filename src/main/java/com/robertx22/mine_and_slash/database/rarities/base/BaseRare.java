package com.robertx22.mine_and_slash.database.rarities.base;

import com.robertx22.mine_and_slash.database.MinMax;
import com.robertx22.mine_and_slash.saveclasses.gearitem.gear_bases.Rarity;
import com.robertx22.mine_and_slash.uncommon.enumclasses.RGB;
import com.robertx22.mine_and_slash.uncommon.interfaces.data_items.IRarity;
import net.minecraft.util.text.TextFormatting;

public abstract class BaseRare implements Rarity {
    @Override
    public int colorInt() {
        return 16777045;
    }

    @Override
    public String GUID() {

        return "Rare";
    }

    @Override
    public int Rank() {

        return IRarity.Rare;
    }

    @Override
    public MinMax SpawnDurabilityHit() {
        return new MinMax(70, 90);
    }

    @Override
    public TextFormatting textFormatColor() {
        return TextFormatting.BLUE;
    }

    @Override
    public String Color() {
        return TextFormatting.BLUE.toString();
    }

    @Override
    public RGB getRGBColor() {
        return new RGB(51, 102, 255);
    }

    @Override
    public String locNameForLangFile() {
        return "Rare";
    }
}
