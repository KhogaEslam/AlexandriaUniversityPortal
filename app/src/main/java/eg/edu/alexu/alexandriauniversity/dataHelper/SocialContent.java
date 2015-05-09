package eg.edu.alexu.alexandriauniversity.dataHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces
 * <p/>
 * TO DO: Replace all uses of this class before publishing your app.
 */
public class SocialContent {

    public static List<SocialList> ITEMS = new ArrayList<>();

    public static Map<String, SocialList> ITEM_MAP = new HashMap<>();

    static {
        // Add 3 sample items.
        addItem(new SocialList("1", "Facebook"));
        addItem(new SocialList("2", "Twitter"));
        addItem(new SocialList("3", "LinkedIn"));
        addItem(new SocialList("4", "Google+"));
        addItem(new SocialList("5", "Youtube"));
    }

    private static void addItem(SocialList item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class SocialList {
        public String id;
        public String content;

        public SocialList(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
