package net.gtn.dimensionalpocket.common.core;

import java.util.List;

import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

import com.google.common.collect.Lists;

public class BiomeHelper {

    private static BiomeGenBase pocketBiome;
    static boolean init = false;

    public static void init() {
        if (init) {
            DPLogger.severe("Tried calling BiomeHelper.init() again!");
            return;
        }
        init = true;

        pocketBiome = new BiomeGenBase(99) {
            @Override
            public boolean canSpawnLightningBolt() {
                return false;
            }

            @Override
            public List getSpawnableList(EnumCreatureType par1EnumCreatureType) {
                return Lists.newArrayList();
            }
        }.setBiomeName("Pocket Dimension").setDisableRain();

        BiomeDictionary.registerBiomeType(pocketBiome, Type.MAGICAL);
    }

    public static BiomeGenBase getPocketBiome() {
        return pocketBiome;
    }
}
