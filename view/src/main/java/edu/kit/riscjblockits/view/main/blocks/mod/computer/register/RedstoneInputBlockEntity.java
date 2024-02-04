package edu.kit.riscjblockits.view.main.blocks.mod.computer.register;

import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.blocks.IORegisterController;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.ComputerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import static edu.kit.riscjblockits.model.blocks.IORegisterModel.REDSTONE_INPUT;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_VALUE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WORD_LENGTH;

/**
 * This class defines the block entity for a register that stores the redstone power it receives.
 */
public class RedstoneInputBlockEntity extends ComputerBlockEntity {

    /**
     * Creates a new RedstoneInputBlockEntity.
     * @param pos The position of the block in the minecraft world for which the entity should be created.
     * @param state The state of the minecraft block for which the entity should be created.
     */
    public RedstoneInputBlockEntity(BlockPos pos, BlockState state) {
        super(RISCJ_blockits.REDSTONE_INPUT_BLOCK_ENTITY, pos, state);
    }

    /**
     * Creates a new IORegisterController.
     * @return A new IORegisterController bound to this block entity.
     */
    @Override
    protected ComputerBlockController createController() {
        return new IORegisterController(this, true, REDSTONE_INPUT);
    }

    /**
     * Is called by a redstone input block when the redstone power level changes.
     * Updates the value of the register.
     * @param power The new redstone power level. Between 0 and 15.
     */
    public void setRedstonePower(int power) {
        Value value = Value.fromDecimal(String.valueOf(power), 3);
        IDataContainer data = new Data();
        data.set(REGISTER_VALUE, new DataStringEntry(value.getHexadecimalValue()));
        data.set(REGISTER_WORD_LENGTH, new DataStringEntry("3"));       //ToDo hardcode?
        getController().setData(data);
    }

}
