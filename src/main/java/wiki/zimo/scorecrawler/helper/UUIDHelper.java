package wiki.zimo.scorecrawler.helper;

import java.util.UUID;

public class UUIDHelper {
    public static String getRandomId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
