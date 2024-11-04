package org.example.serialize;

import com.google.gson.*;
import org.example.job.info.JobInfoType;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Objects;

public class InterfaceAdapter<T> implements JsonSerializer, JsonDeserializer {
    private static final String TYPE = "type";
    private static final String CLASSNAME = "CLASS";
    private static final HashMap<String, Class> classes = new HashMap<>(32);


    static {
        for (JobInfoType type : JobInfoType.values()) {
            if (!Objects.isNull(type.className) && !Objects.isNull(type.name)) {
                try {
                    Class cls = Class.forName(type.className);
                    addClass(type.name, cls);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    public static void addClass(String type, Class cls) {
        classes.put(type, cls);
    }

    public static void addClass(Class cls) {
        classes.put(cls.getSimpleName(), cls);
    }

    @Override
    public JsonElement serialize(Object jsonElement, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject data = (JsonObject) jsonSerializationContext.serialize(jsonElement);
        if (data.get(TYPE) == null || data.get(TYPE).isJsonNull()) {
            data.addProperty(TYPE, jsonElement.getClass().getSimpleName());
        }
        if (!classes.containsKey(data.get(TYPE).getAsString())) {
            data.addProperty(CLASSNAME, jsonElement.getClass().getName());
        }
        return data;
    }

    @Override
    public Object deserialize(JsonElement jsonElement, Type typeOfT,
                              JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Class cls;
        if (jsonObject.get(TYPE) != null && !jsonObject.get(TYPE).isJsonNull()
                && classes.containsKey(jsonObject.get(TYPE).getAsString())) {
            cls = classes.get(jsonObject.get(TYPE).getAsString());
        } else {
            String className = jsonObject.get(CLASSNAME).getAsString();
            cls = getObjectClass(className);
        }
        return jsonDeserializationContext.deserialize(jsonObject, cls);
    }

    public Class getObjectClass(String className) throws JsonParseException {
        try {
            return Class.forName(className);
        } catch (Exception e) {
            throw new JsonParseException(e);
        }

    }
}
