
package fi.tuni.prog3.sisu;

import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
//import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Main class and the starting point of the application.
 * 
 * @author Joni Koskinen
 * @author Julius Juutilainen
 */
public class sisu extends Application {

    public HashMap<String, Degree> degrees = new HashMap<>();

    /**
     * Starts the program.
     * 
     * @param stage main stage.
     */

    @Override
    public void start(Stage stage) {

        try {

            // Get the data from the API
            GetJsonData getJson_Degree = new GetJsonData(1, "");
            StringBuilder sb = getJson_Degree.getJsonDataFromURL();
            decodeJson(sb);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }

        try {
            Image icon = GUITools.getImage("sisuTrans.PNG");
            stage.getIcons().add(icon);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Parent parent = new Parent() {
        };
        FXMLLoader loader = new FXMLLoader();

        loader = new FXMLLoader(getClass().getResource("/startupGUI.fxml"));

        try {
            loader = new FXMLLoader(getClass().getResource("/startupGUI.fxml"));
            parent = loader.load();
        } catch (IOException e3) {
            e3.printStackTrace();
        }

        LoginController controller = loader.getController();
        controller.setStage(stage);

        Scene scene = new Scene(parent);
        stage.setScene(scene);

        stage.setTitle("Sisu");
        stage.show();

    }

    /**
     * Calls the launch method.
     * 
     * @param args[] unused
     */
    public static void main(String args[]) {

        launch();
    }

    /**
     * Extracts information from the json text by iterating through it and
     * creates degree objects from said information
     * 
     * @param sb Json-text in a StringBuilder
     */
    private void decodeJson(StringBuilder sb) {
        JsonObject obj = JsonParser.parseString(sb.toString()).getAsJsonObject();
        JsonArray arr = obj.getAsJsonArray("searchResults");
        Iterator<JsonElement> it = arr.iterator();
        while (it.hasNext()) {
            JsonObject jObject = it.next().getAsJsonObject();
            JsonElement id = jObject.get("id");
            JsonElement code = jObject.get("code");
            JsonElement lang = jObject.get("lang");
            JsonElement groupId = jObject.get("groupId");
            JsonElement name = jObject.get("name");
            JsonElement minCredit = jObject.get("credits").getAsJsonObject().get("min");
            Degree deg = new Degree(id.getAsString(), code.getAsString(),
                    lang.getAsString(), groupId.getAsString(), name.getAsString(), minCredit.getAsInt());
            degrees.put(name.getAsString(), deg);
        }

        Controller.setDegrees(degrees);
        LoginController.setDegrees(degrees);

    }
}
