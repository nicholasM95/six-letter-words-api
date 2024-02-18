package be.nicholasmeyers.word.usecase.model;

import java.util.List;

public record WordModel(String value, List<String> parts) {
}
