package com.robertx22.mine_and_slash.vanilla_mc.packets.particles;

import com.robertx22.mine_and_slash.uncommon.enumclasses.RGB;
import info.loenwind.autosave.annotations.Storable;
import info.loenwind.autosave.annotations.Store;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.registries.ForgeRegistries;

@Storable
public class ParticlePacketData {

    private ParticlePacketData() {

    }

    public static ParticlePacketData empty() {
        return new ParticlePacketData();
    }

    @Store
    public double x = 1;
    @Store
    public double y = 1;
    @Store
    public double z = 1;

    @Store
    public boolean isVecPos = false;

    @Store
    public double mx = 1;
    @Store
    public double my = 1;
    @Store
    public double mz = 1;

    @Store
    public ParticleEnum type;

    @Store
    public float radius = 1;

    @Store
    public int amount = 1;

    @Store
    public RGB color;

    @Store
    public String particleID;

    public IParticleData getParticleType() {
        return (IParticleData) ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation(particleID));
    }

    public ParticlePacketData motion(Vec3d v) {
        mx = v.x;
        my = v.y;
        mz = v.z;
        return this;
    }

    public ParticlePacketData type(ParticleType type) {
        this.particleID = type.getRegistryName()
            .toString();
        return this;
    }

    public ParticlePacketData amount(int num) {
        this.amount = num;
        return this;
    }

    public ParticlePacketData radius(double rad) {
        this.radius = (float) rad;
        return this;
    }

    public ParticlePacketData radius(float rad) {
        this.radius = rad;
        return this;
    }

    public ParticlePacketData color(RGB color) {
        this.color = color;
        return this;
    }

    public Vec3d getPos() {
        return new Vec3d(x, y, z);
    }

    public BlockPos getBlockPos() {
        return new BlockPos(x, y, z);
    }

    public ParticlePacketData(Vec3d pos, ParticleEnum type) {
        x = pos.getX();
        y = pos.getY();
        z = pos.getZ();
        this.isVecPos = true;
        this.type = type;
    }

    public ParticlePacketData(BlockPos pos, ParticleEnum type) {
        x = pos.getX();
        y = pos.getY();
        z = pos.getZ();
        this.type = type;
    }
}
