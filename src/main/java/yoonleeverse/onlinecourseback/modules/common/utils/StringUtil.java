package yoonleeverse.onlinecourseback.modules.common.utils;

import java.util.regex.Pattern;

public class StringUtil {
    private static final Pattern NOT_USED = Pattern.compile("[^a-zA-Z가-힣0-9-]+");
    private static final Pattern MULTIPLE_DASH = Pattern.compile("(^-|-$)");

    public static String toSlug(String input) {
        String slug = NOT_USED.matcher(input).replaceAll("-");
        slug = MULTIPLE_DASH.matcher(slug).replaceAll("");
        return slug.toLowerCase();
    }
}
