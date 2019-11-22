package com.cpsc410.backend.FileReader;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JSONCreator {

    public static void makeJSON(HashMap<String, HashMap<String, Integer>> rootHashMap){
        JSONObject rootJSON = new JSONObject();
        Iterator hashMapIterator = rootHashMap.entrySet().iterator();
        while(hashMapIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry) hashMapIterator.next();
            String key = ((String) mapElement.getKey());
            HashMap<String, Integer> valueMap = ((HashMap<String, Integer>) mapElement.getValue());
            rootJSON.put(key, valueMap);
        }
//        rootHashMap.put("testkey", "testval");

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(rootHashMap);
        writeJSON(jsonArray);

    }

    private static void writeJSON(JSONArray jsonArray) {
        try (FileWriter file = new FileWriter("rootHashMap.json")) {
            file.write(jsonArray.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
