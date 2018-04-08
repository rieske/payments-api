package lt.rieske.payments.domain;

import lt.rieske.payments.domain.Payment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "payments", path = "payments")
public interface PaymentsRepository extends CrudRepository<Payment, String> {
}