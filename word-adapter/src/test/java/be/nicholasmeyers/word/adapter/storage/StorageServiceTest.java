package be.nicholasmeyers.word.adapter.storage;

import be.nicholasmeyers.word.adapter.exception.WordFileException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@SpringBootTest
@ContextConfiguration(classes = StorageTestConfig.class)
@Testcontainers
public class StorageServiceTest {

    @Container
    static LocalStackContainer localStack = new LocalStackContainer(
            DockerImageName.parse("localstack/localstack:3.0")
    ).withServices(LocalStackContainer.Service.S3);
    @Autowired
    private StorageService service;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("bucket.name", () -> "test-bucket");
        registry.add("bucket.region", () -> localStack.getRegion());
        registry.add("bucket.access-key", () -> localStack.getAccessKey());
        registry.add("bucket.secret-key", () -> localStack.getSecretKey());
        registry.add("bucket.endpoint", () -> localStack.getEndpoint().toString());
    }

    @BeforeAll
    static void beforeAll() throws IOException, InterruptedException {
        localStack.execInContainer("awslocal", "s3", "mb", "s3://test-bucket");
    }

    @Test
    public void uploadFile() throws IOException {
        File file = new File("input.txt");
        FileUtils.writeStringToFile(file, """
                hello
                he
                llo
                lo
                """, StandardCharsets.UTF_8);
        service.uploadFile(file);
    }

    @Test
    public void downloadFile() throws IOException {
        uploadFile();
        InputStream inputStream = service.downloadFile("input.txt");
        String fileContent = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        Assertions.assertEquals("""
                hello
                he
                llo
                lo
                """, fileContent);
    }

    @Test
    public void downloadFileFileNotFound() {
        WordFileException exception = Assertions.assertThrows(WordFileException.class, () -> {
            service.downloadFile("foobar.txt");
        });
        Assertions.assertEquals("File not found", exception.getMessage());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }
}
