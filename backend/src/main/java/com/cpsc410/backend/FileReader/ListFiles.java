package com.cpsc410.backend.FileReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ListFiles {
    private Set<String> classSet;
    private HashMap<String, HashMap<String, Integer>> rootHashMap;

    public ListFiles() {
        classSet = new HashSet<>();
        rootHashMap = new HashMap<>();
    }

    // Uses listFiles method
    public void listAllFiles(File folder){
        System.out.println("In listAllfiles(File) method");
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
                HashMap<String, Integer> classHashMap = new HashMap<>();
                rootHashMap.put(className, classHashMap);

                // TODO: DELETE - For testing purpose
                Set<String> keyStrings = rootHashMap.keySet();
                for (String key: keyStrings) {
                    System.out.print("class HashMaps in rootHashMap: ");
                    System.out.println(key);
                }

            }
        }
    }

    // Uses listFiles method
    public void readAllFiles(File folder){
        System.out.println("In readAllfiles(File) method");
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
                System.out.println(lineNumber + " : " + strLine);
                lineNumber++;

                // TODO: Deal with situation when new class has been instantiated
                if (strLine.contains("new")) {

                    // TODO: DELETE - For testing purpose
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@");
                    System.out.println("NEW STATEMENT");
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@");

                    // TODO: Must consider the case of {, }, (, ), <, >, space
                    // e.g. try (BufferedReader br  = new BufferedReader(new FileReader(file))){
                    String[] wordsInLine = strLine.split(" ");

                    // TODO: DELETE - For testing purpose
                    for (String word: wordsInLine) {
                        System.out.print("word in Line: ");
                        System.out.println(word);
                    }

                    // Find a class name that is referenced (Found by "new" keyword)
                    int indexOfNew = getIndexOfWord(wordsInLine, "new");
                    String referencedClass = wordsInLine[indexOfNew + 1];

                    // Remove the brackets at the end of class object instantiation
                    // e.g. new Player();
                    if (containsChar(referencedClass, '(') && containsChar(referencedClass, ')')) {
                        int indexOfBracket = getIndexOfChar(referencedClass, '(');
                        referencedClass = referencedClass.substring(0, indexOfBracket);
                    }

                    // TODO: DELETE - For testing purpose
                    System.out.println("Referenced ClassName: " + referencedClass);

                    // If the referenced class is User defined class,
                    // Go into HashMap of the referenced class, add current class as key
                    if (classSet.contains(referencedClass)) {
                        // TODO: Go into HashMap of the class referenced and add current class as key
                        String className = getFileName(file);

                        // TODO: DELETE - For testing purpose
                        System.out.println("Current ClassName: " + className);

                        // TODO: Got to handle multiple instances of same class instantiation
                        HashMap<String, Integer> classHashMap = getClassHashMap(referencedClass);
                        classHashMap.put(className, 1);

                        // TODO: DELETE - For testing purpose
                        System.out.println(className + " has been inserted into " + referencedClass + "'s HashMap");
                        // getClassHashMap(referencedClass).put(className, 1);
                    }
                    // TODO: DELETE - For testing purpose
                    else {
                        System.out.println("-----Referenced Class NOT user defined class-----");
                    }

                }
            }
        }
    }

    private int getIndexOfWord(String[] stringArray, String keyWord) {
        return Arrays.asList(stringArray).indexOf(keyWord);
    }

    private int getIndexOfChar(String word, char keyChar) {
        char[] chars = word.toCharArray();
        int index = -3;
        for (int i = 0; i < chars.length; i++) {
            if (keyChar == chars[i])
                index = i;
        }
        return index;
    }

    private boolean containsChar(String word, char keyChar) {
        char[] chars = word.toCharArray();
        int index = -3;
        for (char aChar : chars) {
            if (keyChar == aChar)
                return true;
        }
        return false;
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
