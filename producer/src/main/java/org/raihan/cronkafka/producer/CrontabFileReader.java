package org.raihan.cronkafka.consumer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CrontabFileReader {

    public static List<String> read(String path) throws IOException {
        return Files.readAllLines(Path.of(path));  // later return an iterator?
    }

}
