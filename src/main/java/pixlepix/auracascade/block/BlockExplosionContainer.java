package pixlepix.auracascade.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import pixlepix.auracascade.AuraCascade;
import pixlepix.auracascade.registry.ITTinkererBlock;
import pixlepix.auracascade.registry.ThaumicTinkererRecipe;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by localmacaccount on 6/5/15.
 */
public class BlockExplosionContainer extends Block implements ITTinkererBlock {

    String type;


    public BlockExplosionContainer() {
        super(Material.rock);
        //Same as obby
        setResistance(2000F);
        type = "Dirt";
        setTickRandomly(true);
    }

    public BlockExplosionContainer(String s) {
        this();
        type = s;
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
        super.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, meta);
        world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
        return meta;
    }

    @Override
    public int tickRate(World world) {
        return 300;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        super.updateTick(world, x, y, z, rand);
        if (rand.nextDouble() < getChanceToRepair()) {
            int meta = world.getBlockMetadata(x, y, z);
            if (meta > 0) {
                world.setBlockMetadataWithNotify(x, y, z, meta - 1, 3);

            }
        }
        world.scheduleBlockUpdate(x, y, z, this, tickRate(world) + rand.nextInt(5));
    }

    /**
     * Determines if this block should render in this pass.
     *
     * @param pass The pass in question
     * @return True to render
     */
    @Override
    public boolean canRenderInPass(int pass) {
        AuraCascade.proxy.renderPass = pass;
        return true;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    public double getChanceToResist() {
        if (type.equals("Dirt")) {
            return .1;
        }
        if (type.equals("Wood")) {
            return .25;
        }
        if (type.equals("Glass")) {
            return .25;
        }
        if (type.equals("Cobblestone")) {
            return .4;
        }
        if (type.equals("Stone")) {
            return .25;
        }
        if (type.equals("Obsidian")) {
            return .99;
        }
        return 0;
    }

    public double getChanceToRepair() {
        if (type.equals("Dirt")) {
            return .5;
        }
        if (type.equals("Wood")) {
            return .75;
        }
        if (type.equals("Glass")) {
            return .25;
        }
        if (type.equals("Cobblestone")) {
            return .5;
        }
        if (type.equals("Stone")) {
            return .25;
        }
        if (type.equals("Obsidian")) {
            return .05;
        }
        return 0;
    }

    @Override
    public void registerBlockIcons(IIconRegister register) {
        blockIcon = register.registerIcon("aura:fortifiedCobblestone");
    }

    @Override
    public ArrayList<Object> getSpecialParameters() {
        // TODO Auto-generated method stub
        ArrayList result = new ArrayList<Object>();
        result.add("Wood");
        result.add("Glass");
        result.add("Cobblestone");
        result.add("Stone");
        result.add("Obsidian");
        return result;
    }

    /**
     * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
     */
    @Override
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public String getBlockName() {
        return "fortified" + type;
    }

    @Override
    public boolean shouldRegister() {
        return true;
    }

    @Override
    public boolean shouldDisplayInTab() {
        return true;
    }

    /**
     * Gets the block's texture. Args: side, meta
     *
     * @param side
     * @param meta
     */
    @Override
    public IIcon getIcon(int side, int meta) {
        if (AuraCascade.proxy.renderPass == 1 && meta != 0) {
            return AuraCascade.proxy.breakingIcons[getCrackedStage(meta)];
        }
        return blockIcon;
    }

    public int getCrackedStage(int meta) {
        if (meta <= 5) {
            return meta - 1;
        } else {
            return (int) (4 + Math.ceil((meta - 5) / 2));
        }

    }

    @Override
    public Class<? extends ItemBlock> getItemBlock() {
        return null;
    }

    @Override
    public Class<? extends TileEntity> getTileEntity() {
        return null;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public ThaumicTinkererRecipe getRecipeItem() {
        return null;
    }

    @Override
    public int getCreativeTabPriority() {
        return 23;
    }
}