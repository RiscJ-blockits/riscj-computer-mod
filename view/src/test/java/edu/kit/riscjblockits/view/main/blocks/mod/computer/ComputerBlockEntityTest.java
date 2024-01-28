package edu.kit.riscjblockits.view.main.blocks.mod.computer;

import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.clustering.ClusterHandler;
import edu.kit.riscjblockits.model.blocks.ControlUnitModel;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.model.instructionset.InstructionSetBuilder;
import edu.kit.riscjblockits.view.client.TestSetupClient;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.TestSetupMain;
import edu.kit.riscjblockits.view.main.blocks.mod.EntityType;
import edu.kit.riscjblockits.view.main.blocks.mod.ModBlock;
import edu.kit.riscjblockits.view.main.blocks.mod.ModBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.alu.AluBlock;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.bus.BusBlock;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.controlunit.ControlUnitBlock;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.controlunit.ControlUnitBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.memory.MemoryBlock;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.RegisterBlock;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.RegisterBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.systemclock.SystemClockBlock;
import edu.kit.riscjblockits.view.main.items.instructionset.InstructionSetItem;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.kit.riscjblockits.model.data.DataConstants.CONTROL_CLUSTERING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 */
@ExtendWith(TestSetupClient.class)
@ExtendWith(TestSetupMain.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ComputerBlockEntityTest {

    @Test
    void updateUIClientSide() {
        ComputerBlockEntity entity = new RegisterBlockEntity(new BlockPos(0,0, 0), RISCJ_blockits.REGISTER_BLOCK.getDefaultState());
        World world = mock(World.class);
        when(world.isClient()).thenReturn(true);
        ComputerBlockEntity.tick(world, new BlockPos(0,0, 0), RISCJ_blockits.REGISTER_BLOCK.getDefaultState(), entity);
        //should not crash on the client
        assertNotNull(entity.getController());
    }

    private static Map<BlockControllerType, ComputerBlockEntity> blocks;

    @BeforeAll
    public static void setup() {
        World world = mock(World.class);
        when(world.isClient()).thenReturn(false);
        BlockPos posAlu = new BlockPos(0,0, 0);
        BlockPos posControlUnit = new BlockPos(0,0, 1);
        BlockPos posMemory = new BlockPos(0,0, 2);
        BlockPos posRegister1 = new BlockPos(0,0, 3);
        BlockPos posClock = new BlockPos(0,0,4);
        BlockPos posBus0 = new BlockPos(1,0,0);
        BlockPos posBus1 = new BlockPos(1,0,1);
        BlockPos posBus2 = new BlockPos(1,0,2);
        BlockPos posBus3 = new BlockPos(1,0,3);
        BlockPos posBus4 = new BlockPos(1,0,4);
        //We place one register
        ComputerBlock registerBlock = (ComputerBlock) RISCJ_blockits.REGISTER_BLOCK;
        ComputerBlockEntity registerEntity1 = (ComputerBlockEntity) registerBlock.createBlockEntity(posRegister1, RISCJ_blockits.REGISTER_BLOCK.getDefaultState());
        when(world.getBlockEntity(posRegister1)).thenReturn(registerEntity1);
        registerBlock.onPlaced(world, posRegister1, RISCJ_blockits.REGISTER_BLOCK.getDefaultState(), null, null);
        //We place the first two bus blocks
        ComputerBlock busBlock = (ComputerBlock) RISCJ_blockits.BUS_BLOCK;
        ComputerBlockEntity busEntity3 = (ComputerBlockEntity) busBlock.createBlockEntity(posBus3, RISCJ_blockits.BUS_BLOCK.getDefaultState());
        when(world.getBlockEntity(posBus3)).thenReturn(busEntity3);
        busBlock.onPlaced(world, posBus3, RISCJ_blockits.BUS_BLOCK.getDefaultState(), null, null);
        ComputerBlockEntity busEntity4 = (ComputerBlockEntity) busBlock.createBlockEntity(posBus4, RISCJ_blockits.BUS_BLOCK.getDefaultState());
        when(world.getBlockEntity(posBus4)).thenReturn(busEntity4);
        busBlock.onPlaced(world, posBus4, RISCJ_blockits.BUS_BLOCK.getDefaultState(), null, null);
        //We place the clock
        ComputerBlock clockBlock =  (ComputerBlock) RISCJ_blockits.SYSTEM_CLOCK_BLOCK;
        ComputerBlockEntity clockEntity = (ComputerBlockEntity) clockBlock.createBlockEntity(posClock, RISCJ_blockits.SYSTEM_CLOCK_BLOCK.getDefaultState());
        when(world.getBlockEntity(posClock)).thenReturn(clockEntity);
        clockBlock.onPlaced(world, posClock, RISCJ_blockits.SYSTEM_CLOCK_BLOCK.getDefaultState(), null, null);
        //We place the control unit
        ComputerBlock controlUnitBlock =  (ComputerBlock) RISCJ_blockits.CONTROL_UNIT_BLOCK;
        ComputerBlockEntity controlUnitEntity = (ComputerBlockEntity) controlUnitBlock.createBlockEntity(posControlUnit, RISCJ_blockits.CONTROL_UNIT_BLOCK.getDefaultState());
        when(world.getBlockEntity(posControlUnit)).thenReturn(controlUnitEntity);
        controlUnitBlock.onPlaced(world, posControlUnit, RISCJ_blockits.CONTROL_UNIT_BLOCK.getDefaultState(), null, null);
        //We place the ALU
        ComputerBlock aluBlock =  (ComputerBlock) RISCJ_blockits.ALU_BLOCK;
        ComputerBlockEntity aluEntity = (ComputerBlockEntity) aluBlock.createBlockEntity(posAlu, RISCJ_blockits.ALU_BLOCK.getDefaultState());
        when(world.getBlockEntity(posAlu)).thenReturn(aluEntity);
        aluBlock.onPlaced(world, posAlu, RISCJ_blockits.ALU_BLOCK.getDefaultState(), null, null);
        //We place more bus blocks
        ComputerBlockEntity busEntity0 = (ComputerBlockEntity) busBlock.createBlockEntity(posBus0, RISCJ_blockits.BUS_BLOCK.getDefaultState());
        when(world.getBlockEntity(posBus0)).thenReturn(busEntity0);
        busBlock.onPlaced(world, posBus0, RISCJ_blockits.BUS_BLOCK.getDefaultState(), null, null);
        ComputerBlockEntity busEntity1 = (ComputerBlockEntity) busBlock.createBlockEntity(posBus1, RISCJ_blockits.BUS_BLOCK.getDefaultState());
        when(world.getBlockEntity(posBus1)).thenReturn(busEntity1);
        busBlock.onPlaced(world, posBus1, RISCJ_blockits.BUS_BLOCK.getDefaultState(), null, null);
        //We place the memory
        ComputerBlock memoryBlock =  (ComputerBlock) RISCJ_blockits.MEMORY_BLOCK;
        ComputerBlockEntity memoryEntity = (ComputerBlockEntity) memoryBlock.createBlockEntity(posMemory, RISCJ_blockits.MEMORY_BLOCK.getDefaultState());
        when(world.getBlockEntity(posMemory)).thenReturn(memoryEntity);
        memoryBlock.onPlaced(world, posMemory, RISCJ_blockits.MEMORY_BLOCK.getDefaultState(), null, null);
        //We place the last bus block
        ComputerBlockEntity busEntity2 = (ComputerBlockEntity) busBlock.createBlockEntity(posBus2, RISCJ_blockits.BUS_BLOCK.getDefaultState());
        when(world.getBlockEntity(posBus2)).thenReturn(busEntity2);
        busBlock.onPlaced(world, posBus2, RISCJ_blockits.BUS_BLOCK.getDefaultState(), null, null);
        //Computer finished
        blocks = new HashMap<>();
        blocks.put(BlockControllerType.REGISTER, registerEntity1);
        blocks.put(BlockControllerType.MEMORY, memoryEntity);
        blocks.put(BlockControllerType.ALU, aluEntity);
        blocks.put(BlockControllerType.CONTROL_UNIT, controlUnitEntity);
        blocks.put(BlockControllerType.CLOCK, clockEntity);
    }

    @Test
    @Order(1)
    void buildMimaComputer() {
        ControlUnitBlockEntity controlUnitEntity = (ControlUnitBlockEntity) blocks.get(BlockControllerType.CONTROL_UNIT);
        ClusterHandler clusterHandler = ((ComputerBlockController) controlUnitEntity.getController()).getClusterHandler();
        assertEquals(5, clusterHandler.getBlocks().size());
        assertEquals(5, clusterHandler.getBusBlocks().size());
        assertEquals(10, clusterHandler.getBusSystemModel().getBusGraph().size());
    }

    @Disabled   //ToDo does not work yet
    @Test
    @Order(2)
    void testArchitectureCheck() {
        InstructionSetItem istItem = (InstructionSetItem) RISCJ_blockits.INSTRUCTION_SET_ITEM_MIMA;
        ControlUnitBlockEntity cuEntity = (ControlUnitBlockEntity) blocks.get(BlockControllerType.CONTROL_UNIT);
        cuEntity.items.set(0, istItem.getDefaultStack());
        ClusterHandler clusterHandler = ((ComputerBlockController) (blocks.get(BlockControllerType.REGISTER).getController())).getClusterHandler();
        clusterHandler.setIstModel(InstructionSetBuilder.buildInstructionSetModelMima());           //cant really change the inventory
        ControlUnitModel cuModel = (ControlUnitModel) ((ComputerBlockController) cuEntity.getController()).getModel();
        Data cuData = (Data) cuModel.getData();
        assertNotNull(((IDataContainer) cuData.get(CONTROL_CLUSTERING)).get("foundAlu"));
        //assertEquals("1", ((IDataStringEntry) ((IDataContainer) cuData.get(CONTROL_CLUSTERING)).get("foundAlu")).getContent());     //ToDo
    }



}