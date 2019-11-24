package com.cpsc410.backend.FileReader;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JSONCreator {

    public static JSONArray makeJSON(HashMap<String, HashMap<String, Integer>> rootHashMap){
        JSONArray rootJsonArray = new JSONArray();

        for (Map.Entry mapElement : rootHashMap.entrySet()) {
            HashMap<String, Integer> dependencyMap = (HashMap<String, Integer>) mapElement.getValue();
            JSONArray dependencyJsonArray = getDependencyJsonArray(dependencyMap);

            JSONObject classInfo = new JSONObject();
            classInfo.put("className", mapElement.getKey());
            classInfo.put("dependency", dependencyJsonArray);

            rootJsonArray.add(classInfo);
        }

        writeJSON(rootJsonArray);
        return rootJsonArray;
    }

    private static JSONArray getDependencyJsonArray(HashMap<String, Integer> map) {
        JSONArray dependencies = new JSONArray();
        for (Map.Entry mapElement : map.entrySet()) {
            JSONObject dependency = new JSONObject();
            dependency.put("className", mapElement.getKey());
            dependency.put("weight", mapElement.getValue());
            dependencies.add(dependency);
        }

        return dependencies;
    }

    private static void writeJSON(JSONArray rootJSON) {
        try (FileWriter file = new FileWriter("rootHashMap.json")) {

            file.write(rootJSON.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
