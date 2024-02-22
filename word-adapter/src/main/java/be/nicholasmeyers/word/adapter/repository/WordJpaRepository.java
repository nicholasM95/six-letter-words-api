package be.nicholasmeyers.word.adapter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WordJpaRepository extends JpaRepository<WordEntity, UUID> {
}
