package me.noteme.headhunting.common.cache;

public class CacheKey {
    public static String confirmKey(String key){
        return "email:verify:" + key;
    }

    public static String authenticationKey(String key) {
        return "authentication:" + key;
    }

    public static String searchKey() {
        return "searchKeywords";
    }

    public static String searchUserKey(Long userId) {
        return "searchKeywords:user:" + userId;
    }
}
