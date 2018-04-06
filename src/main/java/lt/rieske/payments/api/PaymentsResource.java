package lt.rieske.payments.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentsResource {

    @GetMapping
    public Collection<String> getAllPayments() {
        return Collections.emptyList();
    }

}
