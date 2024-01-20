package edu.kit.riscjblockits.controller.clustering;

import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.blocks.IQueryableClusterController;
import edu.kit.riscjblockits.controller.blocks.RegisterController;
import edu.kit.riscjblockits.model.instructionset.IQueryableInstructionSetModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * checks clusters for valid architectures
 */
public class ClusterArchitectureHandler {

    /**
     * checks a given cluster if it is a specific valid architecture
     */
    public static boolean checkArchitecture(IQueryableInstructionSetModel istModel, ClusterHandler clusterHandler){
        //(send choices to Register Models)
        //send missing/current blocks to controllUnit model
        List<IQueryableClusterController> blocks = clusterHandler.getBlocks();

        //check Registers               // ToDo was wenn zwei Register den gleichen Namen haben?
        List<String> availableRegisters = new ArrayList<>();
        for (IQueryableClusterController block : blocks) {
            if (block.getControllerType() == BlockControllerType.REGISTER) {
                availableRegisters.add(((RegisterController) block).getRegisterType());
            }
        }
        Collections.sort(availableRegisters);
        List<String> requiredRegisters = istModel.getRegisterNames();
        Collections.sort(requiredRegisters);
        //requiredRegisters is now missing registers
        requiredRegisters.removeAll(availableRegisters);








        return false;
    }

}
