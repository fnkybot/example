
package core;

import java.util.ArrayList;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Test class for model class BiorythmMath (Calculations are for TODAY DATE)
 * @author Wojciech-Debicki
 * @version 1.0
 */
public class BiorhythmMathTest {
    
    private BiorhythmMath biorhythmMath;
    

    /**
     * Test of getDaysBetween method, of class BiorhythmMath.
     * @param day
     * @param month
     * @param year
     * @param expected
     */
    @ParameterizedTest
    @CsvSource({"16,04,2002,7956", "1,1,2008,5870", "1,12,2023,57"})
    public void testGetDaysBetween(String day, String month, String year, int expected) {
        ArrayList<String> exampleDate = new ArrayList<String>() {{ 
            add(day); //day
            add(month); //month
            add(year); //year
            } }; 
        biorhythmMath = new BiorhythmMath(exampleDate);
        assertEquals(expected, biorhythmMath.calculateDaysBetween(), 10, "Life days should equals 7907.");

    }

    /**
     * Test of getPhysical method, of class BiorhythmMath.
     * @param day
     * @param month
     * @param year
     * @param expected
     */
    @ParameterizedTest
    @CsvSource({"16,04,2002,-0.52", "1,1,2008,0.98", "1,12,2023,0.14"})
    public void testGetPhysical(String day, String month, String year, double expected) {
        ArrayList<String> exampleDate = new ArrayList<String>() {{ 
            add(day); //day
            add(month); //month
            add(year); //year
            } }; 
        biorhythmMath = new BiorhythmMath(exampleDate);
        assertEquals(expected, biorhythmMath.calculatePhysical(), 0.1, "Get physical should equals .");
    }

    /**
     * Test of getEmotional method, of class BiorhythmMath.
     * @param day
     * @param month
     * @param year
     * @param expected
     */
    @ParameterizedTest
    @CsvSource({"16,04,2002,0.78", "1,1,2008,-0.78", "1,12,2023,0.22"})
    public void testGetEmotional(String day, String month, String year, double expected) {
        ArrayList<String> exampleDate = new ArrayList<String>() {{ 
            add(day); //day
            add(month); //month
            add(year); //year
            } }; 
        biorhythmMath = new BiorhythmMath(exampleDate);
        assertEquals(expected, biorhythmMath.getEmotional(), 0.1, "Get emotional should equals .");
    }

    /**
     * Test of getIntellectual method, of class BiorhythmMath.
     * @param day
     * @param month
     * @param year
     * @param expected
     */
    @ParameterizedTest
    @CsvSource({"16,04,2002,0.54", "1,1,2008,-0.69", "1,12,2023,-0.99"})
    public void testGetIntellectual(String day, String month, String year, double expected) {
        ArrayList<String> exampleDate = new ArrayList<String>() {{ 
            add(day); //day
            add(month); //month
            add(year); //year
            } }; 
        biorhythmMath = new BiorhythmMath(exampleDate);
        assertEquals(expected, biorhythmMath.getIntellectual(), 0.1, "Get intellectual should equals .");
    }
    
}
