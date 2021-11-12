package fr.combine.util;

import fr.combine.tuple.Tuple;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Map.entry;
import static java.util.stream.Collectors.joining;

public class Util {

    public static String repr(Object o) {
        if (o == null) {
            return "null";
        } else if (o instanceof String) {
            return "\"" + escapeString((String) o) + "\"";
        } else if (o instanceof List) {
            return ((List<?>) o).stream()
                                .map(Util::repr)
                                .collect(joining(", ", "[", "]"));
        } else {
            return String.valueOf(o);
        }
    }

    private static String escapeJavaString(String s) {
        return s.replace("\\", "\\\\")
                .replace("\t", "\\t")
                .replace("\b", "\\b")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\f", "\\f")
                .replace("\"", "\\\"");
    }

    public static Tuple dynamicTuple(List<?> elements) {
        Objects.requireNonNull(elements);
        switch (elements.size()) {
            case 1:
                return Tuple.of(elements.get(0));
            case 2:
                return Tuple.of(elements.get(0), elements.get(1));
            case 3:
                return Tuple.of(elements.get(0), elements.get(1), elements.get(2));
            case 4:
                return Tuple.of(elements.get(0), elements.get(1), elements.get(2), elements.get(3));
            case 5:
                return Tuple.of(elements.get(0), elements.get(1), elements.get(2), elements.get(3), elements.get(4));
            default:
                throw new IllegalArgumentException("Wrong number of elements (should be between 1 and 5)");
        }
    }

    public static String slice(String s, int start, int end) {
        if (end < start || start < 0) return "";
        end = Math.min(end, s.length());
        return s.substring(start, end);
    }

    public static String slice(String s, int start) {
        return slice(s, start, s.length());
    }

    public static String escapeString(String s) {
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

    public static boolean hasLessThanTwoElements(Iterable<?> iterable) {
        var it = iterable.iterator();
        if (it.hasNext()) {
            it.next();
            return !it.hasNext();
        }
        return true;
    }
}
