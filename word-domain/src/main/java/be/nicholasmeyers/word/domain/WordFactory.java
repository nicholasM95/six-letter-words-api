package be.nicholasmeyers.word.domain;

import java.util.List;

public class WordFactory {

    public Word create(String value, List<String> parts) {
        Word word = new Word(value, parts.stream().map(Part::new).toList());
        word.findPairs();
        return word;
    }
}
