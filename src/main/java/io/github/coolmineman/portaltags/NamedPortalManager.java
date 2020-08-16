package io.github.coolmineman.portaltags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Map.Entry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

public class NamedPortalManager {
    private NamedPortalManager() {
    }

    private static HashMap<String, ArrayList<Long>> overworldPortals = new HashMap<>();
    private static HashMap<String, ArrayList<Long>> netherPortals = new HashMap<>();

    public static void fromTag(CompoundTag tag) {
        int managerVersion = tag.getInt("ManagerVersion");
        if (managerVersion == 0) {
            CompoundTag overworldPortalTags = tag.getCompound("overworldPortals");
            for (String key : overworldPortalTags.getKeys()) {
                long[] l = overworldPortalTags.getLongArray(key);
                ArrayList<Long> arrayList = new ArrayList<>();
                for (long l1 : l) {
                    putIfAbsent(arrayList, l1); // Box Long
                }
                overworldPortals.put(key, arrayList);
            }
            CompoundTag netherPortalTags = tag.getCompound("netherPortals");
            for (String key : netherPortalTags.getKeys()) {
                long[] l = netherPortalTags.getLongArray(key);
                ArrayList<Long> arrayList = new ArrayList<>();
                for (long l1 : l) {
                    putIfAbsent(arrayList, l1); // Box Long
                }
                netherPortals.put(key, arrayList);
            }
        }
    }

    public static CompoundTag toTag() {
        CompoundTag result = new CompoundTag();
        result.putInt("ManagerVersion", 0);
        CompoundTag overworldPortalTags = new CompoundTag();
        for (Entry<String, ArrayList<Long>> e : overworldPortals.entrySet()) {
            overworldPortalTags.put(e.getKey(), new LongArrayTag(e.getValue()));
        }
        result.put("overworldPortals", overworldPortalTags);
        CompoundTag netherPortalTags = new CompoundTag();
        for (Entry<String, ArrayList<Long>> e : netherPortals.entrySet()) {
            netherPortalTags.put(e.getKey(), new LongArrayTag(e.getValue()));
        }
        result.put("netherPortals", netherPortalTags);
        return result;
    }

    //TODO Maybe Unhardcode The Dimenions and use destination Somehow

    public static Optional<TeleportTarget> getPortal(Entity e, ServerWorld destination) {
        BlockPos a = e.getBlockPos();
        BlockPos[] blocksToCheck = {
            a.add(0d, 0d, 0d),
            a.add(0d, 0d, 1d),
            a.add(0d, 0d, -1d),
            a.add(0d, 1d, 0d),
            a.add(0d, -1d, 0d),
            a.add(1d, 0d, 0d),
            a.add(-1d, 0d, 0d),
        };
        for (BlockPos p : blocksToCheck) {
            BlockEntity be = e.getEntityWorld().getBlockEntity(p);
            if (be instanceof NetherPortalBlockEntity) {
                NetherPortalBlockEntity be1 = (NetherPortalBlockEntity)be;
                if (be1.hasTag) {
                    if (e.getEntityWorld().getRegistryKey() == World.NETHER) {
                        List<Long> owPortals = getOverWorldPortals(be1.getName());
                        if (owPortals.size() == 1) {
                            return Optional.of(new TeleportTarget(Vec3d.ofCenter(BlockPos.fromLong(owPortals.get(0))), e.getVelocity(), e.getHeadYaw(), e.getPitch(0)));
                        }
                        if (!owPortals.isEmpty()) {
                            Collections.shuffle(owPortals);
                            return Optional.of(new TeleportTarget(Vec3d.ofCenter(BlockPos.fromLong(owPortals.get(0))), e.getVelocity(), e.getHeadYaw(), e.getPitch(0)));
                        }
                    } else if (e.getEntityWorld().getRegistryKey() == World.OVERWORLD) {
                        List<Long> nPortals = getNetherPortals(be1.getName());
                        if (nPortals.size() == 1) {
                            return Optional.of(new TeleportTarget(Vec3d.ofCenter(BlockPos.fromLong(nPortals.get(0))), e.getVelocity(), e.getHeadYaw(), e.getPitch(0)));
                        }
                        if (!nPortals.isEmpty()) {
                            Collections.shuffle(nPortals);
                            return Optional.of(new TeleportTarget(Vec3d.ofCenter(BlockPos.fromLong(nPortals.get(0))), e.getVelocity(), e.getHeadYaw(), e.getPitch(0)));
                        }
                    }
                }
            }
        }
        return Optional.empty();
    }

    private static void putIfAbsent(ArrayList a, Object b) {
        if (!a.contains(b)) a.add(b);
    }

    public static void addOverworldPortal(String name, BlockPos cornerPos) {
        putIfAbsent(overworldPortals.computeIfAbsent(name, no -> new ArrayList<>()), (cornerPos.asLong()));
    }

    public static void addNetherPortal(String name, BlockPos cornerPos) {
        putIfAbsent(netherPortals.computeIfAbsent(name, no -> new ArrayList<>()), (cornerPos.asLong()));
    }

    public static void removeOverworldPortal(String name, BlockPos cornerPos) {
        overworldPortals.computeIfAbsent(name, no -> new ArrayList<>()).remove(cornerPos.asLong());
    }

    public static void removeNetherPortal(String name, BlockPos cornerPos) {
        netherPortals.computeIfAbsent(name, no -> new ArrayList<>()).remove(cornerPos.asLong());
    }

    public static List<Long> getOverWorldPortals(String name) {
        return overworldPortals.computeIfAbsent(name, no -> new ArrayList<>());
    }

    public static List<Long> getNetherPortals(String name) {
        return netherPortals.computeIfAbsent(name, no -> new ArrayList<>());
    }
}