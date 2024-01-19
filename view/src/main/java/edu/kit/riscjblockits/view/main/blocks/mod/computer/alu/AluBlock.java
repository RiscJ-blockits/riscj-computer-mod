package edu.kit.riscjblockits.view.main.blocks.mod.computer.alu;

import edu.kit.riscjblockits.view.main.blocks.mod.computer.ComputerBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

/**
 * This class defines all computer ALUs in the game.
 * [JavaDoc in this class partially generated with GitHub Copilot]
 */
public class AluBlock extends ComputerBlock {

    /**
     * Creates a new AluBlock with the given settings.
     * @param settings The settings for the block as {@link net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings}.
     */
    public AluBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    /**
     * Creates a new AluBlock with default settings.
     */
    public AluBlock() {
        super();
    }

    /**
     * Creates a new entity for a ALU.
     * This method is invoked by minecraft when the block is loaded.
     * @nullable This is marked as nullable for one minecraft internal call but should never return null.
     * See {@link net.minecraft.block.BlockEntityProvider#createBlockEntity(BlockPos, BlockState)}.
     *
     * @param pos The position of the block in the minecraft world for which the entity should be created.
     * @param state The state of the minecraft block for which the entity should be created.
     * @return A new default instance of {@link AluBlockEntity}.
     */
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new AluBlockEntity(pos, state);
    }

}
