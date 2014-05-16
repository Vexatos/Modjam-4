package net.gtn.dimensionalpocket.common.core;

import net.gtn.dimensionalpocket.common.core.utils.CoordSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class PocketTeleporter extends Teleporter {

    CoordSet targetSet;
    boolean intoPocket;

    public PocketTeleporter(WorldServer worldServer, CoordSet targetSet, boolean intoPocket) {
        super(worldServer);
        this.targetSet = targetSet.copy();
        this.intoPocket = intoPocket;
    }

    @Override
    public void placeInPortal(Entity entity, double x, double y, double z, float par8) {

        if (!(entity instanceof EntityPlayerMP))
            return;

        EntityPlayerMP player = (EntityPlayerMP) entity;
        World world = player.worldObj;

        double posX = targetSet.getX();
        double posY = targetSet.getY();
        double posZ = targetSet.getZ();

        CoordSet airSet = targetSet.copy();

        if (intoPocket) {
            posX = posX * 16 + 8;
            posY = posY * 16;
            posZ = posZ * 16 + 8;
        }

        if (!(isAirBlocks(world, airSet))) {
            
        }

        player.playerNetServerHandler.setPlayerLocation(posX + 0.5, posY + 1, posZ + 0.5, player.rotationYaw, player.rotationPitch);
    }

    private boolean isAirBlocks(World world, CoordSet airSet) {
        return (world.isAirBlock(airSet.getX(), airSet.getY(), airSet.getZ()) && world.isAirBlock(airSet.getX(), airSet.getY() + 1, airSet.getZ()));
    }

}
