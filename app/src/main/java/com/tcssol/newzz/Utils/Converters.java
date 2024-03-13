package com.tcssol.newzz.Utils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.tcssol.newzz.Model.Source;

public class Converters {
    private static final Gson gson = new Gson();

    @TypeConverter
    public static Source fromString(String value) {
        return gson.fromJson(value, Source.class);
    }

    @TypeConverter
    public static String fromSource(Source source) {
        return gson.toJson(source);
    }
}
