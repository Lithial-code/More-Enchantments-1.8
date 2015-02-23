/*package com.lithial.me.gui;
import java.util.ArrayList;
import java.util.List;

import com.lithial.me.enchantments.Enchantments;
import com.lithial.me.utils.ModInfo;

 
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
 

public class MoreEnchantmentsConfigGui extends GuiConfig {

    static Configuration cfg = Enchantments.config;
    static Enchantments cfgh;

    public MoreEnchantmentsConfigGui(GuiScreen parent) {

        super(parent, generateConfigList(), ModInfo.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(Enchantments.config.toString()));
    }

    public static List<IConfigElement> generateConfigList() {

        ArrayList<IConfigElement> elements = new ArrayList<IConfigElement>();
        
        String[] categories = { cfgh.ENCHANT, cfgh.WEIGHT, cfgh.ALLOW, cfgh.MAX, cfgh.UPDATE, cfgh.POISON, cfgh.BONUS};

        for (int i = 0; i < categories.length; i++) {

            elements.add(new ConfigElement(cfg.getCategory(categories[i])));
            System.out.println(categories[i]);
            
        }
        return elements;
    }
}*/