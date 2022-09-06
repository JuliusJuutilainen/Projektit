package fi.tuni.prog3.sisu;

import java.util.HashMap;

/**
 * Prints the data structure to the command line.
 * Is used to identify problems in the data structure and to chech its validity.
 * 
 * @author Joni Koskinen
 * @author Julius Juutilainen
 */
public class PrintDegree {

    /**
     * Initializes new PrintDegree object
     * 
     * @param degreeName name of degree.
     * @param modules    HashMap of degree modules.
     */
    public PrintDegree(String degreeName, HashMap<String, DegreeModule> modules) {
        System.out.println(degreeName);
        int depth = 1;
        printAll(modules, depth);
    }

    /**
     * Crawls the data structure and prints everything based on specified depth.
     * 
     * @param modules HashMap of the data structure.
     * @param depth   depth to be crawled trough.
     */
    private void printAll(HashMap<String, DegreeModule> modules, int depth) {
        System.out.println(modules);
        for (DegreeModule m : modules.values()) {
            String space = "  ";
            System.out.println(space.repeat(depth) + m.getName());
            HashMap<String, Course> cors = m.getCourses();
            System.out.println(cors);
            for (Course c : cors.values()) {
                System.out.println(space.repeat(depth + 1) + c.getName() + " " + c.getTargetCredits());
            }
            HashMap<String, DegreeModule> mods = m.getModules();
            if (!mods.isEmpty()) {
                printAll(mods, depth);
            }
        }
    }
}
