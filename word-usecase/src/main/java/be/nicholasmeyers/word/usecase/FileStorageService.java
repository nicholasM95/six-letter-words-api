package be.nicholasmeyers.word.usecase;

import java.io.File;
import java.io.InputStream;

public interface FileStorageService {
    void uploadFile(File file, String filename);

    InputStream downloadFile(String file);
}
