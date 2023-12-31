package hac.util;

import hac.repo.entities.Image;
import hac.repo.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

/**
 * The component initialize the images in the database
 */
@Component
public class ImageProcessor {

    private static final String IMAGES_FOLDER_PATH = "src/main/resources/static/images";

    @Autowired
    private ImageRepository imageRepository;

    /** Save the images in the db **/
    public void saveImageFileNamesToRepository() {
        try {
            // Get the absolute folder path
            Path absoluteFolderPath = Paths.get(IMAGES_FOLDER_PATH).toAbsolutePath();

            // Iterate through each category folder
            try (Stream<Path> categoryFolders = Files.walk(absoluteFolderPath, 1)) {
                categoryFolders
                        .filter(Files::isDirectory)
                        .skip(1) // Skip the root folder itself
                        .forEach(this::processCategoryFolder);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save images paths in the repository according to the category folder
     * @param categoryFolder the folder of the images that show which category they are
     */
    private void processCategoryFolder(Path categoryFolder) {
        String category = categoryFolder.getFileName().toString();

        try (Stream<Path> imageFiles = Files.walk(categoryFolder, 1)) {
            imageFiles
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        // Extract only the file name (without the path)
                        String fileName = file.getFileName().toString();

                        // Create an Image entity and save it to the repository
                        Image image = new Image("../images/"+category+"/"+fileName, category);
                        imageRepository.save(image);
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
