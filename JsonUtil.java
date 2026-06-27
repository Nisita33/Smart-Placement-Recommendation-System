/**
 * Tiny helper so servlets can build JSON by hand without needing
 * an external library (org.json, Gson, etc). Good enough for this
 * project's simple, predictable response shapes.
 */
public class JsonUtil {

    public static String escape(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                     .replace("\"", "\\\"")
                     .replace("\n", "\\n")
                     .replace("\r", "");
    }

    public static String quote(String value) {
        return "\"" + escape(value) + "\"";
    }
}
