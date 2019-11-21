package com.cpsc410.backend.FileReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ScannerReadFile {

    /**
     * Read the file given the path to the file and the fileName
     * @param basePath
     * @param fileName
     * @return
     */
    public static File readFile(String basePath, String fileName) {
        String filePath = basePath + fileName;
        String path = new File(basePath)
                .getAbsolutePath();
        System.out.println("FilePath: " + path);
        return new File(path);
    }

    /**
     * Read the file line by line and perform analysis
     * @param file
     */
    public static void readLines(File file) {
        try {
            // Create a new Scanner object which will read the data
            // from the file passed in. To check if there are more
            // line to read from it we check by calling the
            // scanner.hasNextLine() method. We then read line one
            // by one till all lines is read.
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // TODO: Analyze the file
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // TODO: change the basepath according to where the repository will be saved to
        String basePath = "backend/src/main/java/com/cpsc410/backend/";
        String fileName = "Example.java";
        basePath += fileName;

        String path = new File(basePath)
                .getAbsolutePath();
        System.out.println("path: " + path);
        // Create an instance of File for data.txt file.
        File file = new File(path);
        try {
            // Create a new Scanner object which will read the data
            // from the file passed in. To check if there are more
            // line to read from it we check by calling the
            // scanner.hasNextLine() method. We then read line one
            // by one till all lines is read.
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}