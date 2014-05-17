package net.gtn.dimensionalpocket.common.block;

import net.gtn.dimensionalpocket.common.ModBlocks;
import net.gtn.dimensionalpocket.common.ModItems;
import net.gtn.dimensionalpocket.common.block.framework.BlockDP;
import net.gtn.dimensionalpocket.common.core.pocket.Pocket;
import net.gtn.dimensionalpocket.common.core.pocket.PocketRegistry;
import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.gtn.dimensionalpocket.common.core.utils.DPLogger;
import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockDimensionalPocketFrame extends BlockDP {

    public BlockDimensionalPocketFrame(Material material, String name) {
        super(material, name);
        setBlockUnbreakable();
        setResistance(6000000.0F);
        setLightLevel(1);
        disableStats();
        setCreativeTab(null);
    }

    @Override
    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
        return false;
    }

    @Override
    public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
    }

    @Override
    public boolean isNormalCube() {
        return true;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
        CoordSet coordSet = new CoordSet(x, y, z);
        Pocket pocket = PocketRegistry.getPocket(coordSet.toChunkCoords());

        DPLogger.info("HALPs");
        if (pocket == null)
            return 0;

        World srcWorld = MinecraftServer.getServer().worldServerForDimension(pocket.getBlockDim());
        ForgeDirection direction = ForgeDirection.getOrientation(side);

        int power = pocket.getSideState(srcWorld, direction);
        

        return power;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitVecX, float hitVecY, float hitVecZ) {
        if (player == null)
            return false;

        ItemStack itemStack = player.getCurrentEquippedItem();

        if (itemStack != null) {
            if (itemStack.getItem() == ModItems.craftingItems && (itemStack.getItemDamage() == 0 || itemStack.getItemDamage() == 1)) {
                if (player.dimension != Reference.DIMENSION_ID || world.isRemote)
                    return false;

                CoordSet coordSet = new CoordSet(x, y, z);

                Pocket pocket = PocketRegistry.getPocket(coordSet.toChunkCoords());
                if (pocket == null)
                    return false;

                boolean setSpawn = pocket.setSpawnSet(coordSet.asSpawnPoint());

                if (setSpawn)
                    player.inventory.decrStackSize(player.inventory.currentItem, 1);
                return true;
            }
            return false;
        }

        if (!player.isSneaking())
            return false;

        if (!world.isRemote) {
            if (player.dimension != Reference.DIMENSION_ID)
                return false;

            Pocket pocket = PocketRegistry.getPocket(new CoordSet(x, y, z).asChunkCoords());
            if (pocket == null)
                return false;

            pocket.teleportFrom(player);
        }

        return true;
    }

    @Override
    public boolean renderWithModel() {
        return false;
    }

    @Override
    public TileEntity getTileEntity(int metadata) {
        return null;
    }
}
