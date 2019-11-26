package com.cpsc410.backend.FileReader;

//import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ListFiles {
    private Set<String> classSet;
    private HashMap<String, HashMap<String, Integer>> rootHashMap;

    public ListFiles() {
        classSet = new HashSet<>();
        rootHashMap = new HashMap<>();
    }

    // Uses listFiles method
    public void listAllFiles(File folder){
        File[] fileNames = folder.listFiles();
        for(File file : fileNames){
            // if directory, call the same method again
            if(file.isDirectory()){
                listAllFiles(file);
            }else if (file.getName().contains(".java")) {
                // Add className to HashSet
                String className = getFileName(file);
                classSet.add(className);

                // Initialize the HashMap for the current class and add it to the root HashMap
                HashMap<String, Integer> classHashMap = new HashMap<String, Integer>();
                rootHashMap.put(className, classHashMap);
            }
        }
    }

    // Uses listFiles method
    public void readAllFiles(File folder){
        File[] fileNames = folder.listFiles();
        for(File file : fileNames){
            // if directory, call the same method again
            // if file, only read the file with .java extension
            if(file.isDirectory()){
                readAllFiles(file);
            }else if (file.getName().contains(".java")){
                try {
                    readContent(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void readContent(File file) throws IOException{
        System.out.println("read file " + file.getCanonicalPath() );
        try(BufferedReader br  = new BufferedReader(new FileReader(file))) {
            String strLine;
            // Read lines from the file, returns null when end of stream is reached
            int lineNumber = 0;
            while((strLine = br.readLine()) != null){
                String[] wordsInLine = strLine.split("\\s+");
                lineNumber++;
                extractDependencies(file, strLine, wordsInLine);
            }
        }
    }

    private void extractDependencies(File file, String strLine, String[] wordsInLine) {
        if (strLine.contains("extends") || strLine.contains("implements")) {
            int index = 0;

            if (strLine.contains("extends")) {
                index = getIndexOfWord(wordsInLine, "extends");
            } else if (strLine.contains("implements")){
                index = getIndexOfWord(wordsInLine, "implements");
            }

            index++;

            while (index < wordsInLine.length) {
                String referencedClass = wordsInLine[index];

                addToHashMap(file, referencedClass);
                index++;
            }
        }

        String className = getFileName(file);

        for (String eachWord : wordsInLine) {
            if (!eachWord.isEmpty() && !eachWord.contains(" ")) {
                char currChar = eachWord.charAt(0);
                System.out.println("word: " + eachWord);
                if (Character.isUpperCase(currChar) && !className.equals(eachWord)) {
                    addToHashMap(file, eachWord);
                }
            }
        }
    }

    private void addToHashMap(File file, String referencedClass) {
        if (classSet.contains(referencedClass)) {
            String className = getFileName(file);

            HashMap<String, Integer> classHashMap = getClassHashMap(referencedClass);
            if (classHashMap.containsKey(className)) {
                int timesReferenced = classHashMap.get(className);
                timesReferenced += 1;
                classHashMap.put(className, timesReferenced);
            } else {
                classHashMap.put(className, 1);
            }
        }
    }

    private int getIndexOfWord(String[] stringArray, String keyWord) {
        return Arrays.asList(stringArray).indexOf(keyWord);
    }

    private String getFileName(File file) {
        String classNameJava = file.getName(); // className.java
        String className = file.getName().substring(0, classNameJava.length()-5); // className
        return className;
    }

    public Set<String> getClassSet() {
        return classSet;
    }

    public HashMap<String, HashMap<String, Integer>> getRootHashMap() {
        return rootHashMap;
    }

    public HashMap<String, Integer> getClassHashMap(String className) {
        if (classSet.contains(className)) {
            return rootHashMap.get(className);
        }
        return null;
    }
}
