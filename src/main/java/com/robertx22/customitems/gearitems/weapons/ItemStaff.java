package com.robertx22.customitems.gearitems.weapons;

import java.util.HashMap;

import javax.annotation.Nonnull;

import com.robertx22.customitems.gearitems.bases.BaseStaff;
import com.robertx22.customitems.gearitems.bases.IWeapon;
import com.robertx22.db_lists.Rarities;
import com.robertx22.spells.EntityStaffProjectileNormal;
import com.robertx22.spells.aoe_projectile.AcidExplosion.EffectAcidExplosion;
import com.robertx22.uncommon.capability.EntityData.UnitData;
import com.robertx22.uncommon.datasaving.Load;
import com.robertx22.uncommon.utilityclasses.RegisterUtils;
import com.robertx22.uncommon.utilityclasses.SoundUtils;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class ItemStaff extends BaseStaff implements IWeapon {
	public static HashMap<Integer, Item> Items = new HashMap<Integer, Item>();

	public ItemStaff(int rarity, HashMap<Integer, Item> map) {
		super(rarity, map);

	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		Rarities.Items.forEach((x) -> Items.put(x.Rank(), new ItemStaff(x.Rank(), Items)));
		Items.values().forEach((x) -> event.getRegistry().register(x));
	}

	@SubscribeEvent
	public static void onModelRegistry(ModelRegistryEvent event) {
		Items.values().forEach((x) -> RegisterUtils.registerRender(x));
	}

	@Override
	public String Name() {
		return "Staff";
	}

	@Override
	public int GetEnergyCost() {
		return 6;
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		player.swingArm(hand);

		try {

			if (!world.isRemote) {

				UnitData data = Load.Unit(player);

				if (data.getUnit().energyData().CurrentValue > this.GetEnergyCost()) {

					data.getUnit().energyData().Decrease(this.GetEnergyCost());

					EntityStaffProjectileNormal projectile = new EntityStaffProjectileNormal(world, player);
					projectile.SetReady(player.getHeldItem(hand));
					projectile.SpawnAndShoot(new EffectAcidExplosion(), player);

					SoundUtils.playSoundAtPlayer(player, SoundEvents.ENTITY_SNOWBALL_THROW, 1, 1);

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
	}

	@Override
	public boolean Attack(EntityLivingBase source, EntityLivingBase target, UnitData unitsource, UnitData targetUnit) {

		return this.defaultAttack(source, target, unitsource, targetUnit);

	}
}