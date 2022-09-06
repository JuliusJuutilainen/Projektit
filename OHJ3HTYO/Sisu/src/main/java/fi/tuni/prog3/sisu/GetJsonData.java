package fi.tuni.prog3.sisu;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.IOException;

/**
 * Is used to fetch JSON-data from the Sisu API.
 * 
 * @author Joni Koskinen
 * @author Julius Juutilainen
 */
public class GetJsonData {

    private int urlId;
    private String moduleGroupId;

    /**
     * Constructor for GetJsonData. Saves urlId and moduleGroupId
     * 
     * @param urlId         Id used to select correct url format
     * @param moduleGroupId Id used to select correct Degree
     */
    public GetJsonData(int urlId, String moduleGroupId) {
        if (urlId > 4 || urlId < 1) {
            throw new IllegalArgumentException("Illegal urlID " + urlId + "! \n");
        } else {
            this.urlId = urlId;
        }
        this.moduleGroupId = moduleGroupId;
    }

    /**
     * Opens up a connection to the given url and requests the jsondata from there.
     * 
     * @return StringBuilder Returns the fetched jsondata in a stringbuilder
     * @throws MalformedURLException throws exception when url is not correct form
     * @throws IOException           throws exception when data is not correct form
     */
    public StringBuilder getJsonDataFromURL() throws MalformedURLException, IOException {
        URL urlOne = new URL("https://sis-tuni.funidata.fi/kori/api/module-search?"
                + "curriculumPeriodId=uta-lvv-2021&universityId=tuni-university"
                + "-root-id&moduleType=DegreeProgramme&limit=1000");
        URL urlTwo = new URL(String.format("https://sis-tuni.funidata.fi/kori/api/modules/" + moduleGroupId));
        URL urlThree = new URL(String.format("https://sis-tuni.funidata.fi/kori/api/modules/by-group-id?groupId="
                + moduleGroupId + "&universityId=tuni-university-root-id"));
        URL urlFour = new URL(String.format("https://sis-tuni.funidata.fi/kori/api/course-units/by-group-id?groupId="
                + moduleGroupId + "&universityId=tuni-university-root-id"));

        HttpURLConnection c = null;
        if (urlId == 1) {
            c = (HttpURLConnection) urlOne.openConnection();
        }
        if (urlId == 2) {
            c = (HttpURLConnection) urlTwo.openConnection();
        }
        if (urlId == 3) {
            c = (HttpURLConnection) urlThree.openConnection();
        }
        if (urlId == 4) {
            c = (HttpURLConnection) urlFour.openConnection();
        }

        String line;
        StringBuilder sb = new StringBuilder();
        c.setRequestMethod("GET");
        c.setConnectTimeout(5000);
        c.setReadTimeout(5000);
        if (c.getResponseCode() < 300) {
            BufferedReader read = new BufferedReader(new InputStreamReader(c.getInputStream(), "UTF-8"));
            while ((line = read.readLine()) != null) {
                sb.append(line);
            }
            read.close();
        } else {
            System.out.println("Not works :(");
        }
        c.disconnect();

        return sb;
    }
}
