package be.nicholasmeyers.word.adapter.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DatabaseTest
public class WordJpaRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private WordJpaRepository repository;

    @Transactional
    @Test
    public void save() {
        WordEntity word = new WordEntity(UUID.randomUUID(), "file.txt");
        repository.save(word);

        List<WordEntity> words = testEntityManager.getEntityManager().createQuery("SELECT w FROM WordEntity w", WordEntity.class)
                .getResultList();

        Assertions.assertEquals(1, words.size());
        Assertions.assertNotNull(words.getFirst().getId());
        Assertions.assertEquals("file.txt", words.getFirst().getFile());
    }

    @Transactional
    @Test
    public void findById() {
        WordEntity wordEntity = new WordEntity(UUID.fromString("16f4befa-fdea-4408-a820-29ddc8fcbf60"), "file.txt");
        testEntityManager.persistAndFlush(wordEntity);

        Optional<WordEntity> word = repository.findById(UUID.fromString("16f4befa-fdea-4408-a820-29ddc8fcbf60"));
        Assertions.assertTrue(word.isPresent());
        Assertions.assertEquals(UUID.fromString("16f4befa-fdea-4408-a820-29ddc8fcbf60"), word.get().getId());
        Assertions.assertEquals("file.txt", word.get().getFile());

    }
}
