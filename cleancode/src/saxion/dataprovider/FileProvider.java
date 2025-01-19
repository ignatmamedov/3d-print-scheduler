package saxion.dataprovider;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for providing file readers from resources.
 */
public class FileProvider {

    /**
     * Retrieves a {@link Reader} for the specified resource file.
     *
     * @param filename the name of the resource file
     * @return a {@link Reader} for reading the file's content
     * @throws FileNotFoundException    if the resource file is not found
     * @throws IllegalArgumentException if the filename is null or empty
     */
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
