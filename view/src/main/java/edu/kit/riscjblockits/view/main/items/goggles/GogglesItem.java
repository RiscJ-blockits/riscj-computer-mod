package edu.kit.riscjblockits.view.main.items.goggles;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;

/**
 * This class defines the goggles item in the game.
 */
public class GogglesItem extends Item implements Equipment {
    /**
     * Creates a new goggles item with the given settings.
     * @param settings The settings for the item as {@link net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings}.
     */
    public GogglesItem(Settings settings) {
        super(settings);
    }

    /**
     * Returns the slot type of the goggles item.
     * This will tell Minecraft where the Item is equip-able
     * @return The slot type of the goggles item.
     */
    @Override
    public EquipmentSlot getSlotType() {
        return EquipmentSlot.HEAD;
    }
}
