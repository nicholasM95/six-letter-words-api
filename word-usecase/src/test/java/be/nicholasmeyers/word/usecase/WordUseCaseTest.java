package be.nicholasmeyers.word.usecase;

import be.nicholasmeyers.word.domain.Part;
import be.nicholasmeyers.word.domain.Word;
import be.nicholasmeyers.word.domain.WordFactory;
import be.nicholasmeyers.word.usecase.model.WordFileModel;
import be.nicholasmeyers.word.usecase.model.WordModel;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class WordUseCaseTest {

    @InjectMocks
    private WordUseCase wordUseCase;

    @Mock
    private WordFactory wordFactory;

    @Mock
    private WordRepository wordRepository;

    @Mock
    private FileStorageService fileStorageService;

    @Test
    public void processWordFileWithFile() throws IOException {
        List<Part> parts = getParts();
        Mockito.when(wordFactory.create(Mockito.eq("word"), Mockito.anyList())).thenReturn(new Word("word", parts));

        String wordFileContent = """
                word
                wo
                rd
                or
                """;

        File file = createFileWithContent(wordFileContent);

        List<WordModel> words = wordUseCase.processWordFile(file).results();
        Assertions.assertEquals(1, words.size());
        Assertions.assertEquals("word", words.getFirst().value());
        Assertions.assertEquals(2, words.getFirst().parts().size());
        Assertions.assertEquals("wo", words.getFirst().parts().getFirst());
        Assertions.assertEquals("rd", words.getFirst().parts().get(1));

        Mockito.verify(wordRepository).save(file.getName());
        Mockito.verify(fileStorageService).uploadFile(file);
    }

    @Test
    public void processWordFileWithId() throws IOException {
        String wordFileContent = """
                word
                wo
                rd
                or
                """;

        Mockito
                .when(wordRepository.getFileName(UUID.fromString("7038b849-35b9-40f6-bce8-a5f59b39908f")))
                .thenReturn("file.txt");

        Mockito
                .when(fileStorageService.downloadFile("file.txt"))
                .thenReturn(IOUtils.toInputStream(wordFileContent, StandardCharsets.UTF_8));

        List<Part> parts = getParts();
        Mockito.when(wordFactory.create(Mockito.eq("word"), Mockito.anyList())).thenReturn(new Word("word", parts));


        List<WordModel> words = wordUseCase.processWordFile(UUID.fromString("7038b849-35b9-40f6-bce8-a5f59b39908f"));
        Assertions.assertEquals(1, words.size());
        Assertions.assertEquals("word", words.getFirst().value());
        Assertions.assertEquals(2, words.getFirst().parts().size());
        Assertions.assertEquals("wo", words.getFirst().parts().getFirst());
        Assertions.assertEquals("rd", words.getFirst().parts().get(1));

    }

    @Test
    public void getWordFiles() {
        Mockito
                .when(wordRepository.findAll())
                .thenReturn(Collections.singletonList(new WordFileModel(UUID.fromString("e1abcfd2-a885-4f91-87db-13ebfc98b046"), "file.txt")));

        List<WordFileModel> results = wordUseCase.getWordFiles();
        Assertions.assertEquals(1, results.size());
        Assertions.assertEquals(UUID.fromString("e1abcfd2-a885-4f91-87db-13ebfc98b046"), results.getFirst().id());
        Assertions.assertEquals("file.txt", results.getFirst().file());
    }

    private static File createFileWithContent(String content) throws IOException {
        File file = File.createTempFile("test", ".txt");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
        return file;
    }

    private List<Part> getParts() {
        List<Part> parts = new ArrayList<>();
        Part part1 = new Part("wo");
        part1.setHasPair(true);

        Part part2 = new Part("rd");
        part2.setHasPair(true);

        Part part3 = new Part("or");
        part3.setHasPair(false);

        parts.add(part1);
        parts.add(part2);
        parts.add(part3);

        return parts;
    }
}
