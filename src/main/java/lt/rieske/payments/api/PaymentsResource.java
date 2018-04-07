package lt.rieske.payments.api;

import lombok.RequiredArgsConstructor;
import lt.rieske.payments.domain.Payment;
import lt.rieske.payments.domain.PaymentsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;

import static lt.rieske.payments.api.PaymentsResource.RESOURCE_PATH;

@RestController
@RequestMapping(RESOURCE_PATH)
@RequiredArgsConstructor
public class PaymentsResource {

    public static final String RESOURCE_PATH = "/api/v1/payments";

    private final PaymentsRepository paymentsRepository;

    @GetMapping
    public Collection<Payment> getAllPayments() {
        return paymentsRepository.findAllPayments();
    }

    @PostMapping
    public ResponseEntity<Void> createPayment(@RequestBody Payment payment) {
        if (paymentsRepository.findPayment(payment.getId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        String paymentId = paymentsRepository.save(payment);

        return ResponseEntity.created(resourceLocation(paymentId)).build();
    }

    @GetMapping(path = "/{paymentId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Payment> getPayment(@PathVariable String paymentId) {
        return paymentsRepository.findPayment(paymentId).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/{paymentId}")
    public ResponseEntity<Void> deletePayment(@PathVariable String paymentId) {

        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "/{paymentId}")
    public ResponseEntity<Void> updatePayment(@PathVariable String paymentId, @RequestBody Payment payment) {
        if (!paymentsRepository.findPayment(paymentId).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        if (!paymentId.equals(payment.getId())) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.noContent().location(resourceLocation(paymentId)).build();
    }

    private URI resourceLocation(String paymentId) {
        return URI.create(RESOURCE_PATH + "/" + paymentId);
    }
}