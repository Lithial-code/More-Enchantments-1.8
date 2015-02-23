package com.lithial.me;


import com.lithial.me.aencEx.api.IdExtension;
import com.lithial.me.enchantments.Compat;
import com.lithial.me.enchantments.Enchantments;
import com.lithial.me.handlers.*;
import com.lithial.me.handlers.controls.KeyBind;
import com.lithial.me.handlers.utils.AntiVenomHandler;
import com.lithial.me.network.ClientProxy;
import com.lithial.me.network.CommonProxy;
import com.lithial.me.utils.ModInfo;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

import javax.security.auth.login.Configuration;
import java.io.File;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, version = ModInfo.VERSION)

public class MoreEnchantments {
	@Mod.Instance(ModInfo.MOD_ID)
	static MoreEnchantments instance;

	@SidedProxy(clientSide = "com.lithial.me.network.ClientProxy", serverSide = "com.lithial.me.network.CommonProxy")
	public static CommonProxy proxy;
	static ClientProxy cproxy;
	static Logger log;
    public static Configuration config;
    public static Configuration config2;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		IdExtension.expand();
		File config = new File(event.getModConfigurationDirectory(), "MoreEnchantments/Core.cfg");
        File config2 = new File(event.getModConfigurationDirectory(), "MoreEnchantments/Compat.cfg");

        Enchantments.initialize(config);
        Compat.initialize(config2);
		Enchantments.save();
        Compat.save();

        if (event.getSide() == Side.CLIENT)
        {
            FMLCommonHandler.instance().bus().register(new KeyBind());
        }
 	}

	@Mod.EventHandler
	public void Initiate(FMLInitializationEvent Event)
	{
		new Enchantments();
		proxy.registerEvents();

	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new LivingHandler());
		MinecraftForge.EVENT_BUS.register(new DamageHandler());
	//	MinecraftForge.EVENT_BUS.register(new ArrowHandler());
		MinecraftForge.EVENT_BUS.register(new HarvestHandler());
		MinecraftForge.EVENT_BUS.register(new ConstructionHandler());
        MinecraftForge.EVENT_BUS.register(new AntiVenomHandler());
	}


}
