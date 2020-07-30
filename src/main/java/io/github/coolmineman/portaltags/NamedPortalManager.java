package io.github.coolmineman.portaltags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.util.math.BlockPos;

public class NamedPortalManager {
    private NamedPortalManager(){}

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
                    arrayList.add(l1); // Box Long
                }
                overworldPortals.put(key, arrayList);
            }
            CompoundTag netherPortalTags = tag.getCompound("netherPortals");
            for (String key : netherPortalTags.getKeys()) {
                long[] l = netherPortalTags.getLongArray(key);
                ArrayList<Long> arrayList = new ArrayList<>();
                for (long l1 : l) {
                    arrayList.add(l1); // Box Long
                }
                netherPortals.put(key, arrayList);
            }
        }
        System.out.println(overworldPortals);
        System.out.println(netherPortals);
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
        for (Entry<String, ArrayList<Long>> e : overworldPortals.entrySet()) {
            netherPortalTags.put(e.getKey(), new LongArrayTag(e.getValue()));
        }
        result.put("netherPortals", netherPortalTags);
        System.out.println(result);
        return result;
    }

    public static void addOverworldPortal(String name, BlockPos cornerPos) {
        overworldPortals.computeIfAbsent(name, no -> new ArrayList<>()).add(cornerPos.asLong());
    }

    public static void addNetherPortal(String name, BlockPos cornerPos) {
        netherPortals.computeIfAbsent(name, no -> new ArrayList<>()).add(cornerPos.asLong());
    }

    public static void removeOverworldPortal(String name, BlockPos cornerPos) {
        overworldPortals.computeIfAbsent(name, no -> new ArrayList<>()).remove(cornerPos.asLong());
    }

    public static void removeNetherPortal(String name, BlockPos cornerPos) {
        netherPortals.computeIfAbsent(name, no -> new ArrayList<>()).remove(cornerPos.asLong());
    }
}