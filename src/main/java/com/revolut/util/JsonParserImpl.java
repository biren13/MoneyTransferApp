package com.revolut.util;

import com.google.gson.Gson;

public class JsonParserImpl  implements  JsonParser{

    @Override
    public <T> T toPojoFromJson(String jsonString, Class<T> classType) {
        return new Gson().fromJson(jsonString, classType);
    }

    @Override
    public String toJsonFromPojo(Object data) {
        return new Gson().toJson(data);
    }
}
