package com.github.reugn.gravity;

import com.github.reugn.gravity.extractor.TextExtractor;
import com.github.reugn.gravity.extractor.TikaTextExtractor;
import com.github.reugn.gravity.matcher.BMHMatcher;
import com.github.reugn.gravity.matcher.ContainsMatcher;
import com.github.reugn.gravity.matcher.LevenshteinMatcher;
import com.github.reugn.gravity.matcher.Matcher;
import com.github.reugn.gravity.matcher.multi.CasualMatcher;
import com.github.reugn.gravity.matcher.multi.ConcurrentMultiMatcher;
import com.github.reugn.gravity.matcher.multi.SpecifiedMatcher;
import com.github.reugn.gravity.matcher.multi.TrieMatcher;
import com.github.reugn.gravity.model.Pattern;
import com.github.reugn.gravity.util.Filters;
import com.github.reugn.gravity.util.Patterns;
import com.github.reugn.gravity.util.Utils;
import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FunctionalityTest {

    @Test
    public void ContainsMatcherTxtFile() throws Exception {
        InputStream is = Utils.readResource("/apache-license-2.0.txt");
        Pattern pattern = new Pattern("within third-party archives", 1);

        Matcher m = new ContainsMatcher();
        int res = m.match(pattern, is, 2);
        assertEquals(1, res);
    }

    @Test
    public void BMHMatcherTxtFile() throws Exception {
        InputStream is = Utils.readResource("/apache-license-2.0.txt");
        Pattern pattern = new Pattern("within third-party archives", 1);

        Matcher m = new BMHMatcher();
        int res = m.match(pattern, is, 2);
        assertEquals(1, res);
    }

    @Test
    public void LevenshteinMatcherTxtFile() throws Exception {
        InputStream is = Utils.readResource("/apache-license-2.0.txt");
        Pattern pattern = new Pattern("file", 1);

        Matcher m = new LevenshteinMatcher(1);
        int res = m.match(pattern, is, 2);
        assertEquals(12, res);
    }

    @Test
    public void multiMatchTxtFile() throws Exception {
        InputStream is = Utils.readResource("/apache-license-2.0.txt");
        List<Pattern> patterns = new ArrayList<>();
        patterns.add(new Pattern("within third-party archives", 10, 5));
        patterns.add(new Pattern("modified files", 5, 5));
        patterns.add(new Pattern("class name", 2, 5));

        CasualMatcher m = new ConcurrentMultiMatcher(ContainsMatcher::new, 32);
        int res = m.match(patterns, is).get().get();
        assertEquals(17, res);
    }

    @Test
    public void multiMatchImgFile() throws Exception {
        InputStream is = Utils.readResource("/ascii.jpg");
        List<Pattern> patterns = new ArrayList<>();
        patterns.add(new Pattern("backspace", 10, 1, String::toLowerCase));
        patterns.add(new Pattern("horizontal tab", 5, 1, String::toLowerCase));
        patterns.add(new Pattern("context info", 2, 1, String::toLowerCase));
        TextExtractor ext = new TikaTextExtractor();
        String extracted = ext.extract(is);

        CasualMatcher m = new ConcurrentMultiMatcher(ContainsMatcher::new, 32);
        int res = m.match(patterns, extracted).get().get();
        assertEquals(15, res);
    }

    @Test
    public void multiMatchFromCSV() throws Exception {
        InputStream is = Utils.readResource("/apache-license-2.0.txt");
        InputStream patterns_is = Utils.readResource("/patterns.csv");
        List<Pattern> patterns = Patterns.fromCSV(patterns_is, Filters::specialChars);

        CasualMatcher cm = new ConcurrentMultiMatcher(ContainsMatcher::new, 32);
        int res = cm.match(patterns, is).get().get();
        assertEquals(18, res);

        SpecifiedMatcher sm = new TrieMatcher(patterns, 3);
        is = Utils.readResource("/apache-license-2.0.txt");
        int res2 = sm.match(is).get().get();
        assertEquals(18, res2);
    }
}
