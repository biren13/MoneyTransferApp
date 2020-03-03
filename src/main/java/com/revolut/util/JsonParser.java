package com.revolut.util;

public interface JsonParser {
    <T> T toPojoFromJson(String jsonString, Class<T> classType);
    String toJsonFromPojo(Object data);
}
