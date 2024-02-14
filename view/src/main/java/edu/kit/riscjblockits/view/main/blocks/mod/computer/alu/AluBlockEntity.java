package edu.kit.riscjblockits.view.main.blocks.mod.computer.alu;

import edu.kit.riscjblockits.controller.blocks.*;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.ComputerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import static edu.kit.riscjblockits.model.data.DataConstants.ALU_OPERATION;
import static edu.kit.riscjblockits.model.data.DataConstants.MOD_DATA;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TYPE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_VALUE;

/**
 * This class represents an alu from our mod in the game.
 * Every alu has its own unique AluBlockEntity while it is loaded.
 */
public class AluBlockEntity extends ComputerBlockEntity {

    /**
     * Creates a new AluBlockEntity with the given settings.
     * @param pos The position of the block in the minecraft world.
     * @param state The state of the minecraft block.
     */
    public AluBlockEntity(BlockPos pos, BlockState state) {
        super(RISCJ_blockits.ALU_BLOCK_ENTITY, pos, state);
    }

    /**
     * Every entity needs its own controller.
     * @return An {@link AluController} bound to this entity.
     */
    @Override
    protected IUserInputReceivableComputerController createController() {
        return new AluController(this);
    }

    @Override
    public Text getGoggleText() {
        NbtCompound nbt = new NbtCompound();
        writeNbt(nbt);
        String operation = "";
        if (nbt.contains(MOD_DATA)) {
            nbt = nbt.getCompound(MOD_DATA);
        }
        if (nbt.contains(ALU_OPERATION)) {
            operation = nbt.getString(ALU_OPERATION);
        }

        return Text.translatable("block.riscj_blockits.alu_block")
                .append("\n")
                .append(Text.translatable("riscj_blockits.alu_operation"))
                .append(": " + operation);

    }
}
