package edu.kit.riscjblockits.controller.clustering;

import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.blocks.ControlUnitController;
import edu.kit.riscjblockits.controller.blocks.IQueryableClusterController;
import edu.kit.riscjblockits.controller.blocks.RegisterController;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.instructionset.IQueryableInstructionSetModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Checks clusters for valid architectures.
 */
public class ClusterArchitectureHandler {

    /**
     * Private constructor to prevent instantiation.
     */
    private ClusterArchitectureHandler() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Checks a given cluster if it is a specific valid architecture.
     */
    public static boolean checkArchitecture(IQueryableInstructionSetModel istModel, ClusterHandler clusterHandler){
        //is called after every block modification
        //should not eat too much performance because even RiscV is not that big

        List<IQueryableClusterController> blocks = clusterHandler.getBlocks();
        boolean correctArchitecture = true;
        List<ControlUnitController> controlUnit = new ArrayList<>();

        //check Registers
        List<String> availableRegisters = new ArrayList<>();
        for (IQueryableClusterController block : blocks) {
            if (block.getControllerType() == BlockControllerType.REGISTER) {
                availableRegisters.add(((RegisterController) block).getRegisterType());
            }
        }
        Collections.sort(availableRegisters);
        List<String> requiredRegisters = istModel.getRegisterNames();
        Collections.sort(requiredRegisters);
        requiredRegisters.removeAll(availableRegisters);        //requiredRegisters is now missing registers
        //user wants to know which registers are missing and which are already there
        Data choseData = new Data();
        choseData.set("missing", new DataStringEntry(listToString(requiredRegisters)));
        choseData.set("found", new DataStringEntry(listToString(availableRegisters)));
        Data rData = new Data();
        rData.set("registers", choseData);
        for (IQueryableClusterController block : blocks) {
            if (block.getControllerType() == BlockControllerType.REGISTER) {
                ((RegisterController) block).setData(rData);
            }
        }

        //check Memory
        int foundMemory = countBlocksOfType(blocks, BlockControllerType.MEMORY);

        //check ALU
        int foundALU = countBlocksOfType(blocks, BlockControllerType.ALU);

        //check ControlUnit
        int foundControlUnit = 0;
        for (IQueryableClusterController block : blocks) {
            if (block.getControllerType() == BlockControllerType.CONTROL_UNIT) {
                foundControlUnit++;
                controlUnit.add((ControlUnitController) block);
            }
        }

        //check SystemClock
        int foundSystemClock = countBlocksOfType(blocks, BlockControllerType.CLOCK);

        //check if everything is correct
        if (foundControlUnit != 1 || foundALU != 1 || foundMemory != 1
            ||!requiredRegisters.isEmpty() || foundSystemClock != 1){      //we only allow one block
            correctArchitecture = false;
        }

        //send missing/current blocks to controlUnit model for display
        for (ControlUnitController controlUnitController : controlUnit) {
            Data clusterignData = new Data();
            clusterignData.set("missingRegisters", new DataStringEntry(listToString(requiredRegisters)));
            clusterignData.set("foundRegisters", new DataStringEntry(listToString(availableRegisters)));
            clusterignData.set("foundMemory", new DataStringEntry(String.valueOf(foundMemory)));
            clusterignData.set("foundALU", new DataStringEntry(String.valueOf(foundALU)));
            clusterignData.set("foundControlUnit", new DataStringEntry(String.valueOf(foundControlUnit)));
            clusterignData.set("foundSystemClock", new DataStringEntry(String.valueOf(foundSystemClock)));
            Data cucData = new Data();
            cucData.set("clustering", clusterignData);
            controlUnitController.setData(cucData);
        }
        System.out.println("CheckArchitecture: " + correctArchitecture + " Missing Registers: " + listToString(requiredRegisters));
        return correctArchitecture;
    }

    /**
     * Counts the blocks of a specific type in a list of blocks.
     * @param blocks
     * @param blockType
     * @return
     */
    private static int countBlocksOfType(List<IQueryableClusterController> blocks, BlockControllerType blockType) {
        int count = 0;
        for (IQueryableClusterController block : blocks) {
            if (block.getControllerType() == blockType) {
                count++;
            }
        }
        return count;
    }

    /**
     * Converts a list of strings to a single string with spaces between the words.
     * @param list
     * @return
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
