package com.androidadvance.androidsurvey;

import android.util.SparseArray;

import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

//Singleton Answers ........

public class Answers {
    private volatile static Answers uniqueInstance;
    private final LinkedHashMap<String, Object> answered_hashmap = new LinkedHashMap<>();
    private final LinkedHashMap<String, String> answered_value_hashmap = new LinkedHashMap<>();


    private Answers() {
    }

    public void put_answer(String key, String value) {
        answered_hashmap.put(key, value);
    }

    public void put_answer(String key, SparseArray<String> value) {
        answered_hashmap.put(key, value);
    }


    public void clear_answer(String key) {
        answered_hashmap.remove(key);
    }

    public String get_answer(String key, String defVal) {
        return answered_hashmap.get(key) == null ? defVal : (String) answered_hashmap.get(key);
    }

    public SparseArray<String> get_answer(String key,  SparseArray<String> defVal) {
        return answered_hashmap.get(key) == null ? defVal : (SparseArray<String>) answered_hashmap.get(key);
    }

    public void put_value_answer(String key, String value) {
        answered_value_hashmap.put(key, value);
    }

    public String get_value_answer(String key, String defVal) {
        return answered_value_hashmap.get(key) == null ? defVal : answered_value_hashmap.get(key);
    }

    public String get_json_object() {
        Gson gson = new Gson();
        return gson.toJson(answered_hashmap, LinkedHashMap.class);
    }

    @Override
    public String toString() {
        return String.valueOf(answered_hashmap);
    }

    public static Answers getInstance() {
        if (uniqueInstance == null) {
            synchronized (Answers.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new Answers();
                }
            }
        }
        return uniqueInstance;
    }
}
