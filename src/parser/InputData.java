package parser;

import core.Segment;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the contents of an
 * input data file.
 *
 * @author Matteo Besenzoni
 * @version 1.0
 * @since 15.05.2019
 */
public class InputData {

    private final List<Segment> segments;
    private final List<Instruction> instructions;

    InputData() {
        this.segments = new ArrayList<>();
        this.instructions = new ArrayList<>();
    }

    void addSegment(Segment s) {
        segments.add(s);
    }

    void addInstruction(Instruction i) {
        instructions.add(i);
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Segment s : segments) {
            sb.append(s.toString());
            sb.append(System.lineSeparator());
        }

        for (Instruction i : instructions) {
            sb.append(i.toString());
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }
}
