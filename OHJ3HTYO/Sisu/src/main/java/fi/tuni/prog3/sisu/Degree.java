package fi.tuni.prog3.sisu;

import java.net.MalformedURLException;
import java.io.IOException;
import com.google.gson.*;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Degree represents a degree in the Sisu API. It is used to store and fetch
 * data.
 * 
 * @author Joni Koskinen
 * @author Julius Juutilainen
 */
public class Degree {
    private String id;
    private String code;
    private String lang;
    private String groupId;
    private String name;
    private int credits;
    private String outcomes;
    HashMap<String, DegreeModule> modules;

    /**
     * Constructor for a new Degree.
     * 
     * @param id      Degree id, can be used to differentiate.
     * @param code    Degree code, can be used to differentiate.
     * @param lang    Degree lang, default is finnish.
     * @param groupId Degree groupId.
     * @param name    Degree name, can be used to differentiate.
     * @param credits Degree credits.
     */
    public Degree(String id, String code, String lang, String groupId, String name, int credits) {
        this.id = id;
        this.code = code;
        this.lang = lang;
        this.groupId = groupId;
        this.name = name;
        this.credits = credits;
        modules = new HashMap<>();

    }

    /**
     * @return this degrees id.
     */
    public String getId() {
        return id;
    }

    /**
     * @return this degrees code.
     */
    public String getCode() {
        return code;
    }

    /**
     * @return this degrees language.
     */
    public String getLang() {
        return lang;
    }

    /**
     * @return this degrees group id.
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * @return this degrees name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return this degrees credits.
     */
    public int getCredits() {
        return credits;
    }

    /**
     * @return this degrees outcomes.
     */
    public String getOutcomes() {
        return outcomes;
    }

    /**
     * @return This Degrees DegreeModule Keys, DegreeModules in a hashmap.
     */
    public HashMap<String, DegreeModule> getModules() {
        decodeJson();
        return modules;
    }

    /**
     * Extracts information from the json text by iterating through it
     * Extracts also modules and courses under it and calls compositeRule to
     * create them
     */
    public void decodeJson() {
        try {
            GetJsonData getJson_Module = new GetJsonData(2, id);
            StringBuilder sb = getJson_Module.getJsonDataFromURL();
            JsonObject obj = JsonParser.parseString(sb.toString()).getAsJsonObject();

            // Peruskamat otetaan talteen
            if (!obj.get("learningOutcomes").isJsonNull()) {
                if (obj.getAsJsonObject("learningOutcomes").get("fi") == null) {
                    this.outcomes = obj.getAsJsonObject("learningOutcomes").getAsJsonPrimitive("en").getAsString();
                } else {
                    this.outcomes = obj.getAsJsonObject("learningOutcomes").getAsJsonPrimitive("fi").getAsString();
                }
            }

            // Rulet käydää läpi
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
     * @param arr, JsonArray containing the rules
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
                default:
                    break;
            }
        }
    }
}
