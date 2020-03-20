package com.robertx22.mine_and_slash.saveclasses.item_classes;

import com.robertx22.mine_and_slash.config.forge.ModConfig;
import com.robertx22.mine_and_slash.database.rarities.GearRarity;
import com.robertx22.mine_and_slash.database.rarities.MapRarity;
import com.robertx22.mine_and_slash.database.world_providers.IWP;
import com.robertx22.mine_and_slash.db_lists.Rarities;
import com.robertx22.mine_and_slash.db_lists.bases.IBonusLootMulti;
import com.robertx22.mine_and_slash.dimensions.MapManager;
import com.robertx22.mine_and_slash.items.ores.ItemOre;
import com.robertx22.mine_and_slash.registry.SlashRegistry;
import com.robertx22.mine_and_slash.saveclasses.gearitem.StatModData;
import com.robertx22.mine_and_slash.saveclasses.gearitem.gear_bases.TooltipContext;
import com.robertx22.mine_and_slash.saveclasses.gearitem.gear_bases.TooltipInfo;
import com.robertx22.mine_and_slash.saveclasses.mapitem.MapAffixData;
import com.robertx22.mine_and_slash.uncommon.capability.entity.EntityCap;
import com.robertx22.mine_and_slash.uncommon.capability.entity.EntityCap.UnitData;
import com.robertx22.mine_and_slash.uncommon.datasaving.ItemType;
import com.robertx22.mine_and_slash.uncommon.datasaving.Load;
import com.robertx22.mine_and_slash.uncommon.datasaving.Map;
import com.robertx22.mine_and_slash.uncommon.enumclasses.AffectedEntities;
import com.robertx22.mine_and_slash.uncommon.interfaces.data_items.DataItemType;
import com.robertx22.mine_and_slash.uncommon.interfaces.data_items.ICommonDataItem;
import com.robertx22.mine_and_slash.uncommon.interfaces.data_items.IRarity;
import com.robertx22.mine_and_slash.uncommon.interfaces.data_items.ITiered;
import com.robertx22.mine_and_slash.uncommon.localization.CLOC;
import com.robertx22.mine_and_slash.uncommon.localization.Chats;
import com.robertx22.mine_and_slash.uncommon.localization.Styles;
import com.robertx22.mine_and_slash.uncommon.localization.Words;
import com.robertx22.mine_and_slash.uncommon.utilityclasses.EntityTypeUtils;
import com.robertx22.mine_and_slash.uncommon.utilityclasses.ParticleUtils;
import com.robertx22.mine_and_slash.uncommon.utilityclasses.RandomUtils;
import com.robertx22.mine_and_slash.uncommon.utilityclasses.TooltipUtils;
import info.loenwind.autosave.annotations.Storable;
import info.loenwind.autosave.annotations.Store;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.dimension.DimensionType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Storable
public class MapItemData implements ICommonDataItem<MapRarity>, IBonusLootMulti, Cloneable {

    public MapItemData() {

    }

    @Store
    public int level = 1;
    @Store
    public int tier = 0;
    @Store
    public int rarity = 0;
    @Store
    public boolean isExp = false;
    @Store
    public List<MapAffixData> affixes = new ArrayList<MapAffixData>();

    @Store
    public String mapUUID = UUID.randomUUID()
        .toString();

    @Override
    public void saveToStack(ItemStack stack) {
        Map.Save(stack, this);
    }

    public static MapItemData empty() {
        MapItemData map = new MapItemData();
        map.mapUUID = "error";
        return map;

    }

    @Override
    public MapItemData clone() {
        CompoundNBT nbt = new CompoundNBT();
        Map.Save(nbt, this);
        return Map.Load(nbt);
    }

    @Store
    public String worldGeneratorName = "dungeon";

    @Nonnull
    public IWP getIWP() {

        return SlashRegistry.WorldProviders()
            .get(this.worldGeneratorName);

    }

    @Override
    public float getBonusLootMulti() {
        if (isExp) {
            return 1;
        } else {
            return 1 + bonusFormula();
        }
    }

    public float bonusFormula() {
        return (1 * getAffixMulti());
    }

    public float getBonusExpMulti() {
        if (isExp) {
            return 1 + (bonusFormula() * 2);
        } else {
            return 1;
        }
    }

    public boolean increaseLevel(int i) {

        int lvl = level + i;

        if (lvl > ModConfig.INSTANCE.Server.MAXIMUM_PLAYER_LEVEL.get()) {
            return false;
        }

        level = lvl;

        return true;
    }

    public boolean increaseTier(int i) {

        int tier = this.tier + i;

        if (tier > ITiered.MAX_TIER) {
            return false;
        }

        this.tier = tier;

        return true;
    }

    private float getAffixMulti() {

        float total = 0;
        for (MapAffixData affix : affixes) {
            total += affix.getBonusLootMultiplier();
        }
        return total;
    }

    public static List<MapAffixData> getAllAffixesThatAffect(List<MapAffixData> affixes, LivingEntity entity) {

        AffectedEntities affected = AffectedEntities.All;

        if (entity instanceof PlayerEntity) {
            affected = AffectedEntities.Players;
        } else if (EntityTypeUtils.isMob(entity)) {
            affected = AffectedEntities.Mobs;
        }

        return getAllAffixesThatAffect(affixes, affected);

    }

    public static List<MapAffixData> getAllAffixesThatAffect(List<MapAffixData> affixes, AffectedEntities affected) {

        List<MapAffixData> list = new ArrayList<>();

        for (MapAffixData data : affixes) {
            if (data.affectedEntities.equals(affected)) {
                list.add(data);
            }
        }
        return list;
    }

    @Override
    public String getUniqueGUID() {
        return this.worldGeneratorName;
    }

    public List<MapAffixData> getAllAffixesThatAffect(AffectedEntities affected) {

        List<MapAffixData> list = new ArrayList<>();

        for (MapAffixData data : affixes) {
            if (data.affectedEntities.equals(affected)) {
                list.add(data);
            }
        }

        for (MapAffixData data : this.getIWP()
            .getMapAffixes()) {
            if (data.affectedEntities.equals(affected)) {
                list.add(data);
            }
        }

        return list;
    }

    public DimensionType setupPlayerMapData(BlockPos pos, PlayerEntity player) {

        UnitData unit = Load.Unit(player);

        ParticleUtils.spawnEnergyRestoreParticles(player, 10);

        player.sendMessage(Styles.GREENCOMP()
            .appendSibling(Chats.MapStarted.locName()));
/*
        Load.quests(player)
            .setMapQuest(SlashRegistry.Quests()
                .get(questGUID), this);


 */
        return MapManager.setupPlayerMapDimension(player, unit, this, pos);

    }

    @Override
    public ItemStack getSalvageResult(float salvageBonus) {

        int min = tryIncreaseAmount(salvageBonus, 2);
        int max = tryIncreaseAmount(salvageBonus, 4);

        ItemStack stack = ItemStack.EMPTY;

        if (RandomUtils.roll(this.getRarity()
            .salvageLotteryWinChance())) {

            Item item = SlashRegistry.CurrencyItems()
                .getWrapped()
                .ofCurrencyUsableOnItemType(ItemType.MAP)
                .random();

            stack = new ItemStack(item);
        } else {

            int amount = RandomUtils.RandomRange(min, max);

            ItemOre ore = (ItemOre) ItemOre.ItemOres.get(rarity);

            stack = new ItemStack(ore);
            stack.setCount(amount);

        }

        return stack;
    }

    @Override
    public DataItemType getDataType() {
        return DataItemType.MAP;
    }

    public List<ITextComponent> getTooltip() {
        List<ITextComponent> tooltip = new ArrayList<>();

        GearRarity rarity = Rarities.Gears.get(this.rarity);

        tooltip.add(TooltipUtils.level(this.level));
        TooltipUtils.addEmpty(tooltip);

        addAffixTypeToTooltip(this, tooltip, AffectedEntities.Mobs);
        addAffixTypeToTooltip(this, tooltip, AffectedEntities.Players);
        addAffixTypeToTooltip(this, tooltip, AffectedEntities.All);

        TooltipUtils.addEmpty(tooltip);

        try {
            tooltip.add(Styles.BLUECOMP()
                .appendSibling(Words.World_Type.locName())
                .appendText(": ")
                .appendSibling(this.getIWP()
                    .locName()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        TooltipUtils.addEmpty(tooltip);

        ITextComponent comp = TooltipUtils.rarityShort(rarity)
            .appendText(TextFormatting.GRAY + ", ")
            .appendSibling(Styles.YELLOWCOMP());

        boolean addedExp = false;
        if (getBonusExpAmountInPercent() > 0) {
            comp.appendSibling(Words.Exp.locName()
                .appendText(
                    ": +" + this.getBonusExpAmountInPercent() + "%"));
            addedExp = true;
        }

        if (getBonusLootAmountInPercent() > 0) {
            if (addedExp) {
                comp.appendText(", ");
            }

            comp.appendSibling(Words.Loot.locName()
                .appendText(
                    ": +" + this.getBonusLootAmountInPercent() + "%"));
        }
        comp.appendText(TextFormatting.GRAY + ", ")
            .appendSibling(Styles.GOLDCOMP()
                .appendSibling(Words.Tier.locName()
                    .appendText(": " + this.tier)));

        tooltip.add(comp);

        TooltipUtils.addEmpty(tooltip);

        tooltip.add(Styles.BLUECOMP()
            .appendSibling(CLOC.tooltip("put_in_mapdevice")));

        TooltipUtils.removeDoubleBlankLines(tooltip, 20);

        return tooltip;

    }

    @Override
    public void BuildTooltip(TooltipContext ctx) {
        if (ctx.data != null) {
            ctx.event.getToolTip()
                .addAll(getTooltip());
        }
    }

    private int getBonusLootAmountInPercent() {
        return (int) ((this.getBonusLootMulti() - 1) * 100);
    }

    private int getBonusExpAmountInPercent() {
        return (int) ((this.getBonusExpMulti() - 1) * 100);
    }

    private static void addAffixTypeToTooltip(MapItemData data, List<ITextComponent> tooltip,
                                              AffectedEntities affected) {

        List<MapAffixData> affixes = new ArrayList<>(data.getAllAffixesThatAffect(affected));

        if (affixes.size() == 0) {
            return;
        }

        ITextComponent str = new StringTextComponent("");

        if (affected.equals(AffectedEntities.Players)) {
            str.appendSibling(Words.Player_Affixes.locName());
        } else if (affected.equals(AffectedEntities.Mobs)) {
            str.appendSibling(Words.Mob_Affixes.locName());
        } else {
            str.appendSibling(Words.Affixes_Affecting_All.locName());
        }

        tooltip.add(Styles.GREENCOMP()
            .appendSibling(str));

        for (MapAffixData affix : affixes) {

            for (StatModData statmod : affix.getAffix()
                .Stats(affix.percent)) {

                TooltipInfo info = new TooltipInfo(
                    new EntityCap.DefaultImpl(), data.getRarity()
                    .StatPercents(), data.level);

                tooltip.addAll(statmod.GetTooltipString(info));

            }

        }
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, false);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, false);
    }

    @Override
    public int getRarityRank() {
        return MathHelper.clamp(rarity, -1, IRarity.Highest);
    }

    @Override
    public MapRarity getRarity() {
        return Rarities.Maps.get(rarity);
    }

    @Override
    public int Tier() {
        return this.tier;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public String getSpecificType() {
        return this.worldGeneratorName;
    }
}
