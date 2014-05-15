package net.gtn.dimensionalpocket.common.core;

import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.block.BlockDimensionalPocketFrame;
import net.gtn.dimensionalpocket.common.core.teleport.TeleportingRegistry;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class PocketDimensionHelper {

    public static void teleportPlayerToPocket(EntityPlayer entityPlayer, CoordSet chunkSet) {
        if (entityPlayer.worldObj.isRemote || !(entityPlayer instanceof EntityPlayerMP))
            return;

        EntityPlayerMP player = (EntityPlayerMP) entityPlayer;

        int dimID = player.dimension;
        
        PocketTeleporter teleporter = new PocketTeleporter(MinecraftServer.getServer().worldServerForDimension(dimID), chunkSet);

        if (dimID != Reference.DIMENSION_ID) {
            transferPlayerToDimension(player, Reference.DIMENSION_ID, teleporter);
        } else {
            teleporter.placeInPortal(player, 0, 0, 0, 0);
        }

        generatePocketIfNecessary(player, chunkSet);
    }
    
    /**
     * Generates the new room. THATS why you hired me :D
     * @author NPException
     * @param world
     * @param chunkSet
     */
    private static void generatePocketIfNecessary(EntityPlayerMP player, CoordSet chunkSet) {
        
        if (player.worldObj.getBlock(chunkSet.getX()*16, chunkSet.getY()*16, chunkSet.getZ()*16) == ModBlocks.dimensionalPocketFrame) {
            return;
        }
        
        World world = player.worldObj;
        
        int worldX = chunkSet.getX()*16;
        int worldY = chunkSet.getY()*16;
        int worldZ = chunkSet.getZ()*16;
        
        Chunk chunk = world.getChunkFromChunkCoords(chunkSet.getX(), chunkSet.getZ());
        
        int l = worldY >> 4; 
        ExtendedBlockStorage extendedBlockStorage = chunk.getBlockStorageArray()[l];

        if (extendedBlockStorage == null) {
            extendedBlockStorage = new ExtendedBlockStorage(worldY, !world.provider.hasNoSky);
            chunk.getBlockStorageArray()[l] = extendedBlockStorage;
        }
        
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                for (int z = 0; z < 16; z++) {
                    if (!(x == 0 || x == 15 || y == 0 || y == 15 || z == 0 || z == 15)) {
                        continue;
                    }
                    extendedBlockStorage.func_150818_a(x, y, z, ModBlocks.dimensionalPocketFrame);
                    
                    world.markBlockForUpdate(worldX+x, worldY+y, worldZ+z); 
                    
                    // use that method if setting things in the chunk will cause problems in the future
//                    world.setBlock(worldX+x, worldY+y, worldZ+z, ModBlocks.dimensionalPocketFrame);
                }
            }
        }
    }

    public static void transferPlayerToDimension(EntityPlayerMP player, int dimID, Teleporter teleporter) {
        MinecraftServer.getServer().getConfigurationManager().transferPlayerToDimension(player, dimID, teleporter);
    }
    
    public static void teleportPlayerFromPocket(EntityPlayer entityPlayer, CoordSet frameCoords){
        if (entityPlayer.worldObj.isRemote || !(entityPlayer instanceof EntityPlayerMP))
            return;
        
        EntityPlayerMP player = (EntityPlayerMP) entityPlayer;

        // adjust to chunk coordinates
        
        TeleportLink link = TeleportingRegistry.getLinkForPocketChunkCoords(frameCoords.copyDividedBy16());
        
    }
}
