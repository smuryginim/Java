package ru.ivansmurygin.ocp.io_nio2;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by SmuryginIM on 09.04.2016.
 */
public class CounterVisitor extends SimpleFileVisitor<Path> {
    int dirs, files;

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        System.out.println("======= " + dir.toAbsolutePath() + "========");
        dirs++;
        return super.preVisitDirectory(dir, attrs);
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        files ++;
        System.out.println("Visit file with name in directory" + file.getFileName());
        return super.visitFile(file, attrs);
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        System.out.println("Exit directory with full path = " + dir.toAbsolutePath());
        return super.postVisitDirectory(dir, exc);
    }
}
