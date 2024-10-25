package me.noteme.headhunting.common.cache;

public class CacheKey {
    public static String verifyKey(String key){
        return "email:verify:" + key;
    }
}
