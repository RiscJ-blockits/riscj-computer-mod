package edu.kit.riscjblockits.view.main.blocks.register;

import edu.kit.riscjblockits.controller.blocks.BlockController;
import edu.kit.riscjblockits.controller.blocks.RegisterController;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.computer.ComputerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents a system clock entity from our mod in the game.
 * Every system clock has its own unique SystemClockBlockEntity while it is loaded.
 */
public class RegisterBlockEntity extends ComputerBlockEntity {

    /**
     * Creates a new RegisterBlockEntity with the given settings.
     * @param pos The position of the block in the minecraft world.
     * @param state The state of the minecraft block.
     */
    public RegisterBlockEntity(BlockPos pos, BlockState state) {
        super(RISCJ_blockits.REGISTER_BLOCK_ENTITY, pos, state);
    }

    /**
     * Every entity needs its own controller.
     * @return An RegisterController bound to this entity.
     */
    @Override
    protected BlockController createController() {
        return new RegisterController(this);
    }

    /**
     * ToDo
     * @return
     */
    @Override
    public @Nullable Object getRenderData() {
        return super.getRenderData();
    }

    /**
     * ToDo
     * @return
     */
    @Override
    public String getInfo() {
        return null;
    }

}
