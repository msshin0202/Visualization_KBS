package com.cpsc410.backend.FileReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ListFiles {
    public static void main(String[] args) {

        String basePath = "backend/src/main/java/com/cpsc410/backend/";
        String path = new File(basePath)
                .getAbsolutePath();
        System.out.println("path: " + path);
        File folder = new File(path);


        // File folder = new File("G:\\Test");
        ListFiles listFiles = new ListFiles();
        System.out.println("reading files before Java8 - Using listFiles() method");
        listFiles.listAllFiles(folder);

        // THIS IS USING Java8 Files.walk() method
        /*
        System.out.println("-------------------------------------------------");
        System.out.println("reading files Java8 - Using Files.walk() method");
        listFiles.listAllFiles(path);
         */

    }
    // Uses listFiles method
    public static void listAllFiles(File folder){
        System.out.println("In listAllfiles(File) method");
        File[] fileNames = folder.listFiles();
        for(File file : fileNames){
            // if directory call the same method again
            if(file.isDirectory()){
                listAllFiles(file);
            }else{
                try {
                    readContent(file);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
    }

    /*
    // Uses Files.walk method
    public static void listAllFiles(String path){
        System.out.println("In listAllfiles(String path) method");
        try(Stream<Path> paths = Files.walk(Paths.get(path))) {
            paths.forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    try {
                        readContent(filePath);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
     */

    public static void readContent(File file) throws IOException{
        System.out.println("read file " + file.getCanonicalPath() );
        try(BufferedReader br  = new BufferedReader(new FileReader(file))){
            String strLine;
            // Read lines from the file, returns null when end of stream
            // is reached
            int lineNumber = 0;
            while((strLine = br.readLine()) != null){
                System.out.println(lineNumber + " : " + strLine);
                lineNumber++;
            }
        }
    }

    /*
    public static void readContent(Path filePath) throws IOException{
        System.out.println("read file " + filePath);
        List<String> fileList = Files.readAllLines(filePath);
        System.out.println("" + fileList);
    }
     */

}
