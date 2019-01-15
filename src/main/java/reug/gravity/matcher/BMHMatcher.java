package reug.gravity.matcher;

import java.util.HashMap;
import java.util.Map;

public class BMHMatcher extends BaseMatcher {

    private static class SkipTable {
        private Map<Character, Integer> skip_table = new HashMap<>();
        private String pattern;
        private int len;

        SkipTable(String pattern) {
            this.pattern = pattern;
            len = pattern.length();
            init();
        }

        int getShift(Character c) {
            return skip_table.getOrDefault(c, len);
        }

        private void init() {
            char[] arr = pattern.toCharArray();
            for (int i = len - 2; i >= 0; i--) {
                if (!skip_table.containsKey(arr[i])) {
                    skip_table.put(arr[i], (len - i) - 1);
                }
            }
        }
    }

    @Override
    public int doMatch(String pattern, String target) {
        SkipTable st = new SkipTable(pattern);
        int i = pattern.length() - 1;
        int len = i;
        int rt = NOT_FOUND;
        while (i < target.length() - 1) {
            for (int j = len; j >= 0; j--) {
                int t_char = i - (len - j);
                if (pattern.charAt(j) == target.charAt(t_char)) {
                    if (j == 0) {
                        i += pattern.length();
                        rt++;
                    }
                } else {
                    i += st.getShift(target.charAt(t_char));
                    break;
                }
            }
        }
        return rt;
    }
}
