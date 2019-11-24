package com.cpsc410.backend;

import com.cpsc410.backend.FileReader.JSONCreator;
import com.cpsc410.backend.FileReader.ListFiles;
import org.apache.log4j.BasicConfigurator;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.json.simple.JSONArray;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

@Path("/getDependency")
public class RestAPI {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response extractDependencyFromGitRepo(GitURL gitURL) {
        String basePath = Paths.get(".").toAbsolutePath().normalize().toString() + "/src/main/java/com/cpsc410/backend/CPSC430";
        String path = new File(basePath)
                .getAbsolutePath();
        System.out.println("path: " + path);
        File folder = new File(path);
        BasicConfigurator.configure();
        Git git;
        try {
            git = Git.cloneRepository()
                    .setURI(gitURL.url)
                    .setDirectory(folder)
                    .call();
        } catch (GitAPIException e) {
            e.printStackTrace();
            return Response.status(404).entity(" ").build();
        }

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
            for (HashMap<String, Integer> classHashMap: listOfHashMaps) {
                System.out.println(classHashMap.keySet());
                for (String classNames: classHashMap.keySet()) {
                    System.out.print("Class referenced: ");
                    System.out.println(classNames);
                }
            }
        }

        listFiles.readAllFiles(folder);

        for (String className: listFiles.getClassSet()) {
            System.out.print("Current Class: " + className + "\n");
            HashMap<String, Integer> innerHashMap = listFiles.getRootHashMap().get(className);
            if (!innerHashMap.isEmpty()) {
                System.out.println(innerHashMap);
            }
        }

        JSONCreator jsonCreator = new JSONCreator();
        JSONArray response = jsonCreator.makeJSON(listFiles.getRootHashMap());

        git.close();

        deleteDir(folder);
        return Response.status(200).entity(response.toString()).build();
    }

    public void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }
}

class GitURL{
    public String url;
}
