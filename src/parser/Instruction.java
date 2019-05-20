package parser;

import core.types.InstructionType;

public class Instruction {

    private final InstructionType type;

    private Instruction(InstructionType type) {
        this.type = type;
    }

    public static Instruction parse(String i) {
        InstructionType type = InstructionType.parse(i);
        if (type == InstructionType.ERROR)
            return null;
        return new Instruction(type);
    }

    public InstructionType getType() {
        return type;
    }

    @Override
    public String toString() {
        switch (type) {
            case STEP:
                return "step";
            case STEP_P:
                return "step -p";
            case STATUS:
                return "status";
            case RUN:
                return "run";
            default:
                return "";
        }
    }
}
