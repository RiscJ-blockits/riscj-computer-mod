package edu.kit.riscjblockits.controller.clustering;


import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
import edu.kit.riscjblockits.controller.blocks.IQueryableClusterController;
import edu.kit.riscjblockits.controller.blocks.IQueryableComputerController;
import edu.kit.riscjblockits.controller.blocks.IQueryableSimController;
import edu.kit.riscjblockits.controller.blocks.SystemClockController;
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
     * True, if the cluster is finished building.
     */
    private boolean buildingFinished;


    /**
     * Creates a new ClusterHandler and combines it with all neighbours
     * @param blockController BlockController to start the cluster with
     */
    public ClusterHandler(IQueryableClusterController blockController) {
        blocks = new ArrayList<>();
        busBlocks = new ArrayList<>();
        blockController.setClusterHandler(this);
        busSystemModel = new BusSystemModel(blockController.getBlockPosition());

        if (blockController.getControllerType() == BlockControllerType.BUS) {
            busBlocks.add(blockController);
        } else {
            blocks.add(blockController);
        }

        combineToNeighbours(blockController);
    }

    /**
     * Creates a new ClusterHandler and combines it with all neighbours
     * @param busSystemModel BusSystemModel to start the cluster with
     */
    public ClusterHandler(IQueryableBusSystem busSystemModel) {
        blocks = new ArrayList<>();
        busBlocks = new ArrayList<>();
        this.busSystemModel = busSystemModel;
    }

    /**
     * Combines the given block with the cluster
     * @param blockController BlockController to combine
     */
    private void combineToNeighbours(IQueryableClusterController blockController) {
        List<IQueryableClusterController> neighbourBlockControllers = new ArrayList<>();
        List<IQueryableClusterController> neighboursToCombine = new ArrayList<>();
        //fill neighbourBlockControllers with all neighbours, which exists in a neighbourCluster
        for (IQueryableClusterController neighbourBlock: blockController.getNeighbours()) {
            if (neighbourBlock.getClusterHandler().busSystemModel.isNode(neighbourBlock.getBlockPosition())) {
                neighbourBlockControllers.add(neighbourBlock);
            }
        }
        //fill neighboursToCombine with all neighbours, which should be combined
        if (blockController.getControllerType() == BlockControllerType.BUS) {
            neighboursToCombine.addAll(neighbourBlockControllers);
        } else {
            ClusterHandler neighbourCluster = null;
            for (IQueryableClusterController neighbourBlock: neighbourBlockControllers) {
                if (neighbourBlock.getControllerType() == BlockControllerType.BUS &&
                        (neighboursToCombine.isEmpty() || neighbourCluster == neighbourBlock.getClusterHandler())) {
                    neighboursToCombine.add(neighbourBlock);
                    neighbourCluster = neighbourBlock.getClusterHandler();
                }
            }
        }
        //combine all neighbours in neighboursToCombine
        ClusterHandler actualCluster = this;
        for (IQueryableClusterController neighbourBlock: neighboursToCombine) {
            neighbourBlock.getClusterHandler().combine(neighbourBlock, blockController, actualCluster);
            actualCluster = neighbourBlock.getClusterHandler();
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
        if (oldCluster != this) {
            blocks.addAll(oldCluster.getBlocks());
            for (IQueryableClusterController newBlock: (oldCluster.getBlocks())) {
                newBlock.setClusterHandler(this);
            }
            busBlocks.addAll(oldCluster.getBusBlocks());
            for (IQueryableClusterController newBlock: (oldCluster.getBusBlocks())) {
                newBlock.setClusterHandler(this);
            }
        }
    }

    /**
     * manages the destruction of a block and the corresponding change in the cluster
     * @param destroyedBlockController BlockController of the destroyed block
     */
    public void blockDestroyed(IQueryableClusterController destroyedBlockController) {
        //Remove Block from ClusterHandler Lists
        busBlocks.remove(destroyedBlockController);
        List<IQueryableBusSystem> newBusSystemModels =
                busSystemModel.splitBusSystemModel(destroyedBlockController.getBlockPosition());
        List<ClusterHandler> newClusterHandlers = new ArrayList<>();
        for (IQueryableBusSystem newBusSystemModel: newBusSystemModels) {
            newClusterHandlers.add(new ClusterHandler(newBusSystemModel));
        }
        //finish the new ClusterHandlers
        for (IQueryableClusterController blockController: blocks) {
            for (ClusterHandler newclusterHandler: newClusterHandlers) {
                if (newclusterHandler.busSystemModel.isNode(blockController.getBlockPosition())) {
                    newclusterHandler.addBlocks(blockController);
                    blockController.setClusterHandler(newclusterHandler);
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
        for (ClusterHandler newclusterHandler: newClusterHandlers) {
            if (newclusterHandler.getBusBlocks().isEmpty()) {
                IQueryableClusterController neighbourBlockController = newclusterHandler.getBlocks().get(0);
                newclusterHandler.combineToNeighbours(neighbourBlockController);
                neighbourBlockController.getClusterHandler().checkFinished();
            } else {
                newclusterHandler.checkFinished();
            }
        }
    }



    /**
     * method to add a block to the cluster
     * @param blockController BlockController to add
     */
    private void addBlocks(IQueryableClusterController blockController) {
        blocks.add(blockController);
    }

    /**
     * method to add a bus block to the cluster
     * @param blockController BlockController to add
     */
    private void addBusBlocks(IQueryableClusterController blockController) {
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
     * Sets the given IQueryableInstructionSetModel as the instruction set model for this object.
     * @param istModel The IQueryableInstructionSetModel to set.
     * @return true if the set operation was successful, false if there is already an instruction set model set.
     */
    public boolean setIstModel(IQueryableInstructionSetModel istModel) {
        if (this.istModel == null) {
            this.istModel = istModel;
            return true;
        }
        return false;
    }

    /**
     * Removes the instruction set model from this object.
     */
    public void removeIstModel() {
        istModel = null;
    }

    /**
     * method to check whether the cluster is finished building
     */
    public void checkFinished() {
        System.out.println("Blocks: " + blocks.size() + " | BusBlocks: " + busBlocks.size());
        if (istModel != null) {
            buildingFinished = ClusterArchitectureHandler.checkArchitecture(istModel, this);
        }
        if (buildingFinished) {
            System.out.println("Simulation Start [Cluster Handler]");
            startSimulation();
        } else {
            //TODO: remove Simulation
        }
    }

    /**
     * start the simulation using the current cluster
     */
    private void startSimulation() {
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
