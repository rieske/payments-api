package lt.rieske.payments.infrastructure;

import lombok.Data;
import lt.rieske.payments.domain.BadForexCurrencyException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
class ExceptionMappingAdvice {

    @ExceptionHandler(BadForexCurrencyException.class)
    ResponseEntity<BadRequest> handleBadForexCurrencyException(Exception ex, WebRequest request) {
        return ResponseEntity.badRequest().body(new BadRequest(ex.getMessage()));
    }

    @ExceptionHandler(ConversionFailedException.class)
    ResponseEntity<BadRequest> handleConversionFailedException(Exception ex, WebRequest request) {
        return ResponseEntity.badRequest().body(new BadRequest(ex.getCause().getMessage()));
    }
}

@Data
class BadRequest {
    final String message;
}