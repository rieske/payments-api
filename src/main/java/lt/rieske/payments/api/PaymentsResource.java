package lt.rieske.payments.api;

import lombok.RequiredArgsConstructor;
import lt.rieske.payments.domain.PaymentsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentsResource {

    private final PaymentsRepository paymentsRepository;

    @GetMapping
    public Collection<String> getAllPayments() {
        return Collections.emptyList();
    }

    @GetMapping(path = "/{paymentId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getPayment(@PathVariable String paymentId) {
        return paymentsRepository.findPayment(paymentId)
          .orElseThrow(PaymentNotFoundException::new);
    }
}

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class PaymentNotFoundException extends RuntimeException {
}
