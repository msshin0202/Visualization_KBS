package com.cpsc410.backend.FileReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ListFiles {
    private Set<String> classSet;
    private HashMap<String, HashMap> rootHashMap;

    public ListFiles() {
        classSet = new HashSet<>();
        rootHashMap = new HashMap<>();
    }

    /*
    public static void main(String[] args) {

        String basePath = "backend/src/main/java/com/cpsc410/backend/";
        String path = new File(basePath)
                .getAbsolutePath();
        System.out.println("path: " + path);
        File folder = new File(path);

        ListFiles listFiles = new ListFiles();
        System.out.println("reading files before Java8 - Using listFiles() method");
        listFiles.listAllFiles(folder);

        for (String className: listFiles.classSet) {
            System.out.print("Class: ");
            System.out.println(className);
        }
    }
     */


    // Uses listFiles method
    public void listAllFiles(File folder){
        System.out.println("In listAllfiles(File) method");
        File[] fileNames = folder.listFiles();
        for(File file : fileNames){
            // if directory call the same method again
            if(file.isDirectory()){
                listAllFiles(file);
            }else{
                try {
                    if (file.getName().contains(".java")) {
                        // className.java
                        String classNameJava = file.getName();
                        // className
                        String className = file.getName().substring(0, classNameJava.length()-5);
                        classSet.add(className);
                    }
                    readContent(file);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
    }

    public void readContent(File file) throws IOException{
        System.out.println("read file " + file.getCanonicalPath() );
        try(BufferedReader br  = new BufferedReader(new FileReader(file))){
            String strLine;
            // Read lines from the file, returns null when end of stream
            // is reached
            int lineNumber = 0;
            while((strLine = br.readLine()) != null){
                System.out.println(lineNumber + " : " + strLine);
                lineNumber++;

                if (strLine.contains("new")) {
                    // TODO: Deal with situation when new class has been instantiated
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@");
                    System.out.println("NEW STATEMENT");
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@");
                }
            }
        }
    }

    public Set<String> getClassSet() {
        return classSet;
    }

    public HashMap<String, HashMap> getRootHashMap() {
        return rootHashMap;
    }
}
