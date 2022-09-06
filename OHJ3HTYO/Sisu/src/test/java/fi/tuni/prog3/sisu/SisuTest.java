
package fi.tuni.prog3.sisu;

import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;

import javafx.stage.Stage;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Is used to test the program. Contains unit tests.
 */
public class SisuTest extends ApplicationTest{

    @Override
    public void start(Stage stage){
        System.out.println("Testiviesti");
        new sisu().start(stage);
    }
    
    /**
     * Checks student objects get-methods
     */
    @Test
    public void testStudent(){
        Student s = new Student("William", "12004321");
        s.setDegree("Tietotekniikka");
        String expNameResult = "William";
        String nameResult = s.getName();
        String expNumberResult = "12004321";
        String numberResult = s.getNumber();
        String expDegreeResult = "Tietotekniikka";
        String degreeResult = s.getDegree();
        
        assertEquals(expNameResult, nameResult);
        assertEquals(expNumberResult, numberResult);
        assertEquals(expDegreeResult, degreeResult);
    }
    /**
     * Checks degree objects get-methods
     */
    @Test
    public void testDegree(){
        Degree deg = new Degree("otm-d729cfc3-97ad-467f-86b7-b6729c496c82", "TSTK", "fi", "otm-fa02a1e7-4fe1-43e3-818b-810d8e723531","Tieto- ja sähkötekniikan kandidaattiohjelma", 5);
        String expId = "otm-d729cfc3-97ad-467f-86b7-b6729c496c82";
        String id = deg.getId();
        String expCode = "TSTK";
        String code = deg.getCode();
        String expLang = "fi";
        String lang = deg.getLang();
        String expGroupId = "otm-fa02a1e7-4fe1-43e3-818b-810d8e723531";
        String groupId = deg.getGroupId();
        String expName = "Tieto- ja sähkötekniikan kandidaattiohjelma";
        String name = deg.getName();
        int expCredit = 5;
        int credit = deg.getCredits();
        assertEquals(expId, id);
        assertEquals(expCode, code);
        assertEquals(expLang, lang);
        assertEquals(expGroupId, groupId);
        assertEquals(expName, name);
        assertEquals(expCredit, credit);
        
        deg.modules.put("moduuli1", new DegreeModule("otm-6c36cb36-1507-44ff-baab-a30ac76ca786"));
        deg.modules.put("moduuli2", new DegreeModule("otm-35d5a7e1-71c1-456a-8783-9cf8c34262f5"));
        HashMap<String, DegreeModule> modules = deg.getModules();
        assertTrue(modules.containsKey("moduuli1"));
        assertTrue(modules.containsKey("moduuli2"));
    }
    /**
     * Checks module objects get-methods
     */
    @Test
    public void testModule(){
        DegreeModule m = new DegreeModule("otm-3858f1d8-4bf9-4769-b419-3fee1260d7ff");
        String expName = "Tietojenkäsittelytieteiden tutkinto-ohjelman yhteiset opinnot";
        String name = m.getName();
        int expCredits = 35;
        int credits = m.getTargetCredits();
        String expType = "StudyModule";
        String type = m.getType();
        assertEquals(expName, name);
        assertEquals(expCredits, credits);
        assertEquals(expType, type);
    }
    /**
     * Checks course objects get-methods
     */
    @Test
    public void testCourse(){
        Course c = new Course("uta-ykoodi-47926");
        String expName = "Johdatus analyysiin";
        String name = c.getName();
        String expCode = "MATH.MA.110";
        String code = c.getCode();
        int expCredits = 5;
        int credits  = c.getTargetCredits();
        assertEquals(expName, name);
        assertEquals(expCode, code);
        assertEquals(expCredits, credits);
    }
    
    /*@Test
    public void testController(){
        Controller.addCourses(new Course("uta-ykoodi-47926"));
        String expCourseName = "Johdatus analyysiin"; 
        assertTrue(Controller.getCourses().containsKey(expCourseName));
        assertNotEquals(Controller.searchCourse(expCourseName), null);
        Controller.addModules(new DegreeModule("otm-1d25ee85-df98-4c03-b4ff-6cad7b09618b"));
        String expModuleName = "Tietojenkäsittelytieteiden kandidaattiohjelma";
        assertTrue(Controller.getModules().containsKey(expModuleName));
        assertNotEquals(Controller.searchModule(expModuleName), null);
    }*/
    
    
    /**
     * Tests Login UI buttons
     * ! Does not work
     */
    @Test
    public void testButtons(FxRobot robot, Stage stage){
        FxAssert.verifyThat("#regButt", LabeledMatchers.hasText("Rekisteröi"));
        FxAssert.verifyThat("#loadButt", LabeledMatchers.hasText("Lataa"));
    }
    
    
    
    
    
    
}
