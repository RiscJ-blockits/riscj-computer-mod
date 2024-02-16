package edu.kit.riscjblockits.controller.clustering;

import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.blocks.ControlUnitController;
import edu.kit.riscjblockits.controller.blocks.IQueryableClusterController;
import edu.kit.riscjblockits.controller.blocks.MemoryController;
import edu.kit.riscjblockits.controller.blocks.RegisterController;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.instructionset.IQueryableInstructionSetModel;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static edu.kit.riscjblockits.model.data.DataConstants.CLUSTERING_FOUND_ALU;
import static edu.kit.riscjblockits.model.data.DataConstants.CLUSTERING_FOUND_CLOCK;
import static edu.kit.riscjblockits.model.data.DataConstants.CLUSTERING_FOUND_CONTROL_UNIT;
import static edu.kit.riscjblockits.model.data.DataConstants.CLUSTERING_FOUND_MEMORY;
import static edu.kit.riscjblockits.model.data.DataConstants.CLUSTERING_FOUND_REGISTERS;
import static edu.kit.riscjblockits.model.data.DataConstants.CLUSTERING_MISSING_REGISTERS;
import static edu.kit.riscjblockits.model.data.DataConstants.CONTROL_CLUSTERING;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_FOUND;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_MISSING;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_REGISTERS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WORD_LENGTH;

/**
 * Checks clusters for valid architectures.
 */
public final class ClusterArchitectureHandler {

    /**
     * Private constructor to prevent instantiation.
     * @throws IllegalStateException thrown if a constructor is called.
     */
    private ClusterArchitectureHandler() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Checks a given cluster if it is a specific valid architecture.
     * @param istModel The instruction set model to check the cluster against.
     * @param clusterHandler The cluster to check.
     * @return true if the cluster is a valid architecture, false otherwise
     * @throws IllegalStateException thrown if an unknown Controller is found in the cluster.
     */
    public static boolean checkArchitecture(IQueryableInstructionSetModel istModel, ClusterHandler clusterHandler) { //ToDo im ugly code please reformat me
        //is called after every block modification
        //should not eat too much performance because even RiscV is not that big
        List<IQueryableClusterController> blocks = clusterHandler.getBlocks();
        boolean correctArchitecture = true;
        List<ControlUnitController> controlUnit = new ArrayList<>();
        //we could count all blocks first for better performance
        //check Blocks
        int foundMemory = 0;
        int foundALU = 0;
        int foundSystemClock = 0;
        int foundControlUnit = 0;

        String[] aluRegisterNames = istModel.getAluRegisters();
        List<RegisterController> aluRegister = new ArrayList<>();
        ComputerBlockController alu = null;

        List<String> availableRegisters = new ArrayList<>();
        for (IQueryableClusterController block : blocks) {
            switch (block.getControllerType()) {
                case MEMORY:
                    foundMemory++;
                    assert block instanceof MemoryController;
                    // check if memory is set
                    correctArchitecture = ((MemoryController) block).isMemorySet();
                    break;
                case REGISTER:
                    availableRegisters.add(((RegisterController) block).getRegisterType());
                    for (String aluRegisterName : aluRegisterNames) {
                        if (aluRegisterName.equals(((RegisterController) block).getRegisterType())) {
                            aluRegister.add((RegisterController) block);
                        }
                    }
                    break;
                case ALU:
                    foundALU++;
                    alu = (ComputerBlockController) block;
                    break;
                case CONTROL_UNIT:
                    foundControlUnit++;
                    assert block instanceof ControlUnitController;
                    controlUnit.add((ControlUnitController) block);
                    break;
                case CLOCK:
                    foundSystemClock++;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + block.getControllerType());
            }
        }
        //check if the ALU registers are connected to the ALU
        if (alu != null) {
            for (RegisterController register : aluRegister) {
                if (!BlockPosition.isNeighbourPosition(alu.getBlockPosition(), register.getBlockPosition())) {
                    correctArchitecture = false;
                    availableRegisters.remove(register.getRegisterType());
                }
            }
        } else {
            correctArchitecture = false;
        }

        //check Registers
        boolean rightAmountOfRegisters = (availableRegisters.size() == istModel.getRegisterNames().size());
        Collections.sort(availableRegisters);
        List<String> requiredRegisters = istModel.getRegisterNames();
        Collections.sort(requiredRegisters);
        requiredRegisters.removeAll(availableRegisters);        //requiredRegisters is now missing registers
        //user wants to know which registers are missing and which are already there
        Data choseData = new Data();
        choseData.set(REGISTER_MISSING, new DataStringEntry(listToString(requiredRegisters)));
        choseData.set(REGISTER_FOUND, new DataStringEntry(listToString(availableRegisters)));
        Data rData = new Data();
        rData.set(REGISTER_REGISTERS, choseData);
        rData.set(REGISTER_WORD_LENGTH, new DataStringEntry(String.valueOf(istModel.getMemoryWordSize()))); //ToDo vielleicht wäre hier ein Int Data Element besser
        for (IQueryableClusterController block : blocks) {
            if (block.getControllerType() == BlockControllerType.REGISTER) {
                ((RegisterController) block).setData(rData);
                String initialValue = istModel.getRegisterInitialValue(((RegisterController) block).getRegisterType());
                if (initialValue == null) {
                    continue;
                }
                ((RegisterController) block).setNewValue(
                    Value.fromHex(initialValue, istModel.getMemoryWordSize()));
            }
        }
        //check if everything is correct
        if (foundControlUnit != 1 || foundALU != 1 || foundMemory != 1
            ||!requiredRegisters.isEmpty() || foundSystemClock != 1 || !rightAmountOfRegisters){      //we only allow one block
            correctArchitecture = false;
        }
        //send missing/current blocks to controlUnit model for display
        for (ControlUnitController controlUnitController : controlUnit) {
            Data clusterignData = new Data();
            clusterignData.set(CLUSTERING_MISSING_REGISTERS, new DataStringEntry(listToString(requiredRegisters)));
            clusterignData.set(CLUSTERING_FOUND_REGISTERS, new DataStringEntry(listToString(availableRegisters)));
            clusterignData.set(CLUSTERING_FOUND_MEMORY, new DataStringEntry(String.valueOf(foundMemory)));
            clusterignData.set(CLUSTERING_FOUND_ALU, new DataStringEntry(String.valueOf(foundALU)));
            clusterignData.set(CLUSTERING_FOUND_CONTROL_UNIT, new DataStringEntry(String.valueOf(foundControlUnit)));
            clusterignData.set(CLUSTERING_FOUND_CLOCK, new DataStringEntry(String.valueOf(foundSystemClock)));
            Data cucData = new Data();
            cucData.set(CONTROL_CLUSTERING, clusterignData);
            controlUnitController.setData(cucData);
        }
        return correctArchitecture;
    }

    /**
     * Converts a list of strings to a single string with spaces between the words.
     * @param list list of strings
     * @return single string with spaces between the words
     */
    private static String listToString(List<String> list) {
        StringBuilder result = new StringBuilder();
        for (String s : list) {
            result.append(s);
            result.append(" ");
        }
        return result.toString().trim();
    }

}
