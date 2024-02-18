package be.nicholasmeyers.word.adapter.controller;

import be.nicholasmeyers.word.adapter.exception.WordFileException;
import be.nicholasmeyers.word.adapter.resource.WordResponseResource;
import be.nicholasmeyers.word.usecase.WordUseCase;
import lombok.RequiredArgsConstructor;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AutoDetectParser;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class WordController implements WordApi {

    private final WordUseCase wordUseCase;

    @Override
    public ResponseEntity<List<WordResponseResource>> processWordFile(Resource body) throws IOException {
        if (isContentTypeValid(body.getInputStream())) {
            List<WordResponseResource> responseResources = wordUseCase.processWordFile(body.getContentAsString(StandardCharsets.UTF_8))
                    .stream()
                    .map(result -> WordResponseResource.builder()
                            .result(result.value())
                            .parts(result.parts())
                            .build())
                    .toList();
            return ResponseEntity.ok(responseResources);
        }
        throw new WordFileException("Invalid content type");
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
