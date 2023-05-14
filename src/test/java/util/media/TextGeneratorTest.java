package util.media;
import org.junit.*;
import static org.junit.Assert.*;

public class TextGeneratorTest {
    @Test
    public void testInstantiatingClass() {
        // Test instantiating the class.
        TextGenerator textGenerator = new TextGenerator();
    }

    @Test
    public void testGetDurationText(){
        assertEquals("Time: 01 : 01", TextGenerator.getDurationText(1,1));
        assertEquals("Time: 01 : 10", TextGenerator.getDurationText(1,10));
        assertEquals("Time: 10 : 01", TextGenerator.getDurationText(10,1));
        assertEquals("Time: 10 : 10", TextGenerator.getDurationText(10,10));
    }
}
