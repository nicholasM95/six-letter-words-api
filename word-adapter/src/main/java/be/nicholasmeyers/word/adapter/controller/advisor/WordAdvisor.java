package be.nicholasmeyers.word.adapter.controller.advisor;


import be.nicholasmeyers.word.adapter.exception.WordDataException;
import be.nicholasmeyers.word.adapter.exception.WordFileException;
import be.nicholasmeyers.word.adapter.resource.ProblemDetailResponseResource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class WordAdvisor extends ResponseEntityExceptionHandler {
    @ExceptionHandler({WordFileException.class})
    protected ResponseEntity<ProblemDetailResponseResource> handleException(WordFileException exception, HttpServletRequest request) {
        ProblemDetailResponseResource problemDetail = new ProblemDetailResponseResource("Something went wrong with the file", exception.getHttpStatus().value(), exception.getMessage(), request.getRequestURI());
        log.error(problemDetail.toString());
        return new ResponseEntity<>(problemDetail, exception.getHttpStatus());
    }

    @ExceptionHandler({WordDataException.class})
    protected ResponseEntity<ProblemDetailResponseResource> handleException(WordDataException exception, HttpServletRequest request) {
        ProblemDetailResponseResource problemDetail = new ProblemDetailResponseResource("Something went wrong with the data", HttpStatus.NOT_FOUND.value(), exception.getMessage(), request.getRequestURI());
        log.error(problemDetail.toString());
        return new ResponseEntity<>(problemDetail, HttpStatus.NOT_FOUND);
    }
}
