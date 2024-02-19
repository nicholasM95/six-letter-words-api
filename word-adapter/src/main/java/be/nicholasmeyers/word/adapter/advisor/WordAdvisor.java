package be.nicholasmeyers.word.adapter.advisor;


import be.nicholasmeyers.word.adapter.exception.WordFileException;
import be.nicholasmeyers.word.adapter.resource.ProblemDetailResponseResource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class WordAdvisor extends ResponseEntityExceptionHandler {
    @ExceptionHandler({WordFileException.class})
    protected ResponseEntity<ProblemDetailResponseResource> handleException(WordFileException exception, HttpServletRequest request) {
        ProblemDetailResponseResource problemDetail = new ProblemDetailResponseResource("Invalid file", HttpStatus.BAD_REQUEST.value(), exception.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }
}
