package eg.edu.alexu.alexandriauniversity.dataHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing content for user interfaces.
 */
public class OptionsOfList {

    /**
     * An array of Application Options items.
     */
    public static List<OptionsItemsList> ITEMS = new ArrayList<>();

    /**
     * A map of Application Options items, by ID.
     */
    public static Map<String, OptionsItemsList> ITEM_MAP = new HashMap<>();

    static {
        // Add Application Options items.
        addItem(new OptionsItemsList("1", "About"));
        addItem(new OptionsItemsList("2", "NEWS and EVENTS"));
//        addItem(new OptionsItemsList("3", "NEWS"));
//        addItem(new OptionsItemsList("4", "EVENTS"));
        addItem(new OptionsItemsList("5", "SOCIAL"));
        addItem(new OptionsItemsList("6", "CONTACT AU"));
        addItem(new OptionsItemsList("7", "LOCATION"));

    }

    private static void addItem(OptionsItemsList item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * Application Options items representing a piece of content.
     */
    public static class OptionsItemsList {
        public String id;
        public String content;

        public OptionsItemsList(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
