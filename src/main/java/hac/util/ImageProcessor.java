package hac.util;

import hac.repo.entities.Image;
import hac.repo.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ImageProcessor {

    private static final String IMAGES_FOLDER_PATH = "src/main/resources/static/images";

    @Autowired
    private ImageRepository imageRepository;

    public void saveImageFileNamesToRepository() {
        try {
            // Get the absolute folder path
            Path absoluteFolderPath = Paths.get(IMAGES_FOLDER_PATH).toAbsolutePath();

            // Iterate through each file in the folder
            Files.walk(absoluteFolderPath)
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        // Extract only the file name (without the path)
                        String fileName = file.getFileName().toString();

                        // Create an Image entity and save it to the repository
                        Image image = new Image();
                        image.setPath(fileName);
                        imageRepository.save(image);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
