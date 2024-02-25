package be.nicholasmeyers.word.usecase;

import be.nicholasmeyers.word.usecase.model.WordFileModel;

import java.util.List;
import java.util.UUID;

public interface WordRepository {
    UUID save(String file);

    String getFileName(UUID id);

    List<WordFileModel> findAll();
}
