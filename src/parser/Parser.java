package parser;

import core.Segment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Parser {

    public static InputData parseFile(String path) {
        InputData i = new InputData();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {

            int segments = Integer.valueOf(reader.readLine().trim());
            for (int k = 0; k < segments; k++) {
                String segment_s = reader.readLine().trim();
                String[] values = segment_s.split(" ");
                if (values.length != 5) {
                    System.out.println("Error while parsing segment [" + (k + 1) + "]: 5 numbers expected, " + values.length + " found.");
                    System.exit(1);
                }
                Segment segment = new Segment(
                        k,
                        Float.parseFloat(values[0]),
                        Float.parseFloat(values[1]),
                        Float.parseFloat(values[2]),
                        Float.parseFloat(values[3]),
                        Float.parseFloat(values[4])
                );
                i.addSegment(segment);
            }
            String instruction_s;
            while ((instruction_s = reader.readLine()) != null) {
                Instruction instruction = Instruction.parse(instruction_s);
                if (instruction == null) {
                    System.out.println("Error while parsing instruction: " + instruction_s);
                    System.exit(1);
                }
                i.addInstruction(instruction);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Unable to find file \"" + path + "\"");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Unable to parse # of segments (line 1)");
        }
        return i;
    }
}
