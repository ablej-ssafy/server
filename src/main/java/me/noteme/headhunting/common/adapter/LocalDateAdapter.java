package me.noteme.headhunting.common.adapter;

import com.google.gson.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public JsonElement serialize(LocalDate src, java.lang.reflect.Type typeOfSrc, com.google.gson.JsonSerializationContext context) {
        return src == null ? null : new JsonPrimitive(src.format(formatter));
    }

    @Override
    public LocalDate deserialize(JsonElement json, java.lang.reflect.Type typeOfT, com.google.gson.JsonDeserializationContext context) throws JsonParseException {
        if (json == null || json.getAsString().isEmpty()) {
            return LocalDate.now(); // null 또는 빈 문자열일 경우 현재 날짜 반환
        }
        return LocalDate.parse(json.getAsString(), formatter);
    }
}
