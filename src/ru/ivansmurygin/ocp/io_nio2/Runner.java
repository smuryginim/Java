package ru.ivansmurygin.ocp.io_nio2;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by SmuryginIM on 09.04.2016.
 */
public class Runner {

    public static void main(String[] args) {
        findVisitor();
    }

    static void getSimplePath() {
        Path currentDir = Paths.get(".");//get cuurent folder
        Path resolvedPath = currentDir.resolve("pathme.txt");
        Path absolutePath = resolvedPath.normalize().toAbsolutePath();

        if (Files.notExists(absolutePath)) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(absolutePath.toFile()))) {
                bw.write("I'm the file to path me");
            } catch (IOException exception) {
                System.out.println("Something went wrong during file creation");
                exception.printStackTrace();
            }
        }

        System.out.println("Not normalized path looks like: " + resolvedPath.toAbsolutePath());
        System.out.println("Normalized path looks like: " + resolvedPath.normalize().toAbsolutePath());
        System.out.println("Get root return = " + absolutePath.getRoot());
        System.out.println("Get name0 return = " + absolutePath.getName(0));
        System.out.println("test subpath(0, 2) = " + absolutePath.subpath(0, 2));
        System.out.println("relative root to current directory = " + absolutePath.relativize(absolutePath.getRoot()));
        System.out.println("Resolve siblings to get file in the same dir = " + absolutePath.resolveSibling("pathme2.txt"));

        File file = resolvedPath.toFile(); //you can convert the Path to File and vice versa

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            System.out.println("readed line is = " + bufferedReader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("elements of the path");
        for (Path path : resolvedPath) {
            System.out.println("path element = " + path);
        }
        System.out.println();

        System.out.println("====Compare two paths====");
        System.out.println("resolvedPath.compareTo(currentDir) = " + resolvedPath.compareTo(currentDir));
    }

    static void fileMetadata() throws IOException {
        Path currentDir = Paths.get(".");//get cuurent folder
        Path resolvedPath = currentDir.resolve("pathme.txt");

        //first way to read attributes
        BasicFileAttributeView fileAttributeView = Files.getFileAttributeView(resolvedPath, BasicFileAttributeView.class);
        BasicFileAttributes posixFileAttributes = fileAttributeView.readAttributes();

        //second way to read attributes
        BasicFileAttributeView fileAttributeView1 =
                Files.getFileAttributeView(resolvedPath, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
        //Do not follow symbolic links

        //read attribute from file
        Files.getAttribute(resolvedPath, "creationTime", LinkOption.NOFOLLOW_LINKS);
        Object isDirectory = Files.getAttribute(resolvedPath, "isDirectory", LinkOption.NOFOLLOW_LINKS);

        //print some information about the file
        System.out.println(posixFileAttributes.isDirectory());
        System.out.println(Files.isDirectory(resolvedPath));
        System.out.println(isDirectory);

        //printing some additional info
        System.out.printf("File creation time %s \n", posixFileAttributes.creationTime());
        System.out.printf("File ois regular file? %s \n", posixFileAttributes.isRegularFile());
    }

    static void copyFile() {
        Path sourcePath = Paths.get(".", "pathme.txt");
        Path destinationPath = Paths.get(".", "copy", "pathme.txt");

        try {
            Files.copy(sourcePath, destinationPath);
        } catch (IOException e) {
            System.out.println("Ooops, something went wrong");
            e.printStackTrace();
            return;
        }

        System.out.println("file was successfully copied");
    }


    static void moveFile() {
        Path sourcePath = Paths.get(".", "pathme.txt");
        Path destinationPath = Paths.get(".", "copy", "pathme.txt");

        try {
            if (Files.exists(sourcePath)) {
                System.out.println("Move file to folder");
                Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                System.out.println("Move file back to root folder");
                Files.move(destinationPath, sourcePath, StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (IOException e) {
            System.out.println("Ooops, something went wrong");
            e.printStackTrace();
            return;
        }

        System.out.println("file was successfully moved");
    }

    static void deleteFile() {
        Path sourcePath = Paths.get(".", "pathme.txt");
        Path destinationPath = Paths.get(".", "copy", "pathme.txt");

        try {
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("Ooops, something went wrong during copy opertaion");
            e.printStackTrace();
            return;
        }

        System.out.println("file was successfully copied");

        try {
            Files.delete(destinationPath);
        } catch (IOException e) {
            System.out.println("Ooops, something went wrong during delete opertaion");
            e.printStackTrace();
        }

        System.out.println("file was successfuly deleted");
    }

    static void fileVisitor() {
        Path path = Paths.get(".","src", "ru", "ivansmurygin", "ocp");

        CounterVisitor visitor = new CounterVisitor();
        try {
            Files.walkFileTree(path.normalize(), new HashSet(Arrays.asList(FileVisitOption.FOLLOW_LINKS)), 5, visitor);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.printf("Directories count = %s, files count = %s", visitor.dirs, visitor.files);

    }

    static void findVisitor() {
        Path path = Paths.get(".","src", "ru", "ivansmurygin", "ocp");

        ClassFindCounterVisitor visitor = new ClassFindCounterVisitor("glob:*.{java}");
        try {
            Files.walkFileTree(path.normalize(), visitor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
