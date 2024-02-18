package be.nicholasmeyers.word.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class WordFactoryTest {

    private WordFactory wordFactory;

    @BeforeEach
    public void init() {
        wordFactory = new WordFactory();
    }

    @Test
    public void create() {
        Word word = wordFactory.create("word", Arrays.asList("wo", "rd", "or"));
        Assertions.assertEquals("word", word.getWord());
        Assertions.assertEquals(3, word.getParts().size());
        Assertions.assertEquals("wo", word.getParts().get(0).getPart());
        Assertions.assertEquals("rd", word.getParts().get(1).getPart());
        Assertions.assertEquals("or", word.getParts().get(2).getPart());

        Assertions.assertTrue(word.getParts().get(0).hasPair());
        Assertions.assertTrue(word.getParts().get(1).hasPair());
        Assertions.assertFalse(word.getParts().get(2).hasPair());
    }
}
