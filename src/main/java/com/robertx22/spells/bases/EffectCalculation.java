package com.robertx22.spells.bases;

import com.robertx22.saveclasses.Unit;

public class EffectCalculation {

	public String Stat;
	public float Multi;

	public EffectCalculation(String stat, float multi) {
		super();
		Stat = stat;
		Multi = multi;
	}

	public int GetValue(Unit unit) {

		return (int) (unit.Stats.get(Stat).Value * Multi);

	}

}
