package edu.kit.riscjblockits.model.instructionset;

public abstract class MicroInstruction {

    String[] from;
    String to;

    public MicroInstruction(String[] from, String to) {
        this.from = from;
        this.to = to;
    }

    public String[] getFrom() {
        return from;
    }
    public String getTo() {
        return to;
    }


    public abstract void execute(IExecutor executor);

}
