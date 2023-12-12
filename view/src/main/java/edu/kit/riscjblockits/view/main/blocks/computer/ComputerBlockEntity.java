package edu.kit.riscjblockits.view.main.blocks.computer;

import edu.kit.riscjblockits.model.Model;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;


import net.minecraft.util.math.BlockPos;

public abstract class ComputerBlockEntity extends BlockEntity {

    private Model model;
    public ComputerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void setModel(Model model) {
        this.model = model;
    }
}
