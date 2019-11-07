package lt.rieske.payments.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

@RepositoryRestResource(collectionResourceRel = "payments", path = "payments")
public interface PaymentsRepository extends CrudRepository<Payment, UUID> {
}