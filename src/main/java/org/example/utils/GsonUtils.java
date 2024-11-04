package org.example.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.job.info.IJobInfo;
import org.example.serialize.InterfaceAdapter;

import java.lang.reflect.Type;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GsonUtils {
    private static final Gson GSON;

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(IJobInfo.class, new InterfaceAdapter<IJobInfo>());
        GSON = builder.serializeNulls().create();
    }

    public static String ObjectToJson(Object object) {
        return GSON.toJson(object);
    }

    public static <T> T gsonToOject(String json, Type type) {
        return GSON.fromJson(json, type);
    }

    public static <T> T gsonToObject(String json, Class<T> classOfT) {
        return GSON.fromJson(json, classOfT);
    }
}
