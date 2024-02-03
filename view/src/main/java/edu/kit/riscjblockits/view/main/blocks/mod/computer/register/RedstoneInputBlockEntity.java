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

public class RedstoneInputBlockEntity extends ComputerBlockEntity {
    public RedstoneInputBlockEntity(BlockPos pos, BlockState state) {
        super(RISCJ_blockits.REDSTONE_INPUT_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected ComputerBlockController createController() {
        return new IORegisterController(this, true, REDSTONE_INPUT);
    }

    public void setRedstonePower(int power) {
        Value value = Value.fromDecimal(String.valueOf(power), 3);
        IDataContainer data = new Data();
        data.set(REGISTER_VALUE, new DataStringEntry(value.getHexadecimalValue()));
        data.set(REGISTER_WORD_LENGTH, new DataStringEntry("3"));       //ToDo hardcode?
        getController().setData(data);
    }

}
