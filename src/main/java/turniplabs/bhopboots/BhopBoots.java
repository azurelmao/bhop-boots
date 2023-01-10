package turniplabs.bhopboots;

import net.fabricmc.api.ModInitializer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemArmor;
import net.minecraft.src.material.ArmorMaterial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.ArmorHelper;
import turniplabs.halplibe.helper.TextureHelper;


public class BhopBoots implements ModInitializer {
    public static final String MOD_ID = "bhopboots";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static String name(String name) {
        return BhopBoots.MOD_ID + "." + name;
    }

    public static final ArmorMaterial bhopMaterial = ArmorHelper.createArmorMaterial("bhop", 300, 0.0f, 0.0f, 0.0f,140.0f);

    public static final Item bhopBoots = new ItemArmor(2000, bhopMaterial, 3).setIconCoord(0, 23).setItemName(name("bhopboots"));

    @Override
    public void onInitialize() {
        LOGGER.info("BhopBoots initialized.");

        TextureHelper.addTextureToItems(MOD_ID, "bhop_boots.png", 0, 23);
    }
}
