package me.noteme.headhunting.common.cache;

public class CacheKey {
    public static String confirmKey(String key){
        return "email:verify:" + key;
    }

    public static String blackListKey(String key) {
        return "blackList:" + key;
    }

    public static String searchKey() {
        return "searchKeywords";
    }

    public static String searchUserKey(Long userId) {
        return "searchKeywords:user:" + userId;
    }
}
