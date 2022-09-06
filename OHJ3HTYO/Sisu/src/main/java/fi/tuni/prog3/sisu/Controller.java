package fi.tuni.prog3.sisu;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;

/**
 * Controls the main UI-window and its elements. Is responsible for program
 * functionality.
 * Controls mainGUI.fxml.
 * 
 * @author Joni Koskinen
 * @author Julius Juutilainen
 */
public class Controller implements Initializable {

    private static HashMap<String, Degree> degrees;
    private static HashMap<String, Course> allCourses;
    private static HashMap<String, DegreeModule> allModules;
    private static Student student;
    private static Degree degree;
    private static String selectedItemName;

    @FXML
    private TreeView<String> mainView = new TreeView<>();
    @FXML
    private WebView courseInfo = new WebView();
    @FXML
    private Text studentName = new Text();
    @FXML
    private Text studentNumber = new Text();
    @FXML
    private Text studentCredits = new Text();
    @FXML
    private Button saveButton = new Button();
    @FXML
    private Button loadButton = new Button();
    @FXML
    private Button switchButton = new Button();
    @FXML
    private ComboBox<String> searchBar = new ComboBox<>();
    @FXML
    private VBox completedCourses = new VBox();
    @FXML
    private VBox selectableCourseList = new VBox();
    @FXML
    private CheckBox courseCheckBox = new CheckBox();
    @FXML
    private Text allCredits = new Text();
    @FXML
    private WebView motivation = new WebView();

    /**
     * Initializes the UI and performs necessary actions.
     * 
     * @param arg0 unused
     * @param arg1 unused
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        degree = degrees.get(student.getDegree());

        allCourses = new HashMap<>();
        allModules = new HashMap<>();

        TreeItem<String> rootItem = GUITools.initializeTree(degree);

        GUITools.setUpDegreeBox(searchBar, degrees);
        // selectableCourseList = GUITools.initializeCheckList(degrees);

        mainView.setRoot(rootItem);

        studentNumber.setText(student.getNumber());
        studentName.setText(student.getName());
        studentCredits.setText(student.getCreditsAsString());

        motivation.getEngine().load(GUITools.getMotivationalImageUrl());
        refreshStudiesCompleted();

        courseInfo.getEngine().setUserStyleSheetLocation(getClass().getResource("/webViewStyleSheet.css").toString());

    }

    /**
     * Shows info based on selected element and toggles the visibility of "course
     * completed" checkbox.
     * Checks if clicked element is a course and sets checkbox visibility by it.
     * NOTE: Not every element has info that can be fetched.
     */
    @FXML
    public void selectItem() {
        TreeItem<String> item = mainView.getSelectionModel().getSelectedItem();

        if (item != null) {
            String courseHeader = item.getValue();
            String[] splitString = courseHeader.split(" ");
            String[] courseName = splitString;
            if (splitString.length > 1 && courseHeader.endsWith("op")) {
                courseName = Arrays.copyOf(splitString, splitString.length - 1);
            }

            StringBuilder sb = new StringBuilder();

            for (String s : courseName) {
                sb.append(s).append(" ");
            }
            sb.setLength(sb.length() - 1);
            String name = sb.toString();

            setSelectedElement(name);

            Course c = searchCourse(name);
            StringBuilder infoData = new StringBuilder();
            courseInfo.getEngine().loadContent("");

            if (c != null) {
                infoData.append("<p><h2>").append(c.getName()).append("</h2></p>");
                if (c.getContent() != null) {
                    infoData.append("<p><h3>Sisältö: </h3></p>").append(c.getContent());
                }
                if (c.getOutcomes() != null) {
                    infoData.append("<p><h3>Osaamistavoitteet: </h3></p>").append(c.getOutcomes());
                }
                if (c.getAdditional() != null) {
                    infoData.append("<p><h3>Lisätiedot: </h3></p>").append(c.getAdditional());
                }
            }

            DegreeModule m = searchModule(name);
            if (m != null) {
                infoData.append("<p><h2>").append(m.getName()).append("</h2></p>");
                if (m.getContentDescription() != null) {
                    infoData.append("<p><h3>Sisältö: </h3></p>").append(m.getContentDescription());
                }
                if (m.getOutcomes() != null) {
                    infoData.append("<p><h3>Osaamistavoitteet: </h3></p>").append(m.getOutcomes());
                }
            }

            if (name.equals(degree.getName())) {
                infoData.append("<p><h2>").append(degree.getName()).append("</h2></p>");
                if (degree.getOutcomes() != null) {
                    infoData.append(degree.getOutcomes());
                }
            }
            courseInfo.getEngine().loadContent(infoData.toString());
            // Checkboxin nollaus jos dataa ei tulekkaan
            courseCheckBox.setSelected(false);

            if (allCourses.containsKey(Controller.selectedItemName)) {
                courseCheckBox.setVisible(true);
                if (student.getCoursesDone().get(Controller.selectedItemName) == null
                        || student.getCoursesDone() == null) {
                    student.addCoursesDone(Controller.selectedItemName, courseCheckBox.isSelected());
                }
                courseCheckBox.setSelected(student.getCoursesDone().get(Controller.selectedItemName));
            } else {
                courseCheckBox.setVisible(false);
            }
        }
    }

    /**
     * "Completes" course
     * Gives the Student-object a course key and sets visible credits to TreeView
     * TreeItem.
     * Lastly refreshes the completed studies part of the UI view.
     */
    @FXML
    public void checkBoxOnClick() {

        if (Controller.selectedItemName != null) {

            student.addCoursesDone(Controller.selectedItemName, courseCheckBox.isSelected());

            TreeItem<String> item = mainView.getSelectionModel().getSelectedItem();
            if (item != null) {
                Course c = searchCourse(GUITools.combineString(GUITools.splitString(item.getValue())).toString());

                if (courseCheckBox.isSelected()) {
                    student.addCredits(c.getTargetCredits());
                    addCreditsToTree(item, c.getTargetCredits(), true);
                } else {
                    student.subtractCredits(c.getTargetCredits());
                    addCreditsToTree(item, c.getTargetCredits(), false);
                }
                // ? tarpeellinen
                courseCheckBox.setSelected(student.getCoursesDone().get(Controller.selectedItemName));
                refreshStudiesCompleted();
            }
        }
    }

    /**
     * Refreshes the comleted studies part of the UI.
     * Adds completed courses to a VBOX and adds credits to the allCredits Text
     * element.
     */
    @FXML
    public void refreshStudiesCompleted() {
        completedCourses.getChildren().clear();

        for (String key : student.getCoursesDone().keySet()) {
            if (student.getCoursesDone().get(key)) {
                // Muutos maxCreditistä getTargetCreditsiin
                String credits = String.valueOf(allCourses.get(key).getTargetCredits());

                StringBuilder sb = new StringBuilder();
                sb.append(key).append(" ").append(credits).append("op");

                String course = sb.toString();

                Text completedCourse = new Text(course);
                completedCourse.setStyle("-fx-fill: #ffffff");
                completedCourses.getChildren().add(completedCourse);
            }
        }
        allCredits.setText(String.format("%d" + "op", student.getCredits()));
        studentCredits.setText(student.getCreditsAsString());

    }

    /**
     * Saves current Student by using methods from SaveProgress.
     * Catches IOException if given file is not valid.
     */
    @FXML
    public void save() {
        System.out.println("Saving...");
        try {
            SaveProgress.saveStudent(student);

        } catch (IOException ex) {
            System.out.println("Couldn't save progress!");
        }
    }

    /**
     * Loads Student object from file, replaces the current Student.
     * Catches FileNotFoundException if given file doesn't exist.
     * Lastly refreshes the completed studies part of the UI view.
     */
    @FXML
    public void load() {
        System.out.println("Loading...");
        try {
            Student studentPlaceholder = SaveProgress.loadStudent();
            if (studentPlaceholder != null) {
                Controller.student = studentPlaceholder;
                GUITools.setStudent(student);
                studentNumber.setText(student.getNumber());
                studentName.setText(student.getName());
                TreeItem<String> rootItem = GUITools.initializeTree(degrees.get(student.getDegree()));
                degree = degrees.get(student.getDegree());
                courseInfo.getEngine().loadContent("");
                searchBar.setValue("");
                courseCheckBox.setVisible(false);
                mainView.setRoot(rootItem);
            }

        } catch (FileNotFoundException e2) {
            System.out.println("File not found :(");
        }

        refreshStudiesCompleted();
    }

    /**
     * Switches the current degree shown in UI and sets it for the current Student.
     * NOTE: Switching degrees will reset the Students credits and courses done.
     * Lastly refreshes the completed studies part of the UI view.
     * 
     */
    // Precondition: Given degree must be found in the database
    // Postcondition: sets up new degree in the tree and updates student
    @FXML
    public void switchDegree() {
        if (!searchBar.getEditor().getText().isEmpty() && degrees.containsKey(searchBar.getEditor().getText())) {
            Degree degree = degrees.get(searchBar.getEditor().getText());
            Controller.degree = degree;
            courseInfo.getEngine().loadContent("");
            String s = degree.getName();
            student.setDegree(s);
            TreeItem<String> rootItem = GUITools.initializeTree(degree);
            mainView.setRoot(rootItem);
        }
        refreshStudiesCompleted();

    }

    /**
     * Refreshs the motivational image shown on the tab "motivaatio".
     * Image is AI generated by https://inspirobot.me/.
     */
    @FXML
    public void getNewMotivationalImage() {
        motivation.getEngine().load(GUITools.getMotivationalImageUrl());
    }

    /**
     * @param degrees HashMap containing String keys and Degree degrees to set.
     */

    public static void setDegrees(HashMap<String, Degree> degrees) {
        Controller.degrees = degrees;
    }

    /**
     * @param student Student-object to be set onto the Controller.
     */
    public static void setStudent(Student student) {
        Controller.student = student;
    }

    /**
     * @param degree Degree-object to be set onto the Controller.
     */
    public static void setDegree(Degree degree) {
        Controller.degree = degree;
    }

    /**
     * @param itemName String element to be set onto the Controller.
     */
    public static void setSelectedElement(String itemName) {
        Controller.selectedItemName = itemName;
    }

    /**
     * @param c Course to be added to the allCourses HashMap
     */
    public static void addCourses(Course c) {
        allCourses.put(c.getName(), c);
    }

    /**
     * @param m DegreeModule to be added to the allModules HashMap.
     */
    public static void addModules(DegreeModule m) {
        allModules.put(m.getName(), m);
    }

    /**
     * Getter method for unit tests
     * 
     * @return all courses under the degree
     */
    public static HashMap<String, Course> getCourses() {
        return allCourses;
    }

    /**
     * Getter method for unit tests
     * 
     * @return all modules under the degree
     */
    public static HashMap<String, DegreeModule> getModules() {
        return allModules;
    }

    /**
     * Clears the allCourses and allModules HashMaps.
     */
    public static void clearMaps() {
        if (allCourses != null) {
            allCourses.clear();
        }
        if (allModules != null) {
            allModules.clear();
        }

    }

    /**
     * @param name A String containing the search key.
     * @return Course The Course object corresponding with the search key.
     */
    public static Course searchCourse(String name) {
        if (allCourses.containsKey(name)) {
            return allCourses.get(name);
        } else {
            return null;
        }
    }

    /**
     * @param name A String containing the search key.
     * @return DegreeModule The DegreeModule object corresponding with the search
     *         key.
     */
    public static DegreeModule searchModule(String name) {
        if (allModules.containsKey(name)) {
            return allModules.get(name);
        } else {
            return null;
        }
    }

    /**
     * Closes the program.
     */
    @FXML
    public void closeProgram() {
        Platform.exit();
        System.exit(0);
    }

    /**
     * Shows credits in the TreeView in the format of "n/m op" where m is the max
     * amount of credits
     * and n is the current amount of credits earned by the student.
     * 
     * This method is unique to Controller and is not made to be used in other
     * contexts.
     * 
     * @param item         TreeItem to add the functionality to
     * @param credits      int, courses credits
     * @param coursePassed boolean, determines whether the user has completed the
     *                     course
     */
    public static void addCreditsToTree(TreeItem<String> item, int credits, boolean coursePassed) {
        if (item.getParent().getParent() != null) {
            addCreditsToTree(item.getParent(), credits, coursePassed);
            if (searchModule(item.getParent().getValue()) == null) {
                String value = item.getParent().getValue();
                String[] splitValue = value.split(" ");
                int length = splitValue.length;
                String last = splitValue[length - 1];
                String pointsSplit = last.split("/")[0];
                String prevPoints = pointsSplit.substring(0, pointsSplit.length() - 2);
                int prevPointsNumb = Integer.parseInt(prevPoints);
                String[] name = Arrays.copyOf(splitValue, length - 1);
                StringBuilder sb = GUITools.combineString(name);
                if (coursePassed) {
                    sb.append(" ").append(credits + prevPointsNumb).append("op/")
                            .append(last.split("/")[1]);
                    item.getParent().setValue(sb.toString());
                } else {
                    DegreeModule m = searchModule(sb.toString());
                    sb.append(" ").append(prevPointsNumb - credits).append("op/")
                            .append(m.getTargetCredits()).append("op");
                    item.getParent().setValue(sb.toString());

                }
            }
        }
    }

}
