package me.noteme.headhunting.common.utils;

public class HangulUtils {
    private static final char[] CHOSUNG = {
            'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ',
            'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };

    public static String getChosung(String text) {
        StringBuilder chosung = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (c >= 0xAC00 && c <= 0xD7A3) {
                int cho = (c - 0xAC00) / (21 * 28);
                chosung.append(CHOSUNG[cho]);
            } else {
                chosung.append(c);
            }
        }
        return chosung.toString();
    }

    public static boolean matchSearch(String text, String query) {
        String lowercaseText = text.toLowerCase();
        String lowercaseQuery = query.toLowerCase();
        String textChosung = getChosung(text);

        if (lowercaseText.contains(lowercaseQuery)) return true;
        if (textChosung.contains(lowercaseQuery)) return true;
        return textChosung.startsWith(lowercaseQuery);
    }
}
