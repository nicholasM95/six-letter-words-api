package be.nicholasmeyers.word.adapter.controller;

import be.nicholasmeyers.word.adapter.exception.WordFileException;
import be.nicholasmeyers.word.adapter.resource.WordFileResponseResource;
import be.nicholasmeyers.word.adapter.resource.WordResponseResource;
import be.nicholasmeyers.word.usecase.WordUseCase;
import be.nicholasmeyers.word.usecase.model.WordCreatedModel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class WordController implements WordApi {

    private final WordUseCase wordUseCase;

    @Override
    public ResponseEntity<List<WordResponseResource>> processWordFile(MultipartFile multipartFile) throws IOException, URISyntaxException {
        File file = new File(LocalDateTime.now().toEpochSecond(java.time.ZoneOffset.UTC) + "_" + multipartFile.getOriginalFilename());
        FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);

        if (isContentTypeValid(new FileInputStream(file))) {
            WordCreatedModel wordCreatedModel = wordUseCase.processWordFile(file);
            List<WordResponseResource> responseResources = wordCreatedModel.results()
                    .stream()
                    .map(result -> WordResponseResource.builder()
                            .result(result.value())
                            .parts(result.parts())
                            .build())
                    .toList();
            String location = String.format("/word/%s", wordCreatedModel.id());
            return ResponseEntity.created(new URI(location)).body(responseResources);
        }
        throw new WordFileException("Invalid content type", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<WordFileResponseResource>> getWordFiles() {
        return ResponseEntity.ok(wordUseCase.getWordFiles().stream().map(wordFileModel -> WordFileResponseResource.builder()
                .id(wordFileModel.id().toString())
                .file(wordFileModel.file())
                .build()).toList());
    }

    @Override
    public ResponseEntity<List<WordResponseResource>> getWordFileById(String id) throws IOException {
        List<WordResponseResource> responseResources = wordUseCase.processWordFile(UUID.fromString(id))
                .stream()
                .map(result -> WordResponseResource.builder()
                        .result(result.value())
                        .parts(result.parts())
                        .build())
                .toList();
        return ResponseEntity.ok(responseResources);
    }

    private boolean isContentTypeValid(InputStream inputStream) throws IOException {
        return "text/plain".equals(getContentType(inputStream));
    }

    private String getContentType(InputStream inputStream) throws IOException {
        TikaInputStream tis = TikaInputStream.get(inputStream);
        AutoDetectParser parser = new AutoDetectParser();
        Detector detector = parser.getDetector();
        MediaType contentType = detector.detect(tis, new Metadata());
        return contentType.getBaseType().toString();
    }
}
