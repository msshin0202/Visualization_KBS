package com.cpsc410.backend;

import com.cpsc410.backend.FileReader.ListFiles;

import java.io.File;

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

        for (String className: listFiles.getClassSet()) {
            System.out.print("Class: ");
            System.out.println(className);
        }

    }
}
