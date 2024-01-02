package edu.kit.riscjblockits.model.instructionset;

public class MemoryInstruction extends MicroInstruction {

    public MemoryInstruction(String[] from, String to) {
        super(from, to);
    }

    @Override
    public void execute(IExecutor executor) {
        executor.execute(this);
    }
}
