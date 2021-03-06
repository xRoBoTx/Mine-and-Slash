package com.robertx22.mine_and_slash.database.data.stats.types.core_stats.base;

import com.robertx22.mine_and_slash.database.data.IGUID;
import com.robertx22.mine_and_slash.uncommon.capability.entity.EntityCap;

public interface IAddToOtherStats extends IGUID {

    void addToOtherStats(EntityCap.UnitData unit, float v1, float v2);

}
