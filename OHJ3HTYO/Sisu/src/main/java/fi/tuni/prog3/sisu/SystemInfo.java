package fi.tuni.prog3.sisu;

/**
 * Is used to return current java and javafx versions.
 */
public class SystemInfo {

    /**
     * @return current java version as String.
     */
    public static String javaVersion() {
        return System.getProperty("java.version");
    }

    /**
     * @return current javafx version as string.
     */
    public static String javafxVersion() {
        return System.getProperty("javafx.version");
    }

}