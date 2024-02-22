package be.nicholasmeyers.word.adapter.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class WordFileException extends RuntimeException {
    private HttpStatus httpStatus;

    public WordFileException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
