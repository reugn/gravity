package reug.gravity;

import org.junit.Test;
import reug.gravity.extractor.TextExtractor;
import reug.gravity.extractor.TikaTextExtractor;
import reug.gravity.matcher.BMHMatcher;
import reug.gravity.matcher.ContainsMatcher;
import reug.gravity.matcher.Matcher;
import reug.gravity.matcher.multi.ConcurrentMultiMatcher;
import reug.gravity.matcher.multi.MultiMatcher;
import reug.gravity.model.Pattern;
import reug.gravity.util.Patterns;
import reug.gravity.util.Utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FunctionalityTest {

    @Test
    public void readAndContainsMatcherTxtFile() throws Exception {
        InputStream is = Utils.readResource("/apache-license-2.0.txt");
        Pattern pattern = new Pattern("within third-party archives", 1);

        Matcher m = new ContainsMatcher();
        int res = m.match(pattern, is, 2);
        assertEquals(1, res);
    }

    @Test
    public void readAndBMHMatcherTxtFile() throws Exception {
        InputStream is = Utils.readResource("/apache-license-2.0.txt");
        Pattern pattern = new Pattern("within third-party archives", 1);

        Matcher m = new BMHMatcher();
        int res = m.match(pattern, is, 2);
        assertEquals(1, res);
    }

    @Test
    public void multiMatchTxtFile() throws Exception {
        InputStream is = Utils.readResource("/apache-license-2.0.txt");
        List<Pattern> patterns = new ArrayList<>();
        patterns.add(new Pattern("within third-party archives", 10, 5));
        patterns.add(new Pattern("modified files", 5, 5));
        patterns.add(new Pattern("class name", 2, 5));

        MultiMatcher m = new ConcurrentMultiMatcher(ContainsMatcher::new, 32);
        int res = m.match(patterns, is).get().get();
        assertEquals(17, res);
    }

    @Test
    public void multiMatchImgFile() throws Exception {
        InputStream is = Utils.readResource("/ascii.jpg");
        List<Pattern> patterns = new ArrayList<>();
        patterns.add(new Pattern("backspace", 10, 1, String::toLowerCase));
        patterns.add(new Pattern("vertical tab", 5, 1, String::toLowerCase));
        patterns.add(new Pattern("context info", 2, 1, String::toLowerCase));
        TextExtractor ext = new TikaTextExtractor();
        String extracted = ext.extract(is);

        MultiMatcher m = new ConcurrentMultiMatcher(ContainsMatcher::new, 32);
        int res = m.match(patterns, extracted).get().get();
        assertEquals(15, res);
    }

    @Test
    public void multiMatchFromCSV() throws Exception {
        InputStream is = Utils.readResource("/apache-license-2.0.txt");
        InputStream patterns_is = Utils.readResource("/patterns.csv");
        List<Pattern> patterns = Patterns.fromCSV(patterns_is);

        MultiMatcher m = new ConcurrentMultiMatcher(ContainsMatcher::new, 32);
        int res = m.match(patterns, is).get().get();
        assertEquals(18, res);
    }
}
