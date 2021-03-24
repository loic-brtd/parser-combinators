package org.lobertrand.parser;

import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public final class Utils {

    static String slice(String s, int start, int end) {
        if (end < start || start < 0) return "";
        end = Math.min(end, s.length());
        return s.substring(start, end);
    }

    static String slice(String s, int start) {
        return slice(s, start, s.length());
    }

    static <T> T log(T anything) {
        System.out.println("log: " + anything);
        return anything;
    }

    static <T> T log(String label, T anything) {
        System.out.println(label + ": " + anything);
        return anything;
    }

    static String escapeString(String s) {
        var table = Map.ofEntries(
                entry('\\', "\\\\"),
                entry('\t', "\\t"),
                entry('\b', "\\b"),
                entry('\n', "\\n"),
                entry('\r', "\\r"),
                entry('\f', "\\f"),
                entry('\"', "\\\"")
        );
        var sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            sb.append(table.getOrDefault(c, Character.toString(c)));
        }
        return sb.toString();
    }

    static boolean hasLessThanTwoElements(Iterable<?> iterable) {
        var it = iterable.iterator();
        if (it.hasNext()) {
            it.next();
            return !it.hasNext();
        }
        return true;
    }
}
