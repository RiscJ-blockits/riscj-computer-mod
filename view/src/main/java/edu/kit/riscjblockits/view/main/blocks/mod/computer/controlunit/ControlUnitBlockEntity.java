package edu.kit.riscjblockits.view.main.blocks.mod.computer.controlunit;

import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.blocks.ControlUnitController;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.ComputerBlockEntityWithInventory;
import edu.kit.riscjblockits.view.main.data.NbtDataConverter;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static edu.kit.riscjblockits.model.data.DataConstants.CLUSTERING_FOUND_ALU;
import static edu.kit.riscjblockits.model.data.DataConstants.CLUSTERING_FOUND_CLOCK;
import static edu.kit.riscjblockits.model.data.DataConstants.CLUSTERING_FOUND_CONTROL_UNIT;
import static edu.kit.riscjblockits.model.data.DataConstants.CLUSTERING_FOUND_MEMORY;
import static edu.kit.riscjblockits.model.data.DataConstants.CLUSTERING_FOUND_REGISTERS;
import static edu.kit.riscjblockits.model.data.DataConstants.CLUSTERING_MISSING_REGISTERS;
import static edu.kit.riscjblockits.model.data.DataConstants.CONTROL_CLUSTERING;
import static edu.kit.riscjblockits.model.data.DataConstants.CONTROL_IST_MODEL;
import static edu.kit.riscjblockits.model.data.DataConstants.CONTROL_ITEM_PRESENT;
import static edu.kit.riscjblockits.model.data.DataConstants.MOD_DATA;

/**
 * This class represents a control unit entity from our mod in the game.
 * Every control unit has its own unique ControlUnitBlockEntity while it is loaded.
 */
public class ControlUnitBlockEntity extends ComputerBlockEntityWithInventory implements
    ExtendedScreenHandlerFactory {

    /**
     * The slot for the instruction set.
     */
    private static final int INSTRUCTION_SET_SLOT = 1;

    /**
     * Creates a new ControlUnitBlockEntity with the given settings.
     * @param pos The position of the block in the minecraft world.
     * @param state The state of the minecraft block.
     */
    public ControlUnitBlockEntity(BlockPos pos, BlockState state) {
        super(RISCJ_blockits.CONTROL_UNIT_BLOCK_ENTITY, pos, state, INSTRUCTION_SET_SLOT);
    }

    /**
     * Every entity needs its own controller.
     * @return An ControlUnitController bound to this entity.
     */
    @Override
    protected ComputerBlockController createController() {
        return new ControlUnitController(this);
    }

    /**
     * Called when the screen is opened.
     * We send the position to the screen.
     * @param player the player that is opening the screen
     * @param buf the packet buffer to write the data to
     */
    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    /**
     * Getter for the name of the ControlUnit Screen.
     * @return The name on the ControlUnit Screen.
     */
    @Override
    public Text getDisplayName() {
        return Text.translatable("block.riscj_blockits.control_unit_block");
    }

    /**
     * Creates a screen handler for the control unit.
     * @param syncId The id of the screen handler.
     * @param playerInventory The inventory of the player opening the screen.
     * @param player The player opening the screen.
     * @return A new ControlUnitScreenHandler.
     */
    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new ControlUnitScreenHandler(syncId, playerInventory, this);
    }

    /**
     * When the Instruction Set changes, the controller needs to be notified.
     */
    @Override
    public void inventoryChanged() {
        if (getController() != null) {             //only on the server
            if (getItems().get(0).getCount() == 0) {        //Item is removed when there are zero 'air' items
                Data cuData = new Data();
                cuData.set(CONTROL_IST_MODEL, null);
                getController().setData(cuData);
            } else {
                NbtCompound istNbt = getItems().get(0).getNbt();
                Data cuData = new Data();
                assert istNbt != null;
                NbtDataConverter converter = new NbtDataConverter(istNbt);
                cuData.set(CONTROL_IST_MODEL, converter.getData());
                getController().setData(cuData);
            }
        }
    }

    /**
     * Gets called every tick.
     * Used to update ui elements.
     */
    @Override
    public void updateUI() {
        if (getItems().get(0).getCount() == 0 && getModel() != null
                && ((IDataStringEntry) ((IDataContainer) getModel().getData()).get(CONTROL_ITEM_PRESENT)).getContent().equals("false")) {
            ItemScatterer.spawn(world, pos, (this));        //drop the IstItem if it is rejected
        }
        super.updateUI();
    }


    @Override
    public Text getGoggleText() {
        NbtCompound nbt = new NbtCompound();
        writeNbt(nbt);
        Text completion;

        ItemStack item = getItems().get(0);
        Text istName;

        if (!item.isEmpty()) { //if an instruction set is present, check completion
            istName = item.getName();

            List<String> missing = getStructure()[0];

            if(missing.isEmpty()){
                completion = Text.translatable("riscj_blockits.computer_complete");
            } else if (missing.size() == 1) {
                completion = Text.of("Missing: " + missing.get(0));
            } else {
                completion = Text.translatable("riscj_blockits.computer_incomplete");
            }
        } else { //otherwise set to Empty and incomplete
            istName = Text.translatable("riscj_blockits.ist_none");
            completion = Text.translatable("riscj_blockits.computer_incomplete");
        }

        return Text.translatable("block.riscj_blockits.control_unit_block")
                .append("\n")
                .append(Text.translatable("riscj_blockits.ist"))
                .append(": ")
                .append(istName)
                .append("\n")
                .append(Text.translatable("riscj_blockits.completion"))
                .append(": ")
                .append(completion);

    }

    /**
     * Getter for a List of missing Blocks and a List of found Blocks.
     * @return A List[] with the first entry being the missing Blocks and the second entry being the found Blocks.
     */
    public List[] getStructure(){
        List<String> listFound = new ArrayList<>();
        List<String> listMissing = new ArrayList<>();
        NbtCompound nbt = this.createNbt();
        if (!nbt.contains(MOD_DATA)) {
            return new List[]{listMissing, listFound};
        }
        IDataElement data = new NbtDataConverter(nbt.get(MOD_DATA)).getData();
        if (!data.isContainer()) {
            return new List[]{listMissing, listFound};
        }
        for (String s : ((IDataContainer) data).getKeys()) {
            if (s.equals(CONTROL_CLUSTERING)) {
                IDataContainer clusteringData = (IDataContainer) ((IDataContainer) data).get(CONTROL_CLUSTERING);
                for (String s2 : clusteringData.getKeys()) {
                    switch (s2) {
                        //ToDo reformat ugly code
                        case CLUSTERING_MISSING_REGISTERS:
                            listMissing.addAll(List.of(((IDataStringEntry) clusteringData.get(s2)).getContent().split(" ")));
                            listMissing.remove("");
                            break;
                        case CLUSTERING_FOUND_REGISTERS:
                            listFound.addAll(List.of(((IDataStringEntry) clusteringData.get(s2)).getContent().split(" ")));
                            listFound.remove("");
                            break;
                        case CLUSTERING_FOUND_CONTROL_UNIT:
                            String found = ((IDataStringEntry) clusteringData.get(s2)).getContent();
                            if (found.equals("1")) {
                                listFound.add("ControlUnit");
                            } else {
                                listMissing.add("ControlUnit");
                            }
                            break;
                        case CLUSTERING_FOUND_ALU:
                            String foundALU = ((IDataStringEntry) clusteringData.get(s2)).getContent();
                            if (foundALU.equals("1")) {
                                listFound.add("ALU");
                            } else {
                                listMissing.add("ALU");
                            }
                            break;
                        case CLUSTERING_FOUND_MEMORY:
                            String foundMemory = ((IDataStringEntry) clusteringData.get(s2)).getContent();
                            if (foundMemory.equals("1")) {
                                listFound.add("Memory");
                            } else {
                                listMissing.add("Memory");
                            }
                            break;
                        case CLUSTERING_FOUND_CLOCK:
                            String foundClock = ((IDataStringEntry) clusteringData.get(s2)).getContent();
                            if (foundClock.equals("1")) {
                                listFound.add("Clock");
                            } else {
                                listMissing.add("Clock");
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return new List[]{listMissing, listFound};
    }
}
