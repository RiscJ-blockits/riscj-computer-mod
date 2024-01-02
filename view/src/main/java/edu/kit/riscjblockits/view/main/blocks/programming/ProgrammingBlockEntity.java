package edu.kit.riscjblockits.view.main.blocks.programming;

import edu.kit.riscjblockits.controller.assembler.AssemblyException;
import edu.kit.riscjblockits.controller.blocks.BlockController;
import edu.kit.riscjblockits.controller.blocks.IUserInputReceivableComputerController;
import edu.kit.riscjblockits.controller.blocks.IUserInputReceivableController;
import edu.kit.riscjblockits.controller.blocks.ProgrammingController;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.ImplementedInventory;
import edu.kit.riscjblockits.view.main.blocks.mod.ModBlockEntityWithInventory;
import edu.kit.riscjblockits.view.main.data.Data;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class ProgrammingBlockEntity extends ModBlockEntityWithInventory implements ExtendedScreenHandlerFactory,
        ImplementedInventory {

    public TextContent textContent;

    public ProgrammingBlockEntity(BlockPos pos, BlockState state) {
        super(RISCJ_blockits.PROGRAMMING_BLOCK_ENTITY, pos, state, 2);
        textContent = new TextContent();
    }

    @Override
    protected IUserInputReceivableController createController() {
        return new ProgrammingController();
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return null;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {

    }

    @Override
    public Text getDisplayName() {
        return null;
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return null;
    }

    public void assemble() throws AssemblyException {

        ItemStack instructionSetStack = getStack(0);
        ItemStack memoryStack = getStack(1);

        String code = textContent.toString();
        Data instructionSetData = new Data(instructionSetStack.getOrCreateSubNbt("riskjblockits.instruction_set"));
        Data memoryData = new Data(memoryStack.getOrCreateSubNbt("riskjblockits.memory"));
        ((ProgrammingController) getController()).assemble(code, instructionSetData, memoryData);
        memoryStack.setSubNbt("riskjblockits.memory", memoryData.toNbt());
    }
}
