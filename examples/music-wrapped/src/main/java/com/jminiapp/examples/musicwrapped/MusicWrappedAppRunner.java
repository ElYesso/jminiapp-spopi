package com.jminiapp.examples.musicwrapped;

import com.jminiapp.core.engine.JMiniAppRunner;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MusicWrappedAppRunner {
    public static void main(String[] args) {
        Path rootRelative = Paths.get("examples/music-wrapped/src/main/resources/");
        Path moduleRelative = Paths.get("src/main/resources/");
        Path resourcesPath = Files.exists(rootRelative)
                ? rootRelative.toAbsolutePath()
                : moduleRelative.toAbsolutePath();

        JMiniAppRunner
            .forApp(MusicWrappedApp.class)
            .withState(MusicWrappedState.class)
            .withAdapters(new MusicWrappedJSONAdapter())
            // Use an absolute resources path that works from both repo root and module dir
            .withResourcesPath(resourcesPath.toString())
            .named("MusicWrapped")
            .run(args);
    }
}
