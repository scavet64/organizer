package com.scavettapps.organizer.Util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class EnvironmentUtils {

    private static final String WINDOWS_PATH = "C:/temp/organizer";
    private static final String CONTAINER_DATA_PATH = "./data";

    public static String getDataPath() {
        return EnvironmentUtils.isRunningInsideDocker() ? CONTAINER_DATA_PATH : WINDOWS_PATH;
    }

    public static Boolean isRunningInsideDocker() {

        try (Stream < String > stream =
            Files.lines(Paths.get("/proc/1/cgroup"))) {
            return stream.anyMatch(line -> line.contains("/docker"));
        } catch (IOException e) {
            return false;
        }
    }
}