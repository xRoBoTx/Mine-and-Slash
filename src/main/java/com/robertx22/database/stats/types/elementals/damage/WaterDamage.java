package com.robertx22.database.stats.types.elementals.damage;

import com.robertx22.enums.Elements;
import com.robertx22.stats.Stat;

public class WaterDamage extends Stat {
	public WaterDamage() {
	}

	@Override
	public String Name() {
		return "Water Damage";
	}

	@Override
	public boolean ScalesToLevel() {
		return true;
	}

	@Override
	public Elements Element() {
		return Elements.Water;
	}

	@Override
	public boolean IsPercent() {
		return false;
	}

}