package edu.kit.riscjblockits.view.main.blocks.alu;

import edu.kit.riscjblockits.controller.blocks.*;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.computer.ComputerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

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

}
