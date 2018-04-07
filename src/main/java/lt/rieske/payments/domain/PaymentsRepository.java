package lt.rieske.payments.domain;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class PaymentsRepository {

    public Optional<Payment> findPayment(String paymentId) {
        throw new NotImplementedException("Not implemented!");
    }

    public String save(Payment payment) {
        throw new NotImplementedException("Not implemented!");
    }

    public Collection<Payment> findAllPayments() {
        throw new NotImplementedException("Not implemented!");
    }
}
