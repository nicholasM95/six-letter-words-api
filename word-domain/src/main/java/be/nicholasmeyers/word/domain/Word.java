package be.nicholasmeyers.word.domain;

import java.util.List;

public class Word {
    private final String word;
    private List<Part> parts;

    public Word(String word, List<Part> parts) {
        this.word = word;
        this.parts = parts;
    }

    public void findPairs() {
        for (Part part1: parts) {
            for (Part part2 : parts) {
                String possibleExactMatch = part1.getPart() + part2.getPart();
                if (word.equals(possibleExactMatch)) {
                    part1.setHasPair(true);
                    part2.setHasPair(true);
                }
            }
        }
    }

    public String getWord() {
        return word;
    }

    public List<Part> getParts() {
        return parts;
    }
}
