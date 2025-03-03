package com.alonsoaliaga.nookurestaffapi.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonUtils {
    static boolean NEW_PARSE_METHOD = false;
    static {
        try{
            JsonParser.class.getMethod("parse", String.class);
            NEW_PARSE_METHOD = true;
        }catch (Throwable e) {}
    }
    public static JsonObject parseObject(String string) {
        if (NEW_PARSE_METHOD) {
            return JsonParser.parseString(string).getAsJsonObject();
        }else{
            return new JsonParser().parse(string).getAsJsonObject();
        }
    }
}