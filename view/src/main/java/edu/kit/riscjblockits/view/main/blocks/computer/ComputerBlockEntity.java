package edu.kit.riscjblockits.view.main.blocks.computer;

import edu.kit.riscjblockits.controller.Controller;
import edu.kit.riscjblockits.controller.IQueriableBlockEntity;
import edu.kit.riscjblockits.model.Model;
import edu.kit.riscjblockits.view.main.blocks.mod.ModBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public abstract class ComputerBlockEntity extends ModBlockEntity implements IQueriableBlockEntity {

    public static void tick(World world, BlockPos pos, BlockState state, ComputerBlockEntity be) {
        if (be.model.hasUnqueriedChange())
            be.updateDataFromModel();
    }
    private Model model;
    public ComputerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        model = new Model();
    }

    public List<Controller> getComputerNeighbours() {
        return null;
    }

    protected void updateDataFromModel() {}


}
