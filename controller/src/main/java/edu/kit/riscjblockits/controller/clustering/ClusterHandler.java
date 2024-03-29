package edu.kit.riscjblockits.controller.clustering;

import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
import edu.kit.riscjblockits.controller.blocks.BusController;
import edu.kit.riscjblockits.controller.blocks.ControlUnitController;
import edu.kit.riscjblockits.controller.blocks.IQueryableClusterController;
import edu.kit.riscjblockits.controller.blocks.IQueryableSimController;
import edu.kit.riscjblockits.controller.blocks.RegisterController;
import edu.kit.riscjblockits.controller.blocks.SystemClockController;
import edu.kit.riscjblockits.controller.simulation.SimulationTimeHandler;
import edu.kit.riscjblockits.model.busgraph.BusSystemModel;
import edu.kit.riscjblockits.model.busgraph.IBusSystem;
import edu.kit.riscjblockits.model.busgraph.IQueryableBusSystem;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.instructionset.IQueryableInstructionSetModel;

import java.util.ArrayList;
import java.util.List;

import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_FOUND;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_MISSING;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_REGISTERS;

/**
 * Holds one computer cluster.
 */
public class ClusterHandler {

    /**
     * List of all blocks controllers in this cluster.
     */
    private final List<IQueryableClusterController> blocks;
    /**
     * List of all bus blocks controllers in this cluster.
     */
    private final List<IQueryableClusterController> busBlocks;

    /**
     * BusSystemModel of this cluster.
     */
    private final IQueryableBusSystem busSystemModel;

    /**
     * InstructionSetModel of this cluster.
     */
    private IQueryableInstructionSetModel istModel;

    /**
     * True, if the cluster is finished building.
     */
    private boolean buildingFinished;

    /**
     * List of marked Blocks to reCluster them later if they get a new Neighbor.
     */
    private List<IQueryableClusterController> markedBlocks;


    /**
     * Creates a new ClusterHandler with one Block and combines it with all neighbors.
     * @param blockController BlockController of the first block in the cluster.
     */
    public ClusterHandler(IQueryableClusterController blockController) {
        markedBlocks = new ArrayList<>();
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
     * Creates a new ClusterHandler and with the given BusSystemModel.
     * @param busSystemModel BusSystemModel build the cluster.
     */
    public ClusterHandler(IQueryableBusSystem busSystemModel) {
        markedBlocks = new ArrayList<>();
        blocks = new ArrayList<>();
        busBlocks = new ArrayList<>();
        this.busSystemModel = busSystemModel;
    }

    /**
     * Combines the cluster with all neighbor-clusters of the given BlockController.
     * @param blockController BlockController to combine.
     */
    private void combineToNeighbours(IQueryableClusterController blockController) {
        List<IQueryableClusterController> neighbourBlockControllers = new ArrayList<>();
        List<IQueryableClusterController> neighboursToCombine = new ArrayList<>();
        //fill neighbourBlockControllers with all neighbors, which exists in a neighbourCluster
        for (IQueryableClusterController neighbourBlock: blockController.getNeighbours()) {
            if (neighbourBlock.getClusterHandler().busSystemModel.isNode(neighbourBlock.getBlockPosition())) {
                neighbourBlockControllers.add(neighbourBlock);
            }
        }
        //fill neighboursToCombine with all neighbors, which should be combined
        if (blockController.getControllerType() == BlockControllerType.BUS) {
            for (IQueryableClusterController neighbourBlock: neighbourBlockControllers) {
                if (neighbourBlock.getControllerType() == BlockControllerType.BUS) {
                    neighboursToCombine.add(neighbourBlock);
                } else if (neighbourBlock.getClusterHandler().getBusBlocks().isEmpty()) {
                    neighboursToCombine.add(neighbourBlock);
                } else {
                    markedBlocks.add(neighbourBlock);
                }
            }
        } else {
            ClusterHandler neighbourCluster = null;
            for (IQueryableClusterController neighbourBlock: neighbourBlockControllers) {
                if (neighbourBlock.getControllerType() == BlockControllerType.BUS) {
                    if((neighboursToCombine.isEmpty() || neighbourCluster == neighbourBlock.getClusterHandler())) {
                        neighboursToCombine.add(neighbourBlock);
                        neighbourCluster = neighbourBlock.getClusterHandler();
                    } else {
                        markedBlocks.add(blockController);
                    }
                }
            }
        }
        //combine all neighbors in neighboursToCombine
        ClusterHandler actualCluster = this;
        for (IQueryableClusterController neighbourBlock: neighboursToCombine) {
            neighbourBlock.getClusterHandler().combine(neighbourBlock, blockController, actualCluster);
            actualCluster = neighbourBlock.getClusterHandler();
        }
        blockController.getClusterHandler().reClusterMarkedBlocks();
    }

    /**
     * checks if markedBlocks-neighbours are now in the same cluster and add Edges to the busSystemModel.
     */
    private void reClusterMarkedBlocks() {
        List<IQueryableClusterController> newMarkedBlocks = new ArrayList<>();
        for (IQueryableClusterController block: markedBlocks) {
            for (IQueryableClusterController neighbour: block.getNeighbours()) {
                if (neighbour.getClusterHandler().equals(this)) {
                    busSystemModel.addEdge(block.getBlockPosition(), neighbour.getBlockPosition());
                } else {
                    if (!newMarkedBlocks.contains(block)) {
                        newMarkedBlocks.add(block);
                    }
                }
            }
        }
        markedBlocks = newMarkedBlocks;
    }

    /**
     * Combines this cluster with another cluster.
     * @param ownBlock BlockController of this cluster.
     * @param neighbourBlock BlockController of the other cluster.
     * @param oldCluster ClusterHandler of the other cluster.
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
            markedBlocks.addAll(oldCluster.markedBlocks);
        }
    }

    /**
     * manages the destruction of a block and the corresponding change in the cluster.
     * @param destroyedBlockController BlockController of the destroyed block.
     */
    public void blockDestroyed(IQueryableClusterController destroyedBlockController) {
        //Remove Block from ClusterHandler Lists
        busBlocks.remove(destroyedBlockController);
        List<IQueryableBusSystem> newBusSystemModels =
                busSystemModel.splitBusSystemModel(destroyedBlockController.getBlockPosition());
        busSystemModel.resetVisualisation();
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
                    newclusterHandler.markedBlocks.add(blockController);
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
            newclusterHandler.reClusterMarkedBlocks();
        }
    }

    /**
     * Method to add a block to the cluster.
     * Only add it to the blocklist.
     * Should only be used for blocks with multiple registers
     * @param blockController BlockController to add
     */
    public void addBlocks(IQueryableClusterController blockController) {
        blocks.add(blockController);
    }

    /**
     * method to add a bus block to the cluster.
     * @param blockController BlockController to add.
     */
    private void addBusBlocks(IQueryableClusterController blockController) {
        busBlocks.add(blockController);
    }

    /**
     * method to get the busSystemModel of the cluster.
     * @return busSystemModel of the cluster.
     */
    public IQueryableBusSystem getBusSystemModel() {
        return busSystemModel;
    }

    /**
     * method to get all Blocks of the cluster.
     * @return List of all Blocks of the cluster.
     */
    public List<IQueryableClusterController> getBlocks() {
        return blocks;
    }

    /**
     * method to get all Bus Blocks of the cluster.
     * @return List of all Bus Blocks of the cluster.
     */
    public List<IQueryableClusterController> getBusBlocks() {
        return busBlocks;
    }


    /**
     * Sets the given IQueryableInstructionSetModel as the instruction set model for this object.
     * @param istModel The IQueryableInstructionSetModel to set. Is null if the model should be removed.
     * @return true if the set operation was successful, false if there is already an instruction set model set.
     */
    public boolean setIstModel(IQueryableInstructionSetModel istModel) {
        if (istModel == null) {
            removeIstModel();
            checkFinished();
            return false;
        }
        int cuCount = 0;
        for (IQueryableClusterController block: blocks) {
            if (block.getControllerType() == BlockControllerType.CONTROL_UNIT) {
                cuCount++;
            }
        }
        if (cuCount > 1) {
            removeIstModel();
            checkFinished();
            return false;
        }
        this.istModel = istModel;
        checkFinished();
        return true;
    }

    /**
     * Removes the instruction set model from this object.
     */
    public void removeIstModel() {
        istModel = null;
        for (IQueryableClusterController block: blocks) {
            if (block.getControllerType() == BlockControllerType.CONTROL_UNIT) {
                ((ControlUnitController) block).rejectIstModel();
            }
        }
    }

    /**
     * method to check whether the cluster is finished building.
     */
    public void checkFinished() {
        if (istModel != null) {
            buildingFinished = ClusterArchitectureHandler.checkArchitecture(istModel, this);
        } else {
            //remove data from registers
            Data choseData = new Data();
            choseData.set(REGISTER_MISSING, new DataStringEntry(""));
            choseData.set(REGISTER_FOUND, new DataStringEntry(""));
            Data rData = new Data();
            rData.set(REGISTER_REGISTERS, choseData);
            for (IQueryableClusterController block : blocks) {
                if (block.getControllerType() == BlockControllerType.REGISTER) {
                    ((RegisterController) block).setData(rData);
                }
            }
            buildingFinished = false;
        }
        if (buildingFinished) {
            for (IQueryableClusterController busBlock: busBlocks) {
                ((BusController) busBlock).setBusSystemModel((BusSystemModel) busSystemModel);
            }
            startSimulation();
        } else {
            // stop simulation if it was running and the cluster is not finished anymore
            stopSimulation();
        }
    }

    /**
     * start the simulation using the current cluster.
     */
    private void startSimulation() {
        List<IQueryableSimController> simControllers = blocks.stream().map(IQueryableSimController.class::cast).toList();
        SimulationTimeHandler sim = new SimulationTimeHandler(simControllers, (IBusSystem) busSystemModel);
        for (IQueryableClusterController c : blocks) {
            if (c.getControllerType() == BlockControllerType.CLOCK) {
                ((SystemClockController) c).setSimulationTimeHandler(sim);
            }
        }
    }

    private void stopSimulation() {
        for (IQueryableClusterController c : blocks) {
            if (c.getControllerType() == BlockControllerType.CLOCK) {
                ((SystemClockController) c).setSimulationTimeHandler(null);
            }
            c.stopVisualisation();
        }
        if (busSystemModel != null) {
            busSystemModel.resetVisualisation();
        }
    }
}
