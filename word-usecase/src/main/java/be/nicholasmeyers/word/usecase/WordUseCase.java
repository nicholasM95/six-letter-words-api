package be.nicholasmeyers.word.usecase;

import be.nicholasmeyers.word.domain.Part;
import be.nicholasmeyers.word.domain.Word;
import be.nicholasmeyers.word.domain.WordFactory;

import be.nicholasmeyers.word.usecase.model.WordCreatedModel;
import be.nicholasmeyers.word.usecase.model.WordFileModel;
import be.nicholasmeyers.word.usecase.model.WordModel;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;

@RequiredArgsConstructor
public class WordUseCase {

    private final WordFactory wordFactory;
    private final WordRepository wordRepository;
    private final FileStorageService fileStorageService;

    public WordCreatedModel processWordFile(File file, String fileName) throws IOException {
        String wordFileContent = new String(Files.readAllBytes(file.toPath()));
        List<WordModel> results = processWordFile(wordFileContent);

        UUID id = wordRepository.save(fileName);
        fileStorageService.uploadFile(file, fileName);
        return new WordCreatedModel(id, results);
    }

    public List<WordModel> processWordFile(UUID id) throws IOException {
       String file = wordRepository.getFileName(id);
       return processWordFile(IOUtils.toString(fileStorageService.downloadFile(file), StandardCharsets.UTF_8));
    }

    private List<WordModel> processWordFile(String wordFileContent) {
        List<WordModel> results = new ArrayList<>();
        Set<String> words = getWordListAsCollection(wordFileContent);
        int maxWordLength = getMaxWordSizeLength(words);

        getAllWords(words, maxWordLength).forEach(w -> {
            Word word = wordFactory.create(w, getAllParts(words, maxWordLength).stream().filter(w::contains).toList());
            results.add(new WordModel(word.getWord(), word.getParts().stream().filter(Part::hasPair).map(Part::getPart).toList()));
        });
        return results;
    }

    public List<WordFileModel> getWordFiles() {
        return wordRepository.findAll();
    }

    private Set<String> getWordListAsCollection(String wordList) {
        return Arrays.stream(wordList.toLowerCase().split("\\s+")).collect(Collectors.toSet());
    }

    private int getMaxWordSizeLength(Collection<String> words) {
        AtomicInteger maxWordSizeLength = new AtomicInteger();
        words.forEach(word -> {
            if (word.length() > maxWordSizeLength.get()) {
                maxWordSizeLength.set(word.length());
            }
        });
        return maxWordSizeLength.get();
    }

    private Collection<String> getAllParts(Collection<String> words, int maxWordLength) {
        return words.stream().filter(word -> maxWordLength != word.length()).toList();
    }

    private Collection<String> getAllWords(Collection<String> words, int maxWordLength) {
        return words.stream().filter(word -> maxWordLength == word.length()).toList();
    }
}
