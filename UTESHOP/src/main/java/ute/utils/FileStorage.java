package ute.utils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Part;

public class FileStorage {

    private final Path uploadDir;

    public FileStorage(ServletContext context, String relativeUploadPath) {
        String realPath = context.getRealPath(relativeUploadPath);
        this.uploadDir = Paths.get(realPath);
        if (!Files.exists(uploadDir))
            throw new RuntimeException("The upload directory doesn't exist: " + uploadDir);
    }

    /**
     * Save an uploaded Part (from @MultipartConfig servlet)
     */
    public String save(Part filePart) throws IOException {
        if (filePart == null || filePart.getSize() == 0)
            return null;

        String originalName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        String extension = "";
        int dotIndex = originalName.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalName.substring(dotIndex);
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uniqueName = UUID.randomUUID().toString() + "_" + timestamp + extension;

        Path filePath = uploadDir.resolve(uniqueName);
        filePart.write(filePath.toString());

        System.out.println("Lưu thành công file: " + uniqueName);

        return uniqueName;
    }

    /**
     * Build a public URL or relative path to the saved file
     */
    public String getFileUrl(String fileName, String contextPath, String relativeUploadPath) {
        return contextPath + relativeUploadPath + "/" + fileName;
    }
}
