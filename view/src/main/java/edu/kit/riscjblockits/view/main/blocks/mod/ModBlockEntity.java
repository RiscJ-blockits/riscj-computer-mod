package edu.kit.riscjblockits.view.main.blocks.mod;

import edu.kit.riscjblockits.controller.blocks.BlockController;
import edu.kit.riscjblockits.controller.blocks.IUserInputReceivableController;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.view.main.data.Data;
import edu.kit.riscjblockits.view.main.blocks.EntityType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

public abstract class ModBlockEntity extends BlockEntity {
    private static final String CONTROLLER_NBT_TAG = "riskjblockits.controller";
    private BlockController controller;
    private EntityType etype;

    protected ModBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        etype = EntityType.UNCONNECTABLE;

        // create Controller, FixMe Controller needs to get created when reload
        //if (!world.isClient) {
        //    controller = createController();
        //}
    }

    protected abstract IUserInputReceivableController createController();

    public void setController() {
        if (!world.isClient) {
            controller = createController();
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        controller.setData(new Data(nbt));
    }

    public BlockPosition getBlockPosition() {
        BlockPos pos = getPos();
        return new BlockPosition(pos.getX(), pos.getY(), pos.getZ());
    }

    public EntityType getModblockType() {
        return etype;
    }

    public IUserInputReceivableController getController() {
        if (world.isClient) {
            return null;
        }
        return controller;
    }

    public void setType(EntityType type) {
        this.etype = type;
    }

}
