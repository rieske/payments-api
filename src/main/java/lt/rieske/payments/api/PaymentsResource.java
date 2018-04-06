package lt.rieske.payments.api;

import lombok.RequiredArgsConstructor;
import lt.rieske.payments.domain.Payment;
import lt.rieske.payments.domain.PaymentsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;

import static lt.rieske.payments.api.PaymentsResource.RESOURCE_PATH;

@RestController
@RequestMapping(RESOURCE_PATH)
@RequiredArgsConstructor
public class PaymentsResource {

    public static final String RESOURCE_PATH = "/api/v1/payments";

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

    @PostMapping
    public ResponseEntity<Void> createPayment(@RequestBody Payment payment) {
        System.out.println(payment);
        if (paymentsRepository.findPayment(payment.getId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        String paymentId = paymentsRepository.save(payment);

        return ResponseEntity.created(URI.create(RESOURCE_PATH + "/" + paymentId)).build();
    }
}

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class PaymentNotFoundException extends RuntimeException {
}
