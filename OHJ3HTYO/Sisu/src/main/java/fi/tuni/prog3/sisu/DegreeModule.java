package fi.tuni.prog3.sisu;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 
 * DegreeModule represents a module in the Sisu API. It is used to store and
 * fetch
 * data.
 * 
 */
public class DegreeModule {
    String name;
    String id;
    int targetCredits;
    String outcomes;
    String type;
    String contentDescription;
    HashMap<String, DegreeModule> modules;
    HashMap<String, Course> courses;

    /**
     * Constructor for a new degreeModule
     * 
     * @param id is the id of the new degreeModule. Is used to differentiate.
     */
    public DegreeModule(String id) {
        this.id = id;
        modules = new HashMap<>();
        courses = new HashMap<>();
        decodeJson();
    }

    /**
     * @return this degreeModules name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return this degreeModules credits.
     */
    public int getTargetCredits() {
        return targetCredits;
    }

    /**
     * @return degreeModules outcomes.
     */
    public String getOutcomes() {
        return outcomes;
    }

    /**
     * @return degreeModules content description
     */
    public String getContentDescription() {
        return contentDescription;
    }

    /**
     * @return HashMap<String, DegreeModule> Returns hashmap containing
     *         child modules.
     */
    public HashMap<String, DegreeModule> getModules() {
        return modules;
    }

    /**
     * @return this degreeModules HashMap<String, Course> containing this modules
     *         courses.
     */
    public HashMap<String, Course> getCourses() {
        return courses;
    }

    /**
     * @return this degreeModules type.
     */
    public String getType() {
        return type;
    }

    /**
     * Extracts information from the json text by iterating through it
     * Extracts also modules and courses under it and calls compositeRule to
     * create them
     */
    private void decodeJson() {
        try {
            StringBuilder sb = new StringBuilder();
            JsonObject obj;
            if (id.startsWith("otm")) {
                GetJsonData getJson_Module = new GetJsonData(2, id);
                sb = getJson_Module.getJsonDataFromURL();
                obj = JsonParser.parseString(sb.toString()).getAsJsonObject();
            } else {
                GetJsonData getJson_Module = new GetJsonData(3, id);
                sb = getJson_Module.getJsonDataFromURL();
                obj = JsonParser.parseString(sb.toString()).getAsJsonArray().get(0).getAsJsonObject();
            }

            if (obj.getAsJsonObject("name").getAsJsonPrimitive("fi") == null) {
                this.name = obj.getAsJsonObject("name").getAsJsonPrimitive("en").getAsString();
            } else {
                this.name = obj.getAsJsonObject("name").getAsJsonPrimitive("fi").getAsString();
            }

            this.type = obj.get("type").getAsString();

            switch (obj.getAsJsonPrimitive("type").getAsString()) {
                case "GroupingModule":
                    break;
                case "StudyModule":
                    if (!obj.get("targetCredits").isJsonNull()) {
                        this.targetCredits = obj.getAsJsonObject("targetCredits").getAsJsonPrimitive("min").getAsInt();
                    }
                    if (!obj.get("outcomes").isJsonNull()) {
                        if (obj.getAsJsonObject("outcomes").getAsJsonPrimitive("fi") == null) {
                            this.outcomes = obj.getAsJsonObject("outcomes").getAsJsonPrimitive("en").getAsString();
                        } else {
                            this.outcomes = obj.getAsJsonObject("outcomes").getAsJsonPrimitive("fi").getAsString();
                        }
                    }
                    if (!obj.get("contentDescription").isJsonNull()) {
                        if (obj.getAsJsonObject("contentDescription").getAsJsonPrimitive("fi") == null) {
                            this.contentDescription = obj.getAsJsonObject("contentDescription").getAsJsonPrimitive("en")
                                    .getAsString();
                        } else {
                            this.contentDescription = obj.getAsJsonObject("contentDescription").getAsJsonPrimitive("fi")
                                    .getAsString();
                        }
                    }

                    break;
            }

            // Rulet läpi
            JsonArray arr = null;
            String rule = obj.getAsJsonObject("rule").get("type").getAsString();
            if (rule.equals("CreditsRule")) {
                arr = obj.getAsJsonObject("rule").getAsJsonObject("rule").getAsJsonArray("rules");
            } else if (rule.equals("CompositeRule")) {
                arr = obj.getAsJsonObject("rule").getAsJsonArray("rules");
            }
            compositeRule(arr);
        } catch (MalformedURLException e) {
        } catch (IOException e2) {
        }
    }

    /**
     * Extracts rules from jsonArray and creates module/course objects
     * 
     * @param arr JsonArray containing the rules
     */
    private void compositeRule(JsonArray arr) {
        Iterator<JsonElement> it = arr.iterator();
        while (it.hasNext()) {
            JsonObject jObject = it.next().getAsJsonObject();
            String type = jObject.get("type").getAsString();
            switch (type) {
                case "CompositeRule":
                    compositeRule(jObject.getAsJsonArray("rules"));
                    break;
                case "ModuleRule":
                    String moduleGroupId = jObject.get("moduleGroupId").getAsString();
                    DegreeModule m = new DegreeModule(moduleGroupId);
                    modules.put(m.getName(), m);
                    break;
                case "CourseUnitRule":
                    String courseId = jObject.get("courseUnitGroupId").getAsString();
                    Course c = new Course(courseId);
                    courses.put(c.getName(), c);
                    break;
                default:
                    break;
            }
        }

    }
}
