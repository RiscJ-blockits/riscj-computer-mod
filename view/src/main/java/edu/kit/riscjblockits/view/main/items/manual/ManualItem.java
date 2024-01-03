package edu.kit.riscjblockits.view.main.items.manual;

import net.minecraft.item.Item;

/**
 * This class defines the manual item in the game.
 */
public class ManualItem extends Item {
    /**
     * Creates a new manual item with the given settings.
     * @param settings The settings for the item as {@link net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings}.
     */
    public ManualItem(Settings settings) {
        super(settings);
    }
}
