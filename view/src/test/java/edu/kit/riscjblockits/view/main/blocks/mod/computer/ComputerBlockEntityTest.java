package edu.kit.riscjblockits.view.main.blocks.mod.computer;

import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.clustering.ClusterHandler;
import edu.kit.riscjblockits.model.blocks.ControlUnitModel;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.model.instructionset.InstructionBuildException;
import edu.kit.riscjblockits.model.instructionset.InstructionSetBuilder;
import edu.kit.riscjblockits.model.instructionset.InstructionSetModel;
import edu.kit.riscjblockits.view.client.TestSetupClient;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.TestSetupMain;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.alu.AluBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.controlunit.ControlUnitBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.RegisterBlockEntity;
import edu.kit.riscjblockits.view.main.items.instructionset.InstructionSetItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static edu.kit.riscjblockits.model.data.DataConstants.CONTROL_CLUSTERING;
import static edu.kit.riscjblockits.model.data.DataConstants.MOD_DATA;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test MOSTLY GENERATED WITH GITHUB COPILOT.
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

    private static Map<String, ComputerBlockEntity> blocks;
    private static World world;

    @BeforeAll
    private static void setup() {
        world = mock(World.class);
        when(world.isClient()).thenReturn(false);
        BlockPos posAlu = new BlockPos(0,0, 4);
        BlockPos posControlUnit = new BlockPos(0,0, 1);
        BlockPos posMemory = new BlockPos(0,0, 2);
        BlockPos posRegister1 = new BlockPos(0,0, 3);
        BlockPos posClock = new BlockPos(0,0,0);
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
        blocks.put("REGISTER", registerEntity1);
        blocks.put("MEMORY", memoryEntity);
        blocks.put("ALU", aluEntity);
        blocks.put("CONTROL_UNIT", controlUnitEntity);
        blocks.put("CLOCK", clockEntity);
    }

    @Test
    @Order(1)
    void buildMimaComputer() {
        ControlUnitBlockEntity controlUnitEntity = (ControlUnitBlockEntity) blocks.get("CONTROL_UNIT");
        ClusterHandler clusterHandler = ((ComputerBlockController) controlUnitEntity.getController()).getClusterHandler();
        assertEquals(5, clusterHandler.getBlocks().size());
        assertEquals(5, clusterHandler.getBusBlocks().size());
        assertEquals(10, clusterHandler.getBusSystemModel().getBusGraph().size());
    }

    @Test
    @Order(2)
    void testArchitectureCheck() {
        InstructionSetItem istItem = (InstructionSetItem) RISCJ_blockits.INSTRUCTION_SET_ITEM_MIMA;
        ControlUnitBlockEntity cuEntity = (ControlUnitBlockEntity) blocks.get("CONTROL_UNIT");
        cuEntity.items.set(0, istItem.getDefaultStack());
        ClusterHandler clusterHandler = ((ComputerBlockController) (blocks.get("REGISTER").getController())).getClusterHandler();
        clusterHandler.setIstModel(buildInstructionSetModelMima());           //cant really change the inventory
        ControlUnitModel cuModel = (ControlUnitModel) ((ComputerBlockController) cuEntity.getController()).getModel();
        Data cuData = (Data) cuModel.getData();
        assertEquals("1", ((IDataStringEntry) ((IDataContainer) cuData.get(CONTROL_CLUSTERING)).get("foundALU")).getContent());
        assertEquals("AKKU EINS IAR IR SAR SDR X Y Z", ((IDataStringEntry) ((IDataContainer) cuData.get(CONTROL_CLUSTERING)).get("missingRegisters")).getContent());
    }

    @Test
    @Order(3)
    void testOneRegisterSet() {
        RegisterBlockEntity registerEntity1 = (RegisterBlockEntity) blocks.get("REGISTER");
        NbtCompound nbt = new NbtCompound();
        ((RegisterBlockEntity) registerEntity1).writeNbt(nbt);
        NbtCompound subNbt = (NbtCompound) nbt.get(MOD_DATA);
        assert subNbt != null;
        subNbt.putString(REGISTER_TYPE, "AKKU");
        registerEntity1.readNbt(nbt);
        ControlUnitBlockEntity cuEntity = (ControlUnitBlockEntity) blocks.get("CONTROL_UNIT");
        ControlUnitModel cuModel = (ControlUnitModel) ((ComputerBlockController) cuEntity.getController()).getModel();
        Data cuData = (Data) cuModel.getData();
        assertEquals("EINS IAR IR SAR SDR X Y Z", ((IDataStringEntry) ((IDataContainer) cuData.get(CONTROL_CLUSTERING)).get("missingRegisters")).getContent());
    }

    @Test
    @Order(4)
    void testALLRegisterSet() {
        setupRegisters();
        AluBlockEntity aluUnitEntity = (AluBlockEntity) blocks.get("ALU");
        ClusterHandler clusterHandler = ((ComputerBlockController) aluUnitEntity.getController()).getClusterHandler();
        assertEquals(13, clusterHandler.getBlocks().size());
        assertEquals(26, clusterHandler.getBusSystemModel().getBusGraph().size());
        ControlUnitBlockEntity cuEntity = (ControlUnitBlockEntity) blocks.get("CONTROL_UNIT");
        ControlUnitModel cuModel = (ControlUnitModel) ((ComputerBlockController) cuEntity.getController()).getModel();
        Data cuData = (Data) cuModel.getData();
        assertEquals("Z", ((IDataStringEntry) ((IDataContainer) cuData.get(CONTROL_CLUSTERING)).get("missingRegisters")).getContent());
    }

    private void setupRegisters() {
        BlockPos posRegister2 = new BlockPos(0,0, 10);
        BlockPos posRegister3 = new BlockPos(0,0, 6);
        BlockPos posRegister4 = new BlockPos(0,0, 7);
        BlockPos posRegister5 = new BlockPos(0,0, 8);
        BlockPos posRegister6 = new BlockPos(0,0, 9);
        BlockPos posRegister7 = new BlockPos(0,0, 5);
        BlockPos posRegister8 = new BlockPos(0,1, 4);
        BlockPos posRegister9 = new BlockPos(0,0, 11);
        BlockPos posBus5 = new BlockPos(1,0, 5);
        BlockPos posBus6 = new BlockPos(1,0, 6);
        BlockPos posBus7 = new BlockPos(1,0, 7);
        BlockPos posBus8 = new BlockPos(1,0, 8);
        BlockPos posBus9 = new BlockPos(1,0, 9);
        BlockPos posBus10 = new BlockPos(1,0, 10);
        BlockPos posBus11 = new BlockPos(1,0, 11);
        BlockPos posBus12 = new BlockPos(1,1, 4);
        //we place the bus blocks
        ComputerBlock busBlock = (ComputerBlock) RISCJ_blockits.BUS_BLOCK;
        ComputerBlockEntity busEntity5 = (ComputerBlockEntity) busBlock.createBlockEntity(posBus5, RISCJ_blockits.BUS_BLOCK.getDefaultState());
        when(world.getBlockEntity(posBus5)).thenReturn(busEntity5);
        busBlock.onPlaced(world, posBus5, RISCJ_blockits.BUS_BLOCK.getDefaultState(), null, null);
        ComputerBlockEntity busEntity6 = (ComputerBlockEntity) busBlock.createBlockEntity(posBus6, RISCJ_blockits.BUS_BLOCK.getDefaultState());
        when(world.getBlockEntity(posBus6)).thenReturn(busEntity6);
        busBlock.onPlaced(world, posBus6, RISCJ_blockits.BUS_BLOCK.getDefaultState(), null, null);
        ComputerBlockEntity busEntity7 = (ComputerBlockEntity) busBlock.createBlockEntity(posBus7, RISCJ_blockits.BUS_BLOCK.getDefaultState());
        when(world.getBlockEntity(posBus7)).thenReturn(busEntity7);
        busBlock.onPlaced(world, posBus7, RISCJ_blockits.BUS_BLOCK.getDefaultState(), null, null);
        ComputerBlockEntity busEntity8 = (ComputerBlockEntity) busBlock.createBlockEntity(posBus8, RISCJ_blockits.BUS_BLOCK.getDefaultState());
        when(world.getBlockEntity(posBus8)).thenReturn(busEntity8);
        busBlock.onPlaced(world, posBus8, RISCJ_blockits.BUS_BLOCK.getDefaultState(), null, null);
        ComputerBlockEntity busEntity9 = (ComputerBlockEntity) busBlock.createBlockEntity(posBus9, RISCJ_blockits.BUS_BLOCK.getDefaultState());
        when(world.getBlockEntity(posBus9)).thenReturn(busEntity9);
        busBlock.onPlaced(world, posBus9, RISCJ_blockits.BUS_BLOCK.getDefaultState(), null, null);
        ComputerBlockEntity busEntity10 = (ComputerBlockEntity) busBlock.createBlockEntity(posBus10, RISCJ_blockits.BUS_BLOCK.getDefaultState());
        when(world.getBlockEntity(posBus10)).thenReturn(busEntity10);
        busBlock.onPlaced(world, posBus10, RISCJ_blockits.BUS_BLOCK.getDefaultState(), null, null);
        ComputerBlockEntity busEntity11 = (ComputerBlockEntity) busBlock.createBlockEntity(posBus11, RISCJ_blockits.BUS_BLOCK.getDefaultState());
        when(world.getBlockEntity(posBus11)).thenReturn(busEntity11);
        busBlock.onPlaced(world, posBus11, RISCJ_blockits.BUS_BLOCK.getDefaultState(), null, null);
        ComputerBlockEntity busEntity12 = (ComputerBlockEntity) busBlock.createBlockEntity(posBus12, RISCJ_blockits.BUS_BLOCK.getDefaultState());
        when(world.getBlockEntity(posBus12)).thenReturn(busEntity12);
        busBlock.onPlaced(world, posBus12, RISCJ_blockits.BUS_BLOCK.getDefaultState(), null, null);
        //place registers
        ComputerBlock registerBlock = (ComputerBlock) RISCJ_blockits.REGISTER_BLOCK;
        ComputerBlockEntity registerEntity2 = (ComputerBlockEntity) registerBlock.createBlockEntity(posRegister2, RISCJ_blockits.REGISTER_BLOCK.getDefaultState());
        when(world.getBlockEntity(posRegister2)).thenReturn(registerEntity2);
        registerBlock.onPlaced(world, posRegister2, RISCJ_blockits.REGISTER_BLOCK.getDefaultState(), null, null);
        ComputerBlockEntity registerEntity3 = (ComputerBlockEntity) registerBlock.createBlockEntity(posRegister3, RISCJ_blockits.REGISTER_BLOCK.getDefaultState());
        when(world.getBlockEntity(posRegister3)).thenReturn(registerEntity3);
        registerBlock.onPlaced(world, posRegister3, RISCJ_blockits.REGISTER_BLOCK.getDefaultState(), null, null);
        ComputerBlockEntity registerEntity4 = (ComputerBlockEntity) registerBlock.createBlockEntity(posRegister4, RISCJ_blockits.REGISTER_BLOCK.getDefaultState());
        when(world.getBlockEntity(posRegister4)).thenReturn(registerEntity4);
        registerBlock.onPlaced(world, posRegister4, RISCJ_blockits.REGISTER_BLOCK.getDefaultState(), null, null);
        ComputerBlockEntity registerEntity5 = (ComputerBlockEntity) registerBlock.createBlockEntity(posRegister5, RISCJ_blockits.REGISTER_BLOCK.getDefaultState());
        when(world.getBlockEntity(posRegister5)).thenReturn(registerEntity5);
        registerBlock.onPlaced(world, posRegister5, RISCJ_blockits.REGISTER_BLOCK.getDefaultState(), null, null);
        ComputerBlockEntity registerEntity6 = (ComputerBlockEntity) registerBlock.createBlockEntity(posRegister6, RISCJ_blockits.REGISTER_BLOCK.getDefaultState());
        when(world.getBlockEntity(posRegister6)).thenReturn(registerEntity6);
        registerBlock.onPlaced(world, posRegister6, RISCJ_blockits.REGISTER_BLOCK.getDefaultState(), null, null);
        ComputerBlockEntity registerEntity7 = (ComputerBlockEntity) registerBlock.createBlockEntity(posRegister7, RISCJ_blockits.REGISTER_BLOCK.getDefaultState());
        when(world.getBlockEntity(posRegister7)).thenReturn(registerEntity7);
        registerBlock.onPlaced(world, posRegister7, RISCJ_blockits.REGISTER_BLOCK.getDefaultState(), null, null);
        ComputerBlockEntity registerEntity8 = (ComputerBlockEntity) registerBlock.createBlockEntity(posRegister8, RISCJ_blockits.REGISTER_BLOCK.getDefaultState());
        when(world.getBlockEntity(posRegister8)).thenReturn(registerEntity8);
        registerBlock.onPlaced(world, posRegister8, RISCJ_blockits.REGISTER_BLOCK.getDefaultState(), null, null);
        ComputerBlockEntity registerEntity9 = (ComputerBlockEntity) registerBlock.createBlockEntity(posRegister9, RISCJ_blockits.REGISTER_BLOCK.getDefaultState());
        when(world.getBlockEntity(posRegister9)).thenReturn(registerEntity9);
        registerBlock.onPlaced(world, posRegister9, RISCJ_blockits.REGISTER_BLOCK.getDefaultState(), null, null);
        //configure registers
        NbtCompound nbt = new NbtCompound();
        ((RegisterBlockEntity) registerEntity2).writeNbt(nbt);
        NbtCompound subNbt = (NbtCompound) nbt.get(MOD_DATA);
        subNbt.putString(REGISTER_TYPE, "EINS");
        registerEntity2.readNbt(nbt);
        ((RegisterBlockEntity) registerEntity3).writeNbt(nbt);
        subNbt = (NbtCompound) nbt.get(MOD_DATA);
        subNbt.putString(REGISTER_TYPE, "IAR");
        registerEntity3.readNbt(nbt);
        ((RegisterBlockEntity) registerEntity4).writeNbt(nbt);
        subNbt = (NbtCompound) nbt.get(MOD_DATA);
        subNbt.putString(REGISTER_TYPE, "IR");
        registerEntity4.readNbt(nbt);
        ((RegisterBlockEntity) registerEntity5).writeNbt(nbt);
        subNbt = (NbtCompound) nbt.get(MOD_DATA);
        subNbt.putString(REGISTER_TYPE, "SAR");
        registerEntity5.readNbt(nbt);
        ((RegisterBlockEntity) registerEntity6).writeNbt(nbt);
        subNbt = (NbtCompound) nbt.get(MOD_DATA);
        subNbt.putString(REGISTER_TYPE, "SDR");
        registerEntity6.readNbt(nbt);
        ((RegisterBlockEntity) registerEntity7).writeNbt(nbt);
        subNbt = (NbtCompound) nbt.get(MOD_DATA);
        subNbt.putString(REGISTER_TYPE, "X");
        registerEntity7.readNbt(nbt);
        ((RegisterBlockEntity) registerEntity8).writeNbt(nbt);
        subNbt = (NbtCompound) nbt.get(MOD_DATA);
        subNbt.putString(REGISTER_TYPE, "Y");
        registerEntity8.readNbt(nbt);
        //we don't set the last one to not start the simulation
        blocks.put("REGISTER9", registerEntity9);
    }

    @Disabled("Because of refactored funktionality")
    @Test
    @Order(5)
    void startSimualtion() {
        NbtCompound nbt = new NbtCompound();
        RegisterBlockEntity registerEntity9 = (RegisterBlockEntity) blocks.get("REGISTER9");
        ((RegisterBlockEntity) registerEntity9).writeNbt(nbt);
        NbtCompound subNbt = (NbtCompound) nbt.get(MOD_DATA);
        subNbt.putString(REGISTER_TYPE, "Z");
        registerEntity9.readNbt(nbt);
        //only when memory is set
    }

    private static InstructionSetModel buildInstructionSetModelMima() {
        InputStream is = InstructionSetBuilder.class.getClassLoader().getResourceAsStream("instructionSetMIMA.jsonc");
        try {
            return InstructionSetBuilder.buildInstructionSetModel(is);
        }  catch (InstructionBuildException e) {
            throw new RuntimeException(e);
        }
    }

    private static InstructionSetModel buildInstructionSetModelRiscV() {
        InputStream is = InstructionSetBuilder.class.getClassLoader().getResourceAsStream("instructionSetRiscV.jsonc");
        try {
            return InstructionSetBuilder.buildInstructionSetModel(is);
        }  catch (InstructionBuildException e) {
            throw new RuntimeException(e);
        }
    }

}
