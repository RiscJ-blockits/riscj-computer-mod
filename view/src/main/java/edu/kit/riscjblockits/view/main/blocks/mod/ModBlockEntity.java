package edu.kit.riscjblockits.view.main.blocks.mod;

import edu.kit.riscjblockits.controller.Controller;
import edu.kit.riscjblockits.view.main.Data;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public abstract class ModBlockEntity extends BlockEntity {
    private static final String CONTROLLER_NBT_TAG = "riskjblockits.controller";
    private final Controller controller;

    public ModBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        // create Controller
        controller = new Controller();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        controller.setData(new Data(nbt));
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putString(CONTROLLER_NBT_TAG, controller.saveAsString());
    }
}
