package de.rwth.idsg.bikeman.service;

import de.rwth.idsg.bikeman.domain.ActivationKeyType;
import de.rwth.idsg.bikeman.domain.Customer;
import de.rwth.idsg.bikeman.repository.ActivationKeyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;


@Service
@Slf4j
public class ActivationKeyService {
    @Inject
    private ActivationKeyRepository activationKeyRepository;


    public String createForRegistration(Customer customer) throws Exception {
        return activationKeyRepository.create(customer, ActivationKeyType.REGISTRATION);
    }

    @Transactional
    public String createForPasswordReset(Customer customer) {
        //TODO check for excessive password resets in the past
        return activationKeyRepository.create(customer, ActivationKeyType.PASSWORD_RESET);
    }
}
