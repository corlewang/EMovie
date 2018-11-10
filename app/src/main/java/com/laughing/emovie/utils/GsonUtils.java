package com.laughing.emovie.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Wy on 2018/11/9.
 */

public class GsonUtils {


    private static Gson gson = new Gson();

    private GsonUtils() {
    }

    ;

    public static <T> T fromJsonType(String json, Type type) {

        return gson.fromJson(json, type);
    }

    public static <T> List<T> fromJsonList(String json, Type type) {

        List<T> mlist = gson.fromJson(json, type);
        return mlist;
    }

    public static <T> T fromJson(String json, Class<T> t) {


        return gson.fromJson(json, t);

    }

    public static <T> String toJson(List<T> listData, Type type) {
        return gson.toJson(listData, type);
    }
}

