package fi.tuni.prog3.sisu;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

/**
 * Controls the start and registration windows. Responsible for GUI
 * functionality.
 * Controls startupGUI.fxml and registerGUI.fxml.
 * 
 * @author Joni Koskinen
 * @author Julius Juutilainen
 */
public class LoginController implements Initializable {

    private static HashMap<String, Degree> degrees;
    private Stage stage = new Stage();

    @FXML
    private TextField name = new TextField();
    @FXML
    private TextField studentNumber = new TextField();
    @FXML
    private ComboBox<String> degreePicker = new ComboBox<>();
    @FXML
    private Text errorMessage = new Text();

    /**
     * Initializes the UI and performs necessary actions.
     * 
     * @param arg0 unused.
     * @param arg1 unused.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        GUITools.setUpDegreeBox(degreePicker, degrees);
        errorMessage.setVisible(false);
    }

    /**
     * Loads student data from file.
     * Uses method from SaveProgress class.
     * 
     * @throws FileNotFoundException if file is not found.
     */
    @FXML
    public void loadStudentData() {

        try {
            Student studentPlaceholder = SaveProgress.loadStudent();
            if (studentPlaceholder != null) {
                Student student = studentPlaceholder;
                Controller.setStudent(student);
                GUITools.setStudent(student);
                Parent parent = loadFXMLsettings("/mainGUI.fxml");
                Scene scene = new Scene(parent);
                stage.setScene(scene);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the registration window.
     */
    @FXML
    public void openRegister() {

        Parent parent = loadFXMLsettings("/registerGUI.fxml");

        Scene scene = new Scene(parent);
        stage.setScene(scene);

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
     * Registers new student and opens the main window.
     * Initialized new Student object and passes it on to main Controller.
     */
    // Precondition: Given name can't be empty
    // Precondition: Given number can't be empty
    // Precondition: Given degree must be found in the database
    // Postcondition: sets up the program main view
    @FXML
    void registerStudent() {

        errorMessage.setVisible(false);
        String newName = name.getText();
        String newStudentNumber = studentNumber.getText();
        String newDegree = getPickedDegree();
        if (newDegree != null && !"".equals(newStudentNumber) && !"".equals(newName)) {
            
            if(newStudentNumber.length() >= 15){
                errorMessage.setText("Liian pitkä opiskelijanumero!");
                errorMessage.setVisible(true);
            }
            else if(newName.length() >= 15){
                errorMessage.setText("Liian pitkä nimi!");
                errorMessage.setVisible(true);
            }
            else{
                Student student = new Student(newName, newStudentNumber);
                student.setDegree(newDegree);

                Controller.setStudent(student);
                GUITools.setStudent(student);

                Parent parent = loadFXMLsettings("/mainGUI.fxml");
                Scene scene = new Scene(parent);

                stage.setScene(scene);
                stage.show();
            }
            
        }
    }

    /**
     * Fetches the degree name from degree searchbox(ComboBox)
     * Checks also that the name isn't empty and degrees hashmap contains it.
     * 
     * @return String Returns null or String depending on whether degree was
     *         found
     */
    @FXML
    public String getPickedDegree() {
        String s = null;
        if (!degreePicker.getEditor().getText().isEmpty() && degrees.containsKey(degreePicker.getEditor().getText())) {
            Degree degree = degrees.get(degreePicker.getEditor().getText());
            s = degree.getName();
        }
        return s;
    }

    /**
     * Loads a FXML-file and sets it to new Parent.
     * 
     * @param fileName path to file.
     * @return Parent containin the FXML formatting.
     */
    public Parent loadFXMLsettings(String fileName) {

        Parent mainWindow = new Parent() {
        };
        try {
            mainWindow = FXMLLoader.load(this.getClass().getResource(fileName));
            System.out.print("UI-tiedosto löytyi!\n");

        } catch (IOException e3) {
            System.out.print("VIRHE TIEDOSTON KÄSITTELYSSÄ! " + e3.getCause() + "/n");
        } catch (NullPointerException e4) {
            System.out.print("UI-tiedostoa ei löydy!\n");
        }

        return mainWindow;
    }

    /**
     * Sets the stage for the controller. Is used to get the sisu.java stage for the
     * controller to use.
     * 
     * @param newStage stage to be set as the controller stage.
     */
    public void setStage(Stage newStage) {
        stage = newStage;
    }

    /**
     * Sets the HashMap of degrees for the controller.
     * 
     * @param degrees HashMap containing Degree-objects.
     */
    public static void setDegrees(HashMap<String, Degree> degrees) {
        LoginController.degrees = degrees;
    }

}
