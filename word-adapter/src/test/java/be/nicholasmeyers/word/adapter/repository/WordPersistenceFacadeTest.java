package be.nicholasmeyers.word.adapter.repository;

import be.nicholasmeyers.word.adapter.exception.WordDataException;
import be.nicholasmeyers.word.usecase.model.WordFileModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class WordPersistenceFacadeTest {

    @Mock
    private WordJpaRepository wordJpaRepository;

    private WordPersistenceFacade wordPersistenceFacade;

    @BeforeEach
    public void init() {
        wordPersistenceFacade = new WordPersistenceFacade(wordJpaRepository);
    }

    @Test
    public void save() {
        ArgumentCaptor<WordEntity> wordEntityArgumentCaptor = ArgumentCaptor.forClass(WordEntity.class);
        
        Mockito
                .when(wordJpaRepository.save(wordEntityArgumentCaptor.capture()))
                .thenReturn(new WordEntity(UUID.fromString("f06964e9-90cf-4868-83f8-a7255aacadbe"), ""));

        UUID id = wordPersistenceFacade.save("file.txt");

        Assertions.assertEquals(UUID.fromString("f06964e9-90cf-4868-83f8-a7255aacadbe"), id);
        Assertions.assertEquals("file.txt", wordEntityArgumentCaptor.getValue().getFile());

    }

    @Test
    public void getFileName() {
        Mockito
                .when(wordJpaRepository.findById(UUID.fromString("dede57ea-9fba-4094-bbd7-4588742ba6bf")))
                .thenReturn(Optional.of(new WordEntity(UUID.fromString("dede57ea-9fba-4094-bbd7-4588742ba6bf"), "file.txt")));

        String file = wordPersistenceFacade.getFileName(UUID.fromString("dede57ea-9fba-4094-bbd7-4588742ba6bf"));
        Assertions.assertEquals("file.txt", file);
    }

    @Test
    public void getFileNameIdNotExist() {
        Mockito
                .when(wordJpaRepository.findById(UUID.fromString("dede57ea-9fba-4094-bbd7-4588742ba6bf")))
                .thenReturn(Optional.empty());

        WordDataException exception = Assertions.assertThrows(WordDataException.class, () -> {
            wordPersistenceFacade.getFileName(UUID.fromString("dede57ea-9fba-4094-bbd7-4588742ba6bf"));
        });
        Assertions.assertEquals("Data with id dede57ea-9fba-4094-bbd7-4588742ba6bf not found in the database", exception.getMessage());
    }

    @Test
    public void findAll() {
        Mockito
                .when(wordJpaRepository.findAll())
                .thenReturn(Collections.singletonList(new WordEntity(UUID.fromString("dede57ea-9fba-4094-bbd7-4588742ba6bf"), "file.txt")));

        List<WordFileModel> result = wordPersistenceFacade.findAll();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(UUID.fromString("dede57ea-9fba-4094-bbd7-4588742ba6bf"), result.getFirst().id());
        Assertions.assertEquals("file.txt", result.getFirst().file());
    }
}
