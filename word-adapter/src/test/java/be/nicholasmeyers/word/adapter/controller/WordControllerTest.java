package be.nicholasmeyers.word.adapter.controller;

import be.nicholasmeyers.word.usecase.WordUseCase;
import be.nicholasmeyers.word.usecase.model.WordModel;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WordControllerTest.class)
@ContextConfiguration(classes = ControllerTestConfig.class)
public class WordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WordUseCase wordUseCase;

    @Test
    public void processWordFile() throws Exception {
        String request = """
                word
                wo
                rd
                or
                """;

        String response = """
                [{"result":"word","parts":["wo","rd"]}]
                """;

        Mockito
                .when(wordUseCase.processWordFile(request))
                .thenReturn(Collections.singletonList(new WordModel("word", Arrays.asList("wo", "rd"))));

        mockMvc.perform(post("/word")
                        .content(request)
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response, true));
    }

    @Test
    public void processWordFileInvalidContentType() throws Exception {
        String request = """
                <html></html>
                """;

        String response = """
                {"title":"Invalid file","status":400,"detail":"Invalid content type","instance":"/word"}
                """;

        mockMvc.perform(post("/word")
                        .content(request)
                        .contentType(MediaType.TEXT_PLAIN_VALUE)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(response, true));
    }
}
