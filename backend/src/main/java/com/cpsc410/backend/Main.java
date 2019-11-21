package com.cpsc410.backend;

import com.cpsc410.backend.FileReader.ListFiles;

import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        // TODO: Locate the path to cloned repository correctly
        String basePath = "backend/src/main/java/com/cpsc410/backend/";
        String path = new File(basePath)
                .getAbsolutePath();
        System.out.println("path: " + path);
        File folder = new File(path);

        ListFiles listFiles = new ListFiles();
        listFiles.listAllFiles(folder);

        // TODO: DELETE - For testing purpose
        Set<String> classSet = listFiles.getClassSet();
        if (!classSet.isEmpty()) {
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            System.out.println("classSet has been successfully initialized");
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            for (String className: classSet) {
                System.out.print("Class in classSet: ");
                System.out.println(className);
            }
        }

        // TODO: DELETE - For testing purpose
        if (!listFiles.getRootHashMap().isEmpty()) {
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            System.out.println("classHashMap has been successfully initialized");
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            Collection<HashMap<String, Integer>> listOfHashMaps = listFiles.getRootHashMap().values();
            for (HashMap<String, Integer> hashMap: listOfHashMaps) {
                for (String classNames: hashMap.keySet()) {
                    System.out.print("Class referenced: ");
                    System.out.println(classNames);
                }
            }
        }


        listFiles.readAllFiles(folder);

        for (String className: listFiles.getClassSet()) {
            System.out.print("Class: ");
            System.out.println(className);

            HashMap<String, Integer> innerHashMap = listFiles.getRootHashMap().get(className);
            // TODO: PRINT innerHashMap
            System.out.println(className + "'s HashMap Size: " + innerHashMap.size());
        }



    }
}
