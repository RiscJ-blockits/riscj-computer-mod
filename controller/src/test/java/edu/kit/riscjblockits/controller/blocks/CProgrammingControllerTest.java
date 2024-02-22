package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.assembler.Assembler;
import edu.kit.riscjblockits.controller.assembler.AssemblyException;
import edu.kit.riscjblockits.controller.simulation.SimulationSequenceHandler;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.blocks.ClockMode;
import edu.kit.riscjblockits.model.blocks.ControlUnitModel;
import edu.kit.riscjblockits.model.blocks.IViewQueryableBlockModel;
import edu.kit.riscjblockits.model.blocks.RegisterModel;
import edu.kit.riscjblockits.model.busgraph.IBusSystem;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.instructionset.IQueryableInstructionSetModel;
import edu.kit.riscjblockits.model.instructionset.InstructionBuildException;
import edu.kit.riscjblockits.model.instructionset.InstructionSetBuilder;
import edu.kit.riscjblockits.model.instructionset.InstructionSetModel;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static edu.kit.riscjblockits.model.data.DataConstants.MEMORY_MEMORY;
import static edu.kit.riscjblockits.model.data.DataConstants.MEMORY_PROGRAMM_ITEM;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CProgrammingControllerTest {

    private MemoryController memoryController;
    private SimulationSequenceHandler simulationSequenceHandler;
    private SystemClockController systemClockController;
    private IQueryableInstructionSetModel instructionSetModel;
    private List<IQueryableSimController> simControllers;

    private IConnectableComputerBlockEntity getBlockEntityMock() {
        IConnectableComputerBlockEntity blockEntity = new IConnectableComputerBlockEntity() {
            @Override
            public void setBlockModel(IViewQueryableBlockModel model) {
                //
            }

            @Override
            public List<ComputerBlockController> getComputerNeighbours() {
                return new LinkedList<>();
            }

            @Override
            public BlockPosition getBlockPosition() {
                return new BlockPosition(0, 0, 0);
            }

            @Override
            public void spawnEffect(ComputerEffect effect) {
                //
            }
        };
        return blockEntity;
    }

    private IBusSystem getBusSystemMock() {
        return new IBusSystem() {

            @Override
            public void setBusDataPath(BlockPosition startPos, BlockPosition endPos, Value presentData) {

            }

            @Override
            public void resetVisualisation() {

            }

            @Override
            public void activateVisualisation() {

            }
        };
    }

    private static InstructionSetModel buildInstructionSetModelRiscV() {
        InputStream is = InstructionSetBuilder.class.getClassLoader().getResourceAsStream("instructionSetRiscV.jsonc");
        try {
            return InstructionSetBuilder.buildInstructionSetModel(is);
        } catch (InstructionBuildException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCode(String code) throws AssemblyException {
       CProgrammingController cProgrammingController = new CProgrammingController();
        Assembler assembler = new Assembler(instructionSetModel);
        String assemblyCode = cProgrammingController.translateC(code);
        assembler.assemble(assemblyCode);
        IDataContainer container1 = new Data();
        IDataContainer container2 = new Data();
        container1.set(MEMORY_PROGRAMM_ITEM, assembler.getMemoryData());
        container2.set(MEMORY_MEMORY, container1);
        memoryController.setData(container2);
    }

    @BeforeEach
    public void init() {
        instructionSetModel = buildInstructionSetModelRiscV();
        simControllers = new ArrayList<>();
        memoryController = new MemoryController(getBlockEntityMock());
        simControllers.add(memoryController);
        ControlUnitController controlUnitController = new ControlUnitController(getBlockEntityMock());
        ((ControlUnitModel) controlUnitController.getModel()).setIstModel(instructionSetModel);
        simControllers.add(controlUnitController);
        simControllers.add(new AluController(getBlockEntityMock()));
        systemClockController = new SystemClockController(getBlockEntityMock());
        simControllers.add(systemClockController);
        for (String registerType : instructionSetModel.getRegisterNames()) {
            RegisterController registerController = new RegisterController(getBlockEntityMock());
            ((RegisterModel) registerController.getModel()).setRegisterType(registerType);
            simControllers.add(registerController);
        }
        simulationSequenceHandler = new SimulationSequenceHandler(simControllers, getBusSystemMock(), () -> {
        });
    }

    private void runSimulation() {
        systemClockController.setSimulationMode(ClockMode.REALTIME);
        while (systemClockController.getClockMode() == ClockMode.REALTIME) {
            simulationSequenceHandler.run();
        }
    }

    @Test
    void assemble() throws AssemblyException {
        setCode("""
        void main() {
            int a = 5;
            int b = a + 7;
        } 
        """);
        runSimulation();
        for (IQueryableSimController simController : simControllers) {
            if (simController instanceof RegisterController) {
                if (((RegisterModel) ((RegisterController) simController).getModel()).getRegisterType().equals("a5")) {
                    assertEquals("0000000C", ((RegisterModel) ((RegisterController) simController).getModel()).getValue().getHexadecimalValue());
                }
            }
        }
    }

}
