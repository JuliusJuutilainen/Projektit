package fi.tuni.prog3.sisu;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

/**
 * A set of GUI tools to add functionality to the GUI.
 * Contains unused functions, useful for futher developement.
 * 
 * @author Joni Koskinen
 * @author Julius Juutilainen
 */
public class GUITools {

    private static Student student;

    public GUITools() {
    }

    /**
     * Gets image from filepath.
     * 
     * @param filename path to file.
     * @return Image.
     * @throws FileNotFoundException if file is not found.
     */
    public static Image getImage(String filename) throws FileNotFoundException {

        InputStream stream = new FileInputStream(filename);
        Image image = new Image(stream);

        return image;
    }

    /**
     * Gets image from filepath as Node
     * 
     * @param filename path to file.
     * @return Node containing image.
     * @throws FileNotFoundException if file is not found.
     */
    public static Node getImageAsNode(String filename) throws FileNotFoundException {

        Image image = getImage(filename);
        ImageView imageNode = new ImageView();
        imageNode.setImage(image);

        return imageNode;
    }

    /**
     * Gets new motivational image from https://inspirobot.me/.
     * Inspirobot uses AI to generate motivational images.
     * 
     * @return URL of the new image.
     */
    public static String getMotivationalImageUrl() {
        String imageURL = new String();
        try {
            URLConnection connection = new URL("https://inspirobot.me/api?generate=true").openConnection();
            InputStream input = connection.getInputStream();
            System.out.println(connection.getContentType());

            imageURL = new BufferedReader(new InputStreamReader(input)).readLine();
        } catch (IOException e) {
            System.out.println("Not found!");
        }
        return imageURL;
    }

    /**
     * Initialized the main GUI Degree TreeView.
     * 
     * @param deg chosen Degree to be used in the initialization.
     * @return TreeItem<String> visual representation of Degree contents shown in
     *         UI.
     */
    public static TreeItem<String> initializeTree(Degree deg) {
        TreeItem<String> rootItem = new TreeItem<>(deg.getName());
        Controller.clearMaps();
        printTree(deg.getModules(), rootItem);
        return rootItem;
    }

    /**
     * Prints the treeview containing courses, modules and credits.
     * 
     * @param modules HashMap contains all the modules under the degree/module
     * @param root    TreeItem that functions as the base for the tree
     */
    private static void printTree(HashMap<String, DegreeModule> modules, TreeItem<String> root) {
        TreeItem<String> moduleItem;
        TreeItem<String> courseItem;

        // Go through all modules
        for (DegreeModule m : modules.values()) {
            if (m.getType().equals("GroupingModule")) {
                moduleItem = new TreeItem<>(m.getName());
            } else {
                moduleItem = new TreeItem<>(m.getName() + " 0op/" + m.getTargetCredits() + "op");
            }

            Controller.addModules(m);
            root.getChildren().add(moduleItem);
            HashMap<String, Course> cors = m.getCourses();
            // Go through courses under the module ( if there are any )
            for (Course c : cors.values()) {
                Controller.addCourses(c);
                courseItem = new TreeItem<>(c.getName() + " " + c.getTargetCredits() + "op");
                moduleItem.getChildren().add(courseItem);
                if (student.getCoursesDone().containsKey(c.getName()) && student.getCoursesDone().get(c.getName())) {
                    Controller.addCreditsToTree(courseItem, c.getTargetCredits(), true);
                }
            }
            HashMap<String, DegreeModule> mods = m.getModules();
            if (!mods.isEmpty()) {
                printTree(mods, moduleItem);
            }
        }
    }

    /**
     * Sets up the degree search box. When key is typed, automatically searches
     * degrees that match typed chars.
     * 
     * @param cb      ComboBox that is being set up
     * @param degrees HashMap containing all the degrees
     */
    public static void setUpDegreeBox(ComboBox<String> cb, HashMap<String, Degree> degrees) {
        cb.setEditable(true);
        cb.setPromptText("Hae tutkinnon nimellä...");
        cb.getEditor().setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent k) {
                String s = cb.getEditor().getText();
                cb.getItems().clear();

                compare(s);

            }

            private void compare(String s) {
                ArrayList<String> names = new ArrayList<>();
                for (Degree d : degrees.values()) {
                    if (d.getName().length() >= s.length()
                            && d.getName().substring(0, s.length()).compareToIgnoreCase(s) == 0) {
                        names.add(d.getName());
                    }
                }
                cb.getItems().addAll(names);
            }
        });
    }

    /**
     * Splits given string by space
     * 
     * @param string String to be split
     * @return returns name as an array
     */
    public static String[] splitString(String string) {
        String[] splitValue = string.split(" ");
        int length = splitValue.length;
        String[] name = Arrays.copyOf(splitValue, length - 1);
        return name;
    }

    /**
     * Combines given array to string with spaces
     * 
     * @param nameArray Array to be combined
     * @return returns stringbuilder element with the name
     */
    public static StringBuilder combineString(String[] nameArray) {
        StringBuilder sb = new StringBuilder();
        for (String s : nameArray) {
            sb.append(s).append(" ");
        }
        sb.setLength(sb.length() - 1);
        return sb;
    }

    /**
     * Adds the student object to GUITools
     * 
     * @param student Student object to be added
     */
    public static void setStudent(Student student) {
        GUITools.student = student;
    }
}
