package me.noteme.headhunting.common.utils;

import java.util.UUID;

public class KeyUtils {
    public static String generateKey() {
        return UUID.randomUUID()
                .toString()
                .replaceAll("-", "")
                .substring(0, 15);
    }
}
