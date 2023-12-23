package edu.kit.riscjblockits.controller.computerhandler;

import edu.kit.riscjblockits.controller.blocks.BlockController;
import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
import edu.kit.riscjblockits.controller.blocks.SystemClockController;
import edu.kit.riscjblockits.model.BusSystemModel;
import edu.kit.riscjblockits.model.instructionset.InstructionSetBuilder;
import edu.kit.riscjblockits.model.instructionset.InstructionSetModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds one computer cluster maximum (might be incomplete)
 */
public class ClusterHandler implements IArchitectureCheckable {

    private List<BlockController> blocks;
    private List<BlockController> busBlocks;
    private BusSystemModel busSystemModel;

    private InstructionSetModel istModel;

    private boolean buildingFinished;

    public ClusterHandler(BlockController blockController) {
        buildingFinished = false;
        blockController.setClusterHandler(this);
        blocks = new ArrayList<>();
        busBlocks = new ArrayList<>();
        if (blockController.isBus()) {
            busBlocks.add(blockController);
        } else {
            blocks.add(blockController);
        }
        busSystemModel = new BusSystemModel(blockController.getBlockPosition());
        System.out.println("Start combine");
        combineToNeighbours(blockController);
        //ToDo remove test code
        istModel = InstructionSetBuilder.buildInstructionSetModelMima();
    }

    public ClusterHandler(BusSystemModel busSystemModel) {
        buildingFinished = false;
        blocks = new ArrayList<>();
        busBlocks = new ArrayList<>();
        this.busSystemModel = busSystemModel;
        //ToDo remove test code
        istModel = InstructionSetBuilder.buildInstructionSetModelMima();
    }

    private void combineToNeighbours(BlockController blockController) {
        List<BlockController> neighbourBlockControllers = blockController.getNeighbours();
        ClusterHandler actualCluster = this;
        for (BlockController neighbourBlock: neighbourBlockControllers) {
            neighbourBlock.getClusterHandler().combine(neighbourBlock, blockController, actualCluster);
            actualCluster = neighbourBlock.getClusterHandler();
            //busSystemModel.combineGraph(blockController.getBlockPosition(), neighbourBlock.getBlockPosition(), neighbourBlock.getClusterHandler().getBusSystemModel());
            //blocks.addAll(neighbourBlock.getClusterHandler().getBlocks());
            //for (BlockController newBlock: (neighbourBlock.getClusterHandler().getBlocks())) {
            //    newBlock.setClusterHandler(this);
            //}
            //busBlocks.addAll(neighbourBlock.getClusterHandler().getBusBlocks());
            //for (BlockController newBlock: (neighbourBlock.getClusterHandler().getBusBlocks())) {
            //    newBlock.setClusterHandler(this);
            //}
            //System.out.println("combine");
        }
    }

    public void combine(BlockController ownBlock,BlockController neighbourBlock, ClusterHandler oldCluster) {
        busSystemModel.combineGraph(ownBlock.getBlockPosition(), neighbourBlock.getBlockPosition(), oldCluster.getBusSystemModel());
        blocks.addAll(oldCluster.getBlocks());
        for (BlockController newBlock: (oldCluster.getBlocks())) {
            newBlock.setClusterHandler(this);
        }
        busBlocks.addAll(oldCluster.getBusBlocks());
        for (BlockController newBlock: (oldCluster.getBusBlocks())) {
            newBlock.setClusterHandler(this);
        }
        System.out.println("combine");
    }


    public void blockDestroyed(BlockController destroyedBlockController) {
        List<BusSystemModel> newBusSystemModels = busSystemModel.splitBusSystemModel(destroyedBlockController.getBlockPosition());
        //System.out.println(newBusSystemModels.size());
        if (destroyedBlockController.isBus()) {
            busBlocks.remove(destroyedBlockController);
        } else {
            blocks.remove(destroyedBlockController);
        }
        List<ClusterHandler> newClusterHandlers = new ArrayList<>();
        for (BusSystemModel newBusSystemModel: newBusSystemModels) {
            newClusterHandlers.add(new ClusterHandler(newBusSystemModel));
        }
        System.out.println(newClusterHandlers.size());
        for (BlockController blockController: blocks) {
            for (ClusterHandler clusterHandler: newClusterHandlers) {
                if (clusterHandler.busSystemModel.isNode(blockController.getBlockPosition())) {
                    clusterHandler.addBlocks(blockController);
                    blockController.setClusterHandler(clusterHandler);
                }
            }
        }
        for (BlockController blockController: busBlocks) {
            for (ClusterHandler newclusterHandler: newClusterHandlers) {
                if (newclusterHandler.busSystemModel.isNode(blockController.getBlockPosition())) {
                    newclusterHandler.addBusBlocks(blockController);
                    blockController.setClusterHandler(newclusterHandler);
                }
            }
        }
        //ToDo stop Simulation if architecture invalid
    }
    public void addBlocks(BlockController blockController) {
        blocks.add(blockController);
    }
    public void addBusBlocks(BlockController blockController) {
        busBlocks.add(blockController);
    }

    public BusSystemModel getBusSystemModel() {
        return busSystemModel;
    }

    public List<BlockController> getBlocks() {
        return blocks;
    }

    public List<BlockController> getBusBlocks() {
        return busBlocks;
    }

    public void checkFinished() {
        ClusterArchitectureHandler.checkArchitecture(null);
        //ToDo remove test code and implement method
        if (blocks.size() == 13) {
            System.out.println("Simulation Start [Cluster Handler]");
            buildingFinished = true;
            startSimulation();
        }
    }

    public void startSimulation() {
        SimulationTimeHandler sim = new SimulationTimeHandler(blocks);
        for (BlockController c : blocks) {
            if (c.getControllerType() == BlockControllerType.CLOCK) {
                ((SystemClockController) c).setSimulationTimeHandler(sim);
            }
        }
    }

}
