package com.robertx22.mine_and_slash.data_generation.mob_affixes;

import com.robertx22.mine_and_slash.data_generation.BaseDataPackManager;
import com.robertx22.mine_and_slash.database.mob_affixes.base.MobAffix;
import com.robertx22.mine_and_slash.db_lists.initializers.MobAffixes;
import com.robertx22.mine_and_slash.onevent.data_gen.providers.SlashDataProvider;
import com.robertx22.mine_and_slash.registry.SlashRegistry;
import com.robertx22.mine_and_slash.registry.SlashRegistryType;
import net.minecraft.data.DataGenerator;

public class MobAffixDataPackManager extends BaseDataPackManager<MobAffix> {
    static String ID = "mob_affix";

    public MobAffixDataPackManager() {
        super(SlashRegistryType.MOB_AFFIX, ID, x -> MobAffixes.EMPTY
            .fromJson(x));
    }

    @Override
    public SlashDataProvider getDataPackCreator(DataGenerator gen) {
        return new SlashDataProvider<MobAffix>(gen, SlashRegistry.MobAffixes()
            .getSerializable(), ID);
    }
}
