package parser;

import core.types.InstructionType;

/**
 * Class representing a single input data
 * instruction.
 *
 * @author Matteo Besenzoni
 * @version 1.0
 * @since 15.05.2019
 */
public class Instruction {

    private final InstructionType type;

    private Instruction(InstructionType type) {
        this.type = type;
    }

    static Instruction parse(String i) {
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
