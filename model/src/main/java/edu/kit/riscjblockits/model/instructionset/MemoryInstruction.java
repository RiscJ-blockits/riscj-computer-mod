package edu.kit.riscjblockits.model.instructionset;

/**
 * Represents a memory action as part of a micro instruction.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public class MemoryInstruction extends MicroInstruction {

    private final String flag;

    public MemoryInstruction(String[] from, String to, String flag) {
        super(from, to);
        this.flag = flag;
    }

    /**
     * Hands the memory action to the executor and executes it as part of a visitor pattern.
     */
    @Override
    public void execute(IExecutor executor) {
        executor.execute(this);
    }

    @Override
    public MicroInstruction clone(String[] from, String to) {
        return new MemoryInstruction(from, to, flag);
    }

    @Override
    protected MemoryInstruction clone() {
        return new MemoryInstruction(getFrom(), getTo(), flag);
    }



    public String getFlag() {
        return flag;
    }
}
