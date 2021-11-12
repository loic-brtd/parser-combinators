package fr.combine.util;

import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static java.util.stream.Collectors.joining;

public class Util {

    public static String repr(Object o) {
        if (o == null) {
            return "null";
        } else if (o instanceof String) {
            return "\"" + escapeJavaString((String) o) + "\"";
        } else if (o instanceof List) {
            return ((List<?>) o).stream()
                                .map(Util::repr)
                                .collect(joining(", ", "[", "]"));
        } else {
            return String.valueOf(o);
        }
    }

    private static final Map<Character, String> ESCAPE_CHARS = Map.ofEntries(
            Map.entry('\\', "\\\\"),
            Map.entry('\t', "\\t"),
            Map.entry('\b', "\\b"),
            Map.entry('\n', "\\n"),
            Map.entry('\r', "\\r"),
            Map.entry('\f', "\\f"),
            Map.entry('\"', "\\\"")
    );

    public static String escapeJavaString(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            sb.append(ESCAPE_CHARS.getOrDefault(c, String.valueOf(c)));
        }
        return sb.toString();
    }

    public static String slice(String s, int start, int end) {
        if (end < start || start < 0) return "";
        end = Math.min(end, s.length());
        return s.substring(start, end);
    }

    public static String slice(String s, int start) {
        return slice(s, start, s.length());
    }

    public static boolean isEmpty(Iterable<?> iterable) {
        return !iterable.iterator().hasNext();
    }
}
