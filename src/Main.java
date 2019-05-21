import core.BentleyOttmann;
import gui.Window;
import parser.InputData;
import parser.Instruction;
import parser.Parser;

/**
 * Main class. Reads arguments and
 * handles the algorithm execution
 * and GUI.
 *
 * @author Matteo Besenzoni
 * @version 1.0
 * @since 15.05.2019
 */
public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Missing args.");
            System.exit(1);
        }

        boolean gui = false;
        if (args.length >= 2) gui = args[1].equals("-v");

        // parse data
        InputData data = Parser.parseFile(args[0]);

        Window w = null;
        // create window
        if (gui) w = new Window(data);

        // initialize algorithm
        BentleyOttmann bo = new BentleyOttmann(data);

        // parse and execute instructions
        for (Instruction i : data.getInstructions()) {
            if (gui) w.update(bo.getX(), bo.getSegments(), bo.getEvents(), bo.getIntersections());
            bo.executeInstruction(i);
        }

        // show final state
        if (gui) w.update(bo.getX(), bo.getSegments(), bo.getEvents(), bo.getIntersections());

        System.exit(0);
    }
}
