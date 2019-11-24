package com.cpsc410.backend;

import com.cpsc410.backend.FileReader.JSONCreator;
import com.cpsc410.backend.FileReader.ListFiles;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        String basePath = Paths.get(".").toAbsolutePath().normalize().toString() + "/src/main/java/com/cpsc410/backend/CPSC410";
        String path = new File(basePath)
                .getAbsolutePath();
        System.out.println("path: " + path);
        File folder = new File(path);

        ListFiles listFiles = new ListFiles();
        listFiles.listAllFiles(folder);

        Set<String> keyStrings = listFiles.getRootHashMap().keySet();
        for (String key: keyStrings) {
            System.out.print("class HashMaps in rootHashMap: ");
            System.out.println(key);
        }
        System.out.println("-----------------------------------");

        listFiles.readAllFiles(folder);

        for (String className: listFiles.getClassSet()) {
            System.out.print("Current Class: " + className + "\n");
            HashMap<String, Integer> innerHashMap = listFiles.getRootHashMap().get(className);
            if (!innerHashMap.isEmpty()) {
                System.out.println(innerHashMap);
            }
        }

        JSONCreator jsonCreator = new JSONCreator();
        jsonCreator.makeJSON(listFiles.getRootHashMap());


    }
}
