package pixlepix.auracascade.block.tile;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import pixlepix.auracascade.block.BlockMonitor;
import pixlepix.auracascade.data.AuraQuantity;
import pixlepix.auracascade.data.CoordTuple;
import pixlepix.auracascade.data.EnumAura;

/**
 * Created by pixlepix on 12/24/14.
 */
public class AuraTilePumpBase extends AuraTile {
    public int pumpPower;
    public int pumpSpeed;

    @Override
    protected void readCustomNBT(NBTTagCompound nbt) {
        super.readCustomNBT(nbt);
        pumpPower = nbt.getInteger("pumpPower");
        pumpSpeed = nbt.getInteger("pumpSpeed");
    }

    @Override
    protected void writeCustomNBT(NBTTagCompound nbt) {
        super.writeCustomNBT(nbt);
        nbt.setInteger("pumpPower", pumpPower);
        nbt.setInteger("pumpSpeed", pumpSpeed);
    }

    public boolean isAlternator(){
        return false;
    }

    @Override
    public boolean canTransfer(CoordTuple tuple, EnumAura aura) {
        return false;
    }

    @Override
    public boolean canReceive(CoordTuple source, EnumAura aura) {
        return source.getY() <= yCoord && super.canReceive(source, aura);
    }

    public void addFuel(int time, int speed) {
        if (time * speed > pumpSpeed * pumpPower) {
            pumpSpeed = speed;
            pumpPower = time;
            if(isAlternator()){
                pumpSpeed *= 3;
                
            }
        }
        updateMonitor(worldObj, xCoord, yCoord, zCoord);
    }

    public void updateMonitor(World w, int x, int y, int z) {
        for (ForgeDirection d1 : ForgeDirection.VALID_DIRECTIONS) {
            Block b = new CoordTuple(x, y, z).add(d1).getBlock(w);
            if (b instanceof BlockMonitor) {

                for (ForgeDirection d2 : ForgeDirection.VALID_DIRECTIONS) {
                    CoordTuple tuple = new CoordTuple(x, y, z).add(d2).add(d1);
                    Block b2 = tuple.getBlock(w);
                    b2.onNeighborBlockChange(w, tuple.getX(), tuple.getY(), tuple.getZ(), b);
                }
            }
        }
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!worldObj.isRemote && worldObj.getTotalWorldTime() % 20 == 2 && !worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)) {
            if (pumpPower > 0) {
                AuraTile upNode = null;
                for (int i = 1; i < 16; i++) {
                    TileEntity te = worldObj.getTileEntity(xCoord, yCoord + i, zCoord);
                    if (te instanceof AuraTile) {
                        upNode = (AuraTile) te;
                        break;
                    }
                }
                if (upNode != null) {

                    pumpPower--;
                    if (pumpPower == 0) {
                        updateMonitor(worldObj, xCoord, yCoord, zCoord);

                    }
                    for (EnumAura aura : EnumAura.values()) {
                        int dist = upNode.yCoord - yCoord;
                        int quantity = pumpSpeed / dist;
                        if(isAlternator()){
                            float f = getAlternatingFactor();
                            quantity *= f;
                        }
                        quantity *= storage.getComposition(aura);
                        quantity = aura.getRelativeMass(worldObj) == 0 ? 0 : (int) ((double) quantity / aura.getRelativeMass(worldObj));
                        quantity *= aura.getAscentBoost(worldObj);
                        quantity = Math.min(quantity, storage.get(aura));
                        burst(new CoordTuple(upNode), "magicCrit", aura, 1D);
                        storage.subtract(aura, quantity);
                        upNode.storage.add(new AuraQuantity(aura, quantity));
                    }
                }
            }
        }
    }
    
    public float getAlternatingFactor(){
        return (float) (1 + Math.sin(Math.PI * worldObj.getTotalWorldTime() / 10000))/2;
        
    }
}
