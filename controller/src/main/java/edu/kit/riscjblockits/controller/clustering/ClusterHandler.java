package edu.kit.riscjblockits.controller.clustering;

import edu.kit.riscjblockits.controller.blocks.*;
import edu.kit.riscjblockits.controller.simulation.SimulationTimeHandler;
import edu.kit.riscjblockits.model.busgraph.BusSystemModel;
import edu.kit.riscjblockits.model.busgraph.IQueryableBusSystem;
import edu.kit.riscjblockits.model.instructionset.IQueryableInstructionSetModel;
import edu.kit.riscjblockits.model.instructionset.InstructionSetBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds one computer cluster maximum (might be incomplete)
 */
public class ClusterHandler implements IArchitectureCheckable {

    /**
     * List of all blocks controllers in this cluster
     */
    private List<IQueryableClusterController> blocks;
    /**
     * List of all bus blocks controllers in this cluster
     */
    private List<IQueryableClusterController> busBlocks;

    /**
     * BusSystemModel of this cluster
     */
    private IQueryableBusSystem busSystemModel;

    /**
     * InstructionSetModel of this cluster
     */
    private IQueryableInstructionSetModel istModel;

    /**
     * True, if the cluster is finished building
     */
    private boolean buildingFinished;


    /**
     * Creates a new ClusterHandler and combines it with all neighbours
     * @param blockController BlockController to start the cluster with
     */
    public ClusterHandler(IQueryableClusterController blockController) {
        buildingFinished = false;
        blockController.setClusterHandler(this);
        blocks = new ArrayList<>();
        busBlocks = new ArrayList<>();
        if (blockController.getControllerType() == BlockControllerType.BUS) {
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
    public ClusterHandler(IQueryableBusSystem busSystemModel) {
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
    private void combineToNeighbours(IQueryableClusterController blockController) {
        List<ComputerBlockController> neighbourBlockControllers = blockController.getNeighbours();
        ClusterHandler actualCluster = this;
        for (IQueryableClusterController neighbourBlock: neighbourBlockControllers) {
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
    public void combine(IQueryableClusterController ownBlock,IQueryableClusterController neighbourBlock, ClusterHandler oldCluster) {
        busSystemModel.combineGraph(ownBlock.getBlockPosition(), neighbourBlock.getBlockPosition(), oldCluster.getBusSystemModel());
        blocks.addAll(oldCluster.getBlocks());
        for (IQueryableClusterController newBlock: (oldCluster.getBlocks())) {
            newBlock.setClusterHandler(this);
        }
        busBlocks.addAll(oldCluster.getBusBlocks());
        for (IQueryableClusterController newBlock: (oldCluster.getBusBlocks())) {
            newBlock.setClusterHandler(this);
        }
        System.out.println("combine");
    }

    /**
     * manages the destruction of a block and the corresponding change in the cluster
     * @param destroyedBlockController BlockController of the destroyed block
     */
    public void blockDestroyed(IQueryableClusterController destroyedBlockController) {
        //Remove Block from BusSystemModel
        List<IQueryableBusSystem> newBusSystemModels = busSystemModel.splitBusSystemModel(destroyedBlockController.getBlockPosition());
        //System.out.println(newBusSystemModels.size());
       //Remove Block from ClusterHandler Lists
        if (destroyedBlockController.getControllerType() == BlockControllerType.BUS) {
            busBlocks.remove(destroyedBlockController);
        } else {
            blocks.remove(destroyedBlockController);
        }
        //Create new ClusterHandler for fragmented BusSystemModels
        List<ClusterHandler> newClusterHandlers = new ArrayList<>();
        for (IQueryableBusSystem newBusSystemModel: newBusSystemModels) {
            newClusterHandlers.add(new ClusterHandler(newBusSystemModel));
        }
        System.out.println(newClusterHandlers.size());
        //finish the new ClusterHandlers
        for (IQueryableClusterController blockController: blocks) {
            for (ClusterHandler clusterHandler: newClusterHandlers) {
                if (clusterHandler.busSystemModel.isNode(blockController.getBlockPosition())) {
                    clusterHandler.addBlocks(blockController);
                    blockController.setClusterHandler(clusterHandler);
                }
            }
        }
        for (IQueryableClusterController blockController: busBlocks) {
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
    public void addBlocks(IQueryableClusterController blockController) {
        blocks.add(blockController);
    }

    /**
     * method to add a bus block to the cluster
     * @param blockController BlockController to add
     */
    public void addBusBlocks(IQueryableClusterController blockController) {
        busBlocks.add(blockController);
    }

    /**
     * method to get the busSystemModel of the cluster
     * @return busSystemModel of the cluster
     */
    public IQueryableBusSystem getBusSystemModel() {
        return busSystemModel;
    }

    /**
     * method to get all Blocks of the cluster
     * @return List of all Blocks of the cluster
     */
    public List<IQueryableClusterController> getBlocks() {
        return blocks;
    }

    /**
     * method to get all Bus Blocks of the cluster
     * @return List of all Bus Blocks of the cluster
     */
    public List<IQueryableClusterController> getBusBlocks() {
        return busBlocks;
    }

    /**
     * method to check whether the cluster is finished building
     */
    public void checkFinished() {
        System.out.println("Blocks: " + blocks.size() + " | BusBlocks: " + busBlocks.size());
        ClusterArchitectureHandler.checkArchitecture(null, this);
        //ToDo remove test code and implement method
        if (blocks.size() == 13) {
            System.out.println("Simulation Start [Cluster Handler]");
            buildingFinished = true;
            startSimulation();
        }
    }

    /**
     * start the simulation using the current cluster
     */
    public void startSimulation() {
        // TODO check cast
        List<IQueryableSimController> simControllers = blocks.stream().map(IQueryableSimController.class::cast).toList();
        SimulationTimeHandler sim = new SimulationTimeHandler(simControllers);
        for (IQueryableComputerController c : blocks) {
            if (c.getControllerType() == BlockControllerType.CLOCK) {
                ((SystemClockController) c).setSimulationTimeHandler(sim);
            }
        }
    }

}
