package edu.kit.riscjblockits.controller.computerhandler;

import edu.kit.riscjblockits.controller.blocks.BlockController;

import java.util.List;

public class SimulationSequenceHandler implements Runnable {

    public SimulationSequenceHandler(List<BlockController> blockControllers) {

    }

    @Override
    public void run() {
        System.out.println("run test");
//        try {
//            wait(600);
//        } catch (InterruptedException e) {
//            System.out.println("wait failed");
//            throw new RuntimeException(e);
//        }

    }


}
