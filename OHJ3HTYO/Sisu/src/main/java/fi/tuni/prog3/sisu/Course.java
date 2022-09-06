package fi.tuni.prog3.sisu;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Course represents a course in the Sisu API. It is used to store and fetch
 * data.
 * 
 * @author Joni Koskinen
 * @author Julius Juutilainen
 */
public class Course {
    String id;
    String name;
    String code;
    int minCredits;
    int maxCredits;
    String content;
    String additional;
    String outcomes;

    /**
     * Constructor for a new Course.
     * Calls getInfo to fetch data from sisu API.
     * 
     * @param id String id to differentiate the Course object.
     */
    public Course(String id) {
        this.id = id;
        getInfo();
    }

    /**
     * @return This Courses id.
     */
    public String getId() {
        return id;
    }

    /**
     * @return This Courses name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return This Courses code.
     */
    public String getCode() {
        return code;
    }

    /**
     * @return This Courses target credits (minCredits).
     */
    public int getTargetCredits() {
        if (maxCredits != 0) {
            return maxCredits;
        }
        return minCredits;
    }

    /**
     * @return This Courses outcomes.
     */
    public String getOutcomes() {
        return outcomes;
    }

    /**
     * @return This Courses description/info.
     */
    public String getContent() {
        return content;
    }

    /**
     * @return This Courses additional content.
     */
    public String getAdditional() {
        return additional;
    }

    /**
     * Fetches JSON Data via GetJsonData and sets class variables.
     * Is used to interprent Json data and find correct objects from it.
     * Data is fetched from sisu API.
     */
    private void getInfo() {
        try {

            GetJsonData getJson_Module = new GetJsonData(4, id);
            StringBuilder sb = getJson_Module.getJsonDataFromURL();

            JsonObject obj = JsonParser.parseString(sb.toString()).getAsJsonArray().get(0).getAsJsonObject();
            this.minCredits = obj.getAsJsonObject("credits").getAsJsonPrimitive("min").getAsInt();

            if (obj.getAsJsonObject("credits").get("max").isJsonNull()) {
                this.maxCredits = minCredits;
            } else {
                this.maxCredits = obj.getAsJsonObject("credits").getAsJsonPrimitive("max").getAsInt();
            }

            if (obj.getAsJsonObject("name").getAsJsonPrimitive("fi") == null) {
                this.name = obj.getAsJsonObject("name").getAsJsonPrimitive("en").getAsString();
            }

            else {
                this.name = obj.getAsJsonObject("name").getAsJsonPrimitive("fi").getAsString();
            }
            this.code = obj.getAsJsonPrimitive("code").getAsString();
            if (!obj.get("outcomes").isJsonNull()) {
                if (obj.getAsJsonObject("outcomes").get("fi") == null) {
                    this.outcomes = obj.getAsJsonObject("outcomes").getAsJsonPrimitive("en").getAsString();
                } else {
                    this.outcomes = obj.getAsJsonObject("outcomes").getAsJsonPrimitive("fi").getAsString();
                }
            }
            if (!obj.get("content").isJsonNull()) {
                if (obj.getAsJsonObject("content").get("fi") == null) {
                    this.content = obj.getAsJsonObject("content").getAsJsonPrimitive("en").getAsString();
                } else {
                    this.content = obj.getAsJsonObject("content").getAsJsonPrimitive("fi").getAsString();
                }
            }
            if (!obj.get("additional").isJsonNull()) {
                if (obj.getAsJsonObject("additional").get("fi") == null) {
                    this.additional = obj.getAsJsonObject("additional").getAsJsonPrimitive("en").getAsString();
                } else {
                    this.additional = obj.getAsJsonObject("additional").getAsJsonPrimitive("fi").getAsString();
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }
}
