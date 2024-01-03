package edu.kit.riscjblockits.view.main.items.instructionset;

import net.minecraft.item.Item;

/**
 * This class defines the instruction set item in the game.
 */
public class InstructionSetItem extends Item {
    /**
     * Creates a new instruction set item with the given settings.
     * @param settings The settings for the item as {@link net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings}.
     */
    public InstructionSetItem(Settings settings) {
        super(settings);
    }
}
