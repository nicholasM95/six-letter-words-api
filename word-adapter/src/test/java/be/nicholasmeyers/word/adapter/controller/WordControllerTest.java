package be.nicholasmeyers.word.adapter.controller;

import be.nicholasmeyers.word.adapter.exception.WordDataException;
import be.nicholasmeyers.word.adapter.exception.WordFileException;
import be.nicholasmeyers.word.usecase.WordUseCase;
import be.nicholasmeyers.word.usecase.model.WordCreatedModel;
import be.nicholasmeyers.word.usecase.model.WordFileModel;
import be.nicholasmeyers.word.usecase.model.WordModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WordControllerTest.class)
@ContextConfiguration(classes = ControllerTestConfig.class)
public class WordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WordUseCase wordUseCase;

    @Test
    public void processWordFile() throws IOException {
        String request = """
                word
                wo
                rd
                or
                """;

        String response = """
                [{"result":"word","parts":["wo","rd"]}]
                """;

        ArgumentCaptor<File> fileArgumentCaptor = ArgumentCaptor.forClass(File.class);
        Mockito
                .when(wordUseCase.processWordFile(fileArgumentCaptor.capture(), Mockito.eq("file.txt")))
                .thenReturn(new WordCreatedModel(UUID.fromString("031e06cc-bfee-4d5a-a3f2-83ccc5e4fc36"), Collections.singletonList(new WordModel("word", Arrays.asList("wo", "rd")))));

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "file.txt",
                MediaType.TEXT_PLAIN_VALUE,
                request.getBytes()
        );

        tryOn(LocalDateTime.of(2024, 10, 10, 10, 10), () -> {
            try {
                mockMvc.perform(multipart("/word")
                                .file(file)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andExpect(header().stringValues("Location", "/word/031e06cc-bfee-4d5a-a3f2-83ccc5e4fc36"))
                        .andExpect(content().json(response, true));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Assertions.assertTrue(fileArgumentCaptor.getValue().getName().startsWith("upload"));
        Assertions.assertTrue(fileArgumentCaptor.getValue().getName().endsWith(".txt"));
        Assertions.assertEquals(request, Files.readString(fileArgumentCaptor.getValue().toPath()));
        Assertions.assertTrue(fileArgumentCaptor.getValue().delete());
    }

    @Test
    public void processWordFileInvalidContentType() throws Exception {
        String request = """
                <html></html>
                """;

        String response = """
                {"title":"Something went wrong with the file","status":400,"detail":"Invalid content type","instance":"/word"}
                """;

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "file.txt",
                MediaType.TEXT_PLAIN_VALUE,
                request.getBytes()
        );

        mockMvc.perform(multipart("/word")
                        .file(file)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(response, true));
    }

    @Test
    public void getWordFiles() throws Exception {
        String response = """
                [{"id":"031e06cc-bfee-4d5a-a3f2-83ccc5e4fc36","file":"file.txt"}]
                """;

        Mockito
                .when(wordUseCase.getWordFiles())
                .thenReturn(Collections.singletonList(new WordFileModel(UUID.fromString("031e06cc-bfee-4d5a-a3f2-83ccc5e4fc36"), "file.txt")));


        mockMvc.perform(get("/word")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response, true));
    }

    @Test
    public void getWordFileById() throws Exception {
        String response = """
                [{"result":"word","parts":["wo","rd"]}]
                """;

        Mockito
                .when(wordUseCase.processWordFile(UUID.fromString("76ac84fd-3a30-4b84-9000-acf9019baed3")))
                .thenReturn(Collections.singletonList(new WordModel("word", Arrays.asList("wo", "rd"))));


        mockMvc.perform(get("/word/76ac84fd-3a30-4b84-9000-acf9019baed3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response, true));
    }

    @Test
    public void getWordFileByIdFileNotFound() throws Exception {
        String response = """
                {"title":"Something went wrong with the file","status":404,"detail":"File not found","instance":"/word/76ac84fd-3a30-4b84-9000-acf9019baed3"}
                """;

        Mockito
                .when(wordUseCase.processWordFile(UUID.fromString("76ac84fd-3a30-4b84-9000-acf9019baed3")))
                .thenThrow(new WordFileException("File not found", HttpStatus.NOT_FOUND));


        mockMvc.perform(get("/word/76ac84fd-3a30-4b84-9000-acf9019baed3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(response, true));
    }

    @Test
    public void getWordFileByIdDataNotFound() throws Exception {
        String response = """
                {"title":"Something went wrong with the data","status":404,"detail":"Data not found","instance":"/word/76ac84fd-3a30-4b84-9000-acf9019baed3"}
                """;

        Mockito
                .when(wordUseCase.processWordFile(UUID.fromString("76ac84fd-3a30-4b84-9000-acf9019baed3")))
                .thenThrow(new WordDataException("Data not found"));


        mockMvc.perform(get("/word/76ac84fd-3a30-4b84-9000-acf9019baed3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(response, true));
    }

    private void tryOn(LocalDateTime fixedDate, Runnable test) {
        try (MockedStatic<LocalDateTime> mockedStatic = Mockito.mockStatic(LocalDateTime.class)) {
            mockedStatic.when(LocalDateTime::now).thenReturn(fixedDate);
            test.run();
        }
    }
}
