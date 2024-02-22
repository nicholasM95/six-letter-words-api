package be.nicholasmeyers.word.adapter.storage;

import be.nicholasmeyers.word.adapter.exception.WordFileException;
import be.nicholasmeyers.word.usecase.FileStorageService;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;

@RequiredArgsConstructor
@Slf4j
@Service
public class StorageService implements FileStorageService {

    @Value("${bucket.name}")
    private String bucket;

    private final AmazonS3 storageClient;

    @Override
    public void uploadFile(File file) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, file.getName(), file);
        storageClient.putObject(putObjectRequest);
        log.info("File {} saved in S3", file.getName());
        file.deleteOnExit();
    }

    @Override
    public InputStream downloadFile(String file) {
        try {
            S3Object s3Object = storageClient.getObject(bucket, file);
            log.info("File {} downloaded from S3", file);
            return s3Object.getObjectContent();
        } catch (SdkClientException e) {
            throw new WordFileException("File not found", HttpStatus.NOT_FOUND);
        }
    }
}
