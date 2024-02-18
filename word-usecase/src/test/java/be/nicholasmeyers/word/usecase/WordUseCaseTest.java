package be.nicholasmeyers.word.usecase;

import be.nicholasmeyers.word.domain.Part;
import be.nicholasmeyers.word.domain.Word;
import be.nicholasmeyers.word.domain.WordFactory;
import be.nicholasmeyers.word.usecase.model.WordModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class WordUseCaseTest {

    private WordUseCase wordUseCase;

    @Mock
    private WordFactory wordFactory;

    @BeforeEach
    public void init() {
        wordUseCase = new WordUseCase(wordFactory);
    }

    @Test
    public void processWordFile() {
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


        Mockito.when(wordFactory.create(Mockito.eq("word"), Mockito.anyList())).thenReturn(new Word("word", parts));

        String wordFileContent = """
                word
                wo
                rd
                or
                """;
        List<WordModel> words = wordUseCase.processWordFile(wordFileContent);
        Assertions.assertEquals(1, words.size());
        Assertions.assertEquals("word", words.getFirst().value());
        Assertions.assertEquals(2, words.getFirst().parts().size());
        Assertions.assertEquals("wo", words.getFirst().parts().getFirst());
        Assertions.assertEquals("rd", words.getFirst().parts().get(1));
    }
}
