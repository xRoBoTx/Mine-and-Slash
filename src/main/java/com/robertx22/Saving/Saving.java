package com.robertx22.saving;

import com.google.gson.Gson;

import net.minecraft.nbt.NBTTagCompound;

public class Saving {

	private static Gson gson = new Gson();
	
	public static NBTTagCompound Save(NBTTagCompound nbt, Object thing) {

		if (nbt == null) {
			nbt = new NBTTagCompound();
		}

		nbt.setString("Data", gson.toJson(thing));

		return nbt;

	}

	public static <T> T Load(NBTTagCompound nbt, Class type) {

		return (T) gson.fromJson(nbt.getString("Data"), type);
	}
}
