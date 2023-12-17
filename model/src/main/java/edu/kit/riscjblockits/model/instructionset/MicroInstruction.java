package edu.kit.riscjblockits.model.instructionset;

public class MicroInstruction {
    private final String action;
    private final String[] actionParameter;
    private final String[] memoryAction;
    private final String[] from;
    private final String to;

    public MicroInstruction(String action, String[] actionParameter, String[] memoryAction, String[] from, String to) {
        this.action = action;
        this.actionParameter = actionParameter;
        this.memoryAction = memoryAction;
        this.from = from;
        this.to = to;
    }

    public static MicroInstruction fromJson(String s) {
        return null;
    }

    public String[] getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getAction() {
        return action;
    }

    public String[] getActionParameter() {
        return actionParameter;
    }

    public String[] getMemoryAction() {
        return memoryAction;
    }
}
