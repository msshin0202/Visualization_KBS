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
            }
        }
    }

    // Uses listFiles method
    public void readAllFiles(File folder){
        System.out.println("In listAllfiles(File) method");
        File[] fileNames = folder.listFiles();
        for(File file : fileNames){
            // if directory, call the same method again
            // if file, only read the file with .java extension
            if(file.isDirectory()){
                listAllFiles(file);
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
        try(BufferedReader br  = new BufferedReader(new FileReader(file))){
            String strLine;
            // Read lines from the file, returns null when end of stream is reached
            int lineNumber = 0;
            while((strLine = br.readLine()) != null){
                System.out.println(lineNumber + " : " + strLine);
                lineNumber++;

                if (strLine.contains("new")) {
                    // TODO: Deal with situation when new class has been instantiated
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@");
                    System.out.println("NEW STATEMENT");
                    System.out.println("@@@@@@@@@@@@@@@@@@@@@");
                    String[] wordsInLine = strLine.split(" ");

                    // Find a class name that is referenced (Found by "new" keyword)
                    int indexOfNew = getIndexOfWord(wordsInLine, "new");
                    String referencedClass = wordsInLine[indexOfNew + 1];

                    // Remove the brackets at the end of class object instantiation
                    // e.g. new Player();
                    int indexOfBracket = getIndexOfChar(referencedClass, '(');
                    System.out.println("Index of Bracket: " + indexOfBracket);
                    // referencedClass = referencedClass.substring(0, indexOfBracket - 1);

                    // Go into HashMap of the class referenced and add current class as key
                    if (classSet.contains(referencedClass)) {
                        // TODO: Go into HashMap of the class referenced and add current class as key
                        String className = getFileName(file);

                        // TODO: Got to handle multiple instances of same class instantiation
                        getClassHashMap(referencedClass).put(className, 1);
                    }

                    // TODO: DELETE
                    for (String word: wordsInLine) {
                        System.out.print("word in Line: ");
                        System.out.println(word);
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
