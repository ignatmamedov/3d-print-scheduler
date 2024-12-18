package utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class FileProvider {
    public static Reader getReaderFromResource(String filename) throws FileNotFoundException {
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be null or empty");
        }

        URL resource = FileProvider.class.getResource("/" + filename);
        if (resource == null) {
            throw new FileNotFoundException("Resource not found: " + filename);
        }

        return new FileReader(URLDecoder.decode(resource.getPath(), StandardCharsets.UTF_8));
    }
}
