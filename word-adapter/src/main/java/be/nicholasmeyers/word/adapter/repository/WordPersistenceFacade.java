package be.nicholasmeyers.word.adapter.repository;

import be.nicholasmeyers.word.adapter.exception.WordDataException;
import be.nicholasmeyers.word.usecase.WordRepository;
import be.nicholasmeyers.word.usecase.model.WordFileModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class WordPersistenceFacade implements WordRepository {

    private final WordJpaRepository wordJpaRepository;

    @Override
    public UUID save(String file) {
        return wordJpaRepository.save(new WordEntity(UUID.randomUUID(), file)).getId();
    }

    @Override
    public String getFileName(UUID id) {
        Optional<WordEntity> word = wordJpaRepository.findById(id);
        if (word.isPresent()) {
            return word.get().getFile();
        }
        throw new WordDataException(String.format("Data with id %s not found in the database", id));
    }

    @Override
    public List<WordFileModel> findAll() {
        return wordJpaRepository.findAll().stream().map(wordEntity -> new WordFileModel(wordEntity.getId(), wordEntity.getFile())).toList();
    }
}
