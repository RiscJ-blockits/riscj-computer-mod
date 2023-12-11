package edu.kit.riscjblockits.view.main.blocks.modblock;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

public abstract class ModblockItem extends BlockItem {
    public ModblockItem(Block block, Settings settings) {
        super(block, settings);
    }

    public ModblockItem(Block block) {
        super(block, new FabricItemSettings());
    }
}
