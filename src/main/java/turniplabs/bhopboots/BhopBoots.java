package turniplabs.bhopboots;

import net.fabricmc.api.ModInitializer;

import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemArmor;
import net.minecraft.core.item.material.ArmorMaterial;
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

    static {
        try {
            Class.forName("net.minecraft.core.block.Block");
            Class.forName("net.minecraft.core.item.Item");
        } catch (ClassNotFoundException ignored) {
        }
    }

    public static final ArmorMaterial bhopMaterial = ArmorHelper.createArmorMaterial("bhop", 300, 0.0f, 0.0f, 0.0f,140.0f);
    public static final Item bhopBoots = new ItemArmor("bhopboots", 17001, bhopMaterial, 3).setIconCoord(0, 23);
    public static final Item speedometer = new Item("speedometer", 17002).setIconCoord(1, 23);

    @Override
    public void onInitialize() {
        LOGGER.info("BhopBoots initialized.");

        TextureHelper.addTextureToItems(MOD_ID, "bhop_boots.png", 0, 23);
        TextureHelper.addTextureToItems(MOD_ID, "speedometer.png", 1, 23);
    }
}
