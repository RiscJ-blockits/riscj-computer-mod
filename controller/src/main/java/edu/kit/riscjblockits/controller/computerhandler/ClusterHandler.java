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

    /**
     * List of all blocks controllers in this cluster
     */
    private List<BlockController> blocks;
    /**
     * List of all bus blocks controllers in this cluster
     */
    private List<BlockController> busBlocks;

    /**
     * BusSystemModel of this cluster
     */
    private BusSystemModel busSystemModel;

    /**
     * InstructionSetModel of this cluster
     */
    private InstructionSetModel istModel;

    /**
     * True if the cluster is finished building
     */
    private boolean buildingFinished;


    /**
     * Creates a new ClusterHandler and combines it with all neighbours
     * @param blockController BlockController to start the cluster with
     */
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
        System.out.println("Model ready");
    }

    /**
     * Creates a new ClusterHandler and combines it with all neighbours
     * @param busSystemModel BusSystemModel to start the cluster with
     */
    public ClusterHandler(BusSystemModel busSystemModel) {
        buildingFinished = false;
        blocks = new ArrayList<>();
        busBlocks = new ArrayList<>();
        this.busSystemModel = busSystemModel;
        //ToDo remove test code
        istModel = InstructionSetBuilder.buildInstructionSetModelMima();
    }

    /**
     * Combines the given block with the cluster
     * @param blockController BlockController to combine
     */
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

    /**
     * Combines this cluster with another cluster
     * @param ownBlock BlockController of this cluster
     * @param neighbourBlock BlockController of the other cluster
     * @param oldCluster ClusterHandler of the other cluster
     */
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


    /**
     * manages the destruction of a block and the corresponding change in the cluster
     * @param destroyedBlockController BlockController of the destroyed block
     */
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

    /**
     * method to add a block to the cluster
     * @param blockController BlockController to add
     */
    public void addBlocks(BlockController blockController) {
        blocks.add(blockController);
    }

    /**
     * method to add a bus block to the cluster
     * @param blockController BlockController to add
     */
    public void addBusBlocks(BlockController blockController) {
        busBlocks.add(blockController);
    }

    /**
     * method to get the busSystemModel of the cluster
     * @return busSystemModel of the cluster
     */
    public BusSystemModel getBusSystemModel() {
        return busSystemModel;
    }

    /**
     * method to get all Blocks of the cluster
     * @return List of all Blocks of the cluster
     */
    public List<BlockController> getBlocks() {
        return blocks;
    }

    /**
     * method to get all Bus Blocks of the cluster
     * @return List of all Bus Blocks of the cluster
     */
    public List<BlockController> getBusBlocks() {
        return busBlocks;
    }

    /**
     * method to check whether the cluster is finished building
     */
    public void checkFinished() {
        ClusterArchitectureHandler.checkArchitecture(null);
        //ToDo remove test code and implement method
        if (blocks.size() == 13) {
            System.out.println("Simulation Start [Cluster Handler]");
            buildingFinished = true;
            startSimulation();
        }
    }

    /**
     * start the simulation, using the current cluster
     */
    public void startSimulation() {
        SimulationTimeHandler sim = new SimulationTimeHandler(blocks);
        for (BlockController c : blocks) {
            if (c.getControllerType() == BlockControllerType.CLOCK) {
                ((SystemClockController) c).setSimulationTimeHandler(sim);
            }
        }
    }

}
