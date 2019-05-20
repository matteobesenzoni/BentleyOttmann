package parser;

import core.Segment;

import java.util.ArrayList;
import java.util.List;

public class InputData {

    private final List<Segment> segments;
    private final List<Instruction> instructions;

    public InputData() {
        this.segments = new ArrayList<>();
        this.instructions = new ArrayList<>();
    }

    public void addSegment(Segment s) {
        segments.add(s);
    }

    public void addInstruction(Instruction i) {
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
