package pixlepix.auracascade.item;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import com.google.common.collect.HashMultimap;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import pixlepix.auracascade.data.AuraQuantity;
import pixlepix.auracascade.data.EnumAura;
import pixlepix.auracascade.data.recipe.PylonRecipe;
import pixlepix.auracascade.data.recipe.PylonRecipeComponent;
import pixlepix.auracascade.registry.ITTinkererItem;
import pixlepix.auracascade.registry.ThaumicTinkererRecipe;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by localmacaccount on 2/1/15.
 */
public class ItemOrangeAmulet extends Item implements IBauble, ITTinkererItem {
    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.AMULET;
    }

    @Override
    public void onWornTick(ItemStack itemStack, EntityLivingBase entityLivingBase) {

    }

    @Override
    public void onEquipped(ItemStack itemStack, EntityLivingBase entityLivingBase) {

    }

    @Override
    public void onUnequipped(ItemStack itemStack, EntityLivingBase entityLivingBase) {

    }

    @Override
    public boolean canEquip(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        return true;
    }

    @Override
    public boolean canUnequip(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        return true;
    }

    @Override
    public ArrayList<Object> getSpecialParameters() {
        return null;
    }

    @Override
    public void registerIcons(IIconRegister register) {
        itemIcon = register.registerIcon("aura:amuletOrange");
    }

    @Override
    public String getItemName() {
        return "orangeAmulet";
    }

    @Override
    public boolean shouldRegister() {
        return true;
    }

    @Override
    public boolean shouldDisplayInTab() {
        return true;
    }


    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }


    @Override
    public ThaumicTinkererRecipe getRecipeItem() {
        return new PylonRecipe(new ItemStack(this), new PylonRecipeComponent(new AuraQuantity(EnumAura.WHITE_AURA, 200000), ItemAuraCrystal.getCrystalFromAura(EnumAura.ORANGE_AURA)));
    }
}