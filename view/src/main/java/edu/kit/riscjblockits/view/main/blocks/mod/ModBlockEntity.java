package edu.kit.riscjblockits.view.main.blocks.mod;

import edu.kit.riscjblockits.controller.Controller;
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
        controller.loadFrom(nbt.getString(CONTROLLER_NBT_TAG));
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putString(CONTROLLER_NBT_TAG, controller.saveAsString());
    }
}
