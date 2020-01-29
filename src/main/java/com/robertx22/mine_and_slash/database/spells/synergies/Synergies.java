package com.robertx22.mine_and_slash.database.spells.synergies;

import com.robertx22.mine_and_slash.database.spells.synergies.cleric.InstantHealRemoveDebuffSynergy;
import com.robertx22.mine_and_slash.database.spells.synergies.cleric.RighteousFuryAoeSynergy;
import com.robertx22.mine_and_slash.database.spells.synergies.druid.ThornArmorThornsSynergy;
import com.robertx22.mine_and_slash.database.spells.synergies.druid.ThornBushMajorThornsSynergy;
import com.robertx22.mine_and_slash.database.spells.synergies.ember_mage.FireballBurnConsumeSynergy;
import com.robertx22.mine_and_slash.database.spells.synergies.ember_mage.MagmaFlowerHealSynergy;
import com.robertx22.mine_and_slash.database.spells.synergies.ember_mage.VolcanoBurnSynergy;
import com.robertx22.mine_and_slash.database.spells.synergies.ocean_mystic.*;
import com.robertx22.mine_and_slash.database.spells.synergies.shaman.LightningTotemStaticSynergy;
import com.robertx22.mine_and_slash.database.spells.synergies.shaman.ThunderSpearLightningStrikeSynergy;
import com.robertx22.mine_and_slash.database.spells.synergies.shaman.ThunderSpearThunderEssenceSynergy;

public class Synergies {

    public static HeartOfIceFrostSynergy HEART_OF_ICE_FROST = new HeartOfIceFrostSynergy();
    public static FrostballExtraDmgSynergy FROSTBALL_EXTRA_DMG = new FrostballExtraDmgSynergy();
    public static FrostballFrostEssenceGenSynergy FROSTBALL_FROST_ESSENCE_GEN = new FrostballFrostEssenceGenSynergy();
    public static WhirlpoolFrostDmgSynergy WHIRLPOOL_FROST_DMG = new WhirlpoolFrostDmgSynergy();
    public static BlizzardFrostSynergy BLIZZARD_FROST = new BlizzardFrostSynergy();
    public static WhirlpoolShiverSynergy WHIRLPOOL_SHIVER = new WhirlpoolShiverSynergy();

    public static ThunderSpearLightningStrikeSynergy THUNDER_SPEAR_LIGHTNING = new ThunderSpearLightningStrikeSynergy();
    public static LightningTotemStaticSynergy LIGHTNING_TOTEM_STATIC = new LightningTotemStaticSynergy();
    public static ThunderSpearThunderEssenceSynergy THUNDER_SPEAR_ESSENCE = new ThunderSpearThunderEssenceSynergy();

    public static ThornArmorThornsSynergy THORN_ARMOR_THORNS = new ThornArmorThornsSynergy();
    public static ThornBushMajorThornsSynergy THORN_BUSH_MAJOR_TORNS = new ThornBushMajorThornsSynergy();

    public static RighteousFuryAoeSynergy RIGHTEOUS_FURY_AOE = new RighteousFuryAoeSynergy();
    public static InstantHealRemoveDebuffSynergy INSTANT_HEAL_REMOVE_DEBUFF = new InstantHealRemoveDebuffSynergy();

    public static FireballBurnConsumeSynergy FIREBALL_BURN_CONSUME = new FireballBurnConsumeSynergy();
    public static VolcanoBurnSynergy VOLCANO_BURN = new VolcanoBurnSynergy();
    public static MagmaFlowerHealSynergy MAGMA_FLOWER_HEAL = new MagmaFlowerHealSynergy();
}