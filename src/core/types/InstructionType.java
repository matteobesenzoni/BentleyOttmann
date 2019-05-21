package core.types;

/**
 * InstructionType enumerator.
 *
 * @author Matteo Besenzoni
 * @version 1.0
 * @since 15.05.2019
 */
public enum InstructionType {
    STEP("step"), STEP_P("step -p"), STATUS("status"), RUN("run"), ERROR("error");

    private String instruction;

    InstructionType(String instruction) {
        this.instruction = instruction;
    }

    public static InstructionType parse(String i) {
        if (i == null)
            return ERROR;
        switch (i.toLowerCase()) {
            case "step":
                return STEP;
            case "step -p":
                return STEP_P;
            case "status":
                return STATUS;
            case "run":
                return RUN;
            default:
                return ERROR;
        }
    }

    @Override
    public String toString() {
        return instruction;
    }
}
