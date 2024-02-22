package be.nicholasmeyers.word.usecase.model;

import java.util.List;
import java.util.UUID;

public record WordCreatedModel(UUID id, List<WordModel> results) {
}
