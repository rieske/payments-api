package lt.rieske.payments.infrastructure;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionMappingAdvice {

    @ExceptionHandler({ConversionFailedException.class})
    public ResponseEntity<Void> handleException(Exception ex, WebRequest request) {
        return ResponseEntity.badRequest().build();
    }
}
