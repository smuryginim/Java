package ru.ivansmurygin.ocp.io_nio2;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by SmuryginIM on 09.04.2016.
 */
public class ClassFindCounterVisitor extends SimpleFileVisitor<Path> {
    private PathMatcher matcher;
    private int filesFound;

    public ClassFindCounterVisitor(String pattern) {

        try {
            matcher = FileSystems.getDefault().getPathMatcher(pattern);
        } catch (Exception e) {
            throw new IllegalArgumentException("Incorrect patter was specified. Please use \'glob\' format");
        }
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (matcher.matches(file.getFileName())) {
            filesFound++;
        }
        return super.visitFile(file, attrs);
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        System.out.printf("In the directory = %s, %d files was found \n", dir.toAbsolutePath(), filesFound);
        filesFound = 0;
        return super.postVisitDirectory(dir, exc);
    }
}
