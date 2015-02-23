package com.lithial.me.enchantments;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import com.lithial.me.handlers.controls.KeyBind;
import com.lithial.me.handlers.utils.HorticultureHelper;

/**
 * Created by Lithial on 9/02/2015.
 */
public class EffectManager{
	public static final PlayerCapabilities genericPlayerCapabilities = new PlayerCapabilities();
	public static void HighJump (EntityPlayer player, ItemStack stack){
		double jumpbonus = 1 + (EnchantmentHelper.getEnchantmentLevel(Enchantments.highjump.effectId, stack) * Enchantments.highjumpbonus);
		player.motionY *= jumpbonus;
		 
	}

	public static void Cloud(EntityPlayer player, ItemStack stack)
	{
		//World world = player.worldObj;
		float featherLevel=Math.max(1+ Utils.getEnchHelp(player,2, player.getCurrentArmor(0)),1);
		double minFall=-0.25/featherLevel;
		double quickDrop=Math.max((featherLevel)*-0.3,-1.2);
		double fall = player.motionY;
		if(KeyBind.jumping && player.motionY<minFall) {
			player.motionY = minFall;
		}
		else if(player.isSneaking()	&& !player.capabilities.isCreativeMode)
			player.motionY=Math.min(quickDrop,player.motionY);

	}
	public static void Regen(EntityPlayer player, ItemStack stack)
	{
		if(Utils.getFood(player) >= 6)
		{
			player.heal(1);
			if(player.getHealth() < 20){
				Utils.setFood(player,-1,-2);
			}
		}
	}
	public static void NightVision(EntityPlayer player)
	{
		int duration = 320;
		player.addPotionEffect(new PotionEffect(16, duration, -337));
		if (duration != 317)
			duration = 317;
	}

	public static void RemoveNightVision(EntityPlayer player)
	{
		PotionEffect nightVision = null;

		if (player.isPotionActive(16))
			nightVision = player.getActivePotionEffect(Potion.nightVision);

		if (nightVision != null && nightVision.getAmplifier() == -337 &&player.worldObj.isRemote )
			player.removePotionEffectClient(16);
		else if (nightVision != null && nightVision.getAmplifier() == -337 &&!player.worldObj.isRemote )
			player.removePotionEffect(16);

	}


	public static void PhaseStep(EntityPlayer player)
	{

	}
	public static void Horticulture(EntityPlayer player, ItemStack stack)
	{
		HorticultureHelper.updateSurroundingPlantBlocks(player);
	}
	public static void Disarm(EntityLiving living)
	{
		if(living.getHeldItem() != null){
			living.entityDropItem(living.getHeldItem(), 0.0f);
			living.setCurrentItemOrArmor(0, null);

		}
	}

	public static void Arrow(EntityLiving living, ItemStack stack, int name, int duration, int level)
	{
		living.addPotionEffect(new PotionEffect(name, duration, level));
	}
	public static void ArrowAoe(Entity target, ItemStack stack, int name, int duration, int level)
	{
		((EntityLivingBase)target).addPotionEffect(new PotionEffect(name, duration, level));
	}
	public static void Aspect(EntityLiving living, ItemStack stack, int name, int duration, int level)
	{
		living.addPotionEffect(new PotionEffect(name, duration, level));
	}


	public static void Magnet(EntityPlayer player,World world, ItemStack stack)
	{
		int magnet = Utils.getEnchHelp(player,Enchantments.magnet.effectId, player.getCurrentArmor(2));
		double range = 2D * magnet;
		double speed = 1.5D;
		if(player.inventory.getFirstEmptyStack() != -1){
			if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) return;
			List<EntityItem> nearbyItems = world.getEntitiesWithinAABB(EntityItem.class, player.getBoundingBox().expand(range, range, range));
			for (EntityItem item : nearbyItems) {
				if (player.canEntityBeSeen(item) && item !=null) {
					if (item.cannotPickup()) {
						return;
					}
					double d0 = 8.0D;
					double d1 = (player.posX - item.posX) / d0;
					double d2 = (player.posY + player.getEyeHeight() - item.posY) / d0;
					double d3 = (player.posZ - item.posZ) / d0;
					double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
					double d5 = 1.0D - d4;

					if (d5 > 0.0D) {
						d5 *= d5;
						item.motionX += d1 / d4 * d5 * speed;
						item.motionY += d2 / d4 * d5 * speed;
						item.motionZ += d3 / d4 * d5 * speed;
					}
				}
			}
		}
	}
	public static void IceStep(EntityPlayer player) {
		ItemStack shoes = player.getCurrentArmor(0);
		World world = player.worldObj;
		int k = MathHelper.floor_double(player.posY - 1.0D);
		for (int i = 0; i < 3; i++) {
			for (int l = (int)player.posX -i; l <= player.posX + i; ++l){
				for (int i1 = (int)player.posZ - i; i1 <= player.posZ + i; ++i1){
					BlockPos pos = new BlockPos(l ,k,i1);
					Block block = world.getBlockState(pos).getBlock();
					IBlockState blockObsidian = Block.getStateById(49);
					IBlockState blockCobble = Block.getStateById(4);
					IBlockState blockIce= Block.getStateById(79);

					if ((block == Block.getBlockById(8) || (block == Block.getBlockById(9)))) {
						player.worldObj.setBlockState(pos,blockIce);
						if (player.worldObj.rand.nextInt(10) == 0)
							shoes.damageItem(1, player);
					}
					if ((block == Block.getBlockById(11))) {
						player.worldObj.setBlockState(pos, blockObsidian);

						if (player.worldObj.rand.nextInt(10) == 0)
							shoes.damageItem(1, player);
					}
					 if(block == Block.getBlockById(10)){
						player.worldObj.setBlockState(pos, blockCobble);
						if (player.worldObj.rand.nextInt(10) == 0)
							shoes.damageItem(1, player);}
				
				}
			}
		}
	}
	public static void GreenWood (EntityPlayer player, long time) {
		World world = player.worldObj;
		long timer = time;
		long timerDelay = Enchantments.greenwoodDelay;
		int items = 5;

		BlockPos pos = new BlockPos(player.posX ,player.posY-1,player.posZ);
		if (player.worldObj.isDaytime() && player.worldObj.isRemote) {
			Block block = player.worldObj.getBlockState(pos).getBlock();
			if (block == Block.getBlockById(8) || block == Block.getBlockById(9)) {
				if (time % (Enchantments.greenwoodDelay * 20) == 0L) {
					if (Utils.getEnchHelp(player, Enchantments.greenwood.effectId, player.getCurrentArmor(0)) != 0) {
						player.getCurrentArmor(0).damageItem(-1, player);
						player.addChatMessage(new ChatComponentText(player.getCurrentArmor(0) + " health = " + player.getCurrentArmor(0).getItemDamage()));

					}

					if (Utils.getEnchHelp(player, Enchantments.greenwood.effectId, player.getCurrentArmor(1)) != 0) {

						player.getCurrentArmor(1).damageItem(-1, player);
						player.addChatMessage(new ChatComponentText(player.getCurrentArmor(1) + " health = " + player.getCurrentArmor(1).getItemDamage()));


					}

					if (Utils.getEnchHelp(player, Enchantments.greenwood.effectId, player.getCurrentArmor(2)) != 0) {

						player.getCurrentArmor(2).damageItem(-1, player);
						player.addChatMessage(new ChatComponentText(player.getCurrentArmor(2) + " health = " + player.getCurrentArmor(2).getItemDamage()));

					}

					if (Utils.getEnchHelp(player, Enchantments.greenwood.effectId, player.getCurrentArmor(3)) != 0) {

						player.getCurrentArmor(3).damageItem(-1, player);
						player.addChatMessage(new ChatComponentText(player.getCurrentArmor(3) + " health = " + player.getCurrentArmor(3).getItemDamage()));


					}

					if (Utils.getEnchHelp(player, Enchantments.greenwood.effectId, player.getCurrentEquippedItem()) != 0) {
						for(ItemStack tools : Utils.getTools(player)) {
							tools.damageItem(-1, player);
							player.addChatMessage(new ChatComponentText(tools + " health = " + tools.getItemDamage()));
						}
					}
					time = timer;
				}

			}
		}
	}


	public static void Photosynthesis(EntityPlayer player, long time)
	{
		World world = player.worldObj;
		long timer = time;
		long timerDelay = Enchantments.photoDelay;
		int photo = Utils.getEnchHelp(player,Enchantments.photo.effectId, player.getCurrentArmor(3));
		if (player.worldObj.isDaytime() && !player.worldObj.isRemote)
			if (Utils.seeSky(player))
				if(photo != 0)
					if(player.getFoodStats().getFoodLevel() <= 20 && player.getFoodStats().getSaturationLevel() <= 20F)
					{
						if (time % (timerDelay * 20) == 0L) {
							//	System.out.println(time);
							player.getFoodStats().addStats(photo, photo / 2);
							time = timer;
						}
					}
	}
	public static void QuickDraw(EntityPlayer player, World world)
	{
		int updateStep = 0;
		updateStep += 1;
		final int drawStringLevel = Utils.getEnchHelp(player,Enchantments.quickdraw.effectId, player.getHeldItem());
		if(world.isRemote){
			if (updateStep % 2 == 1) {
				int itemUseCount = player.getItemInUseCount();
				if (player.getItemInUseCount() > 0 && drawStringLevel > 0) {
					player.setItemInUse(null, 0);
					player.setItemInUse(player.getHeldItem(), itemUseCount - drawStringLevel);
				}
			}
		}
	}

}

