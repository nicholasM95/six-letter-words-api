package be.nicholasmeyers.word.adapter.repository;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "word")
@Entity
public class WordEntity {

    @Id
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    private UUID id;

    @Column(name = "file_name")
    private String file;

    public WordEntity(UUID id, String file) {
        this.id = id;
        this.file = file;
    }
}
