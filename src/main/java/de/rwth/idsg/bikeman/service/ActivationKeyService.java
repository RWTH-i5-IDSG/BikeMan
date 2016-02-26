package de.rwth.idsg.bikeman.service;

import de.rwth.idsg.bikeman.domain.ActivationKey;
import de.rwth.idsg.bikeman.domain.ActivationKeyType;
import de.rwth.idsg.bikeman.domain.Customer;
import de.rwth.idsg.bikeman.repository.ActivationKeyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Optional;


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

    @Transactional(readOnly = true)
    public Optional<ActivationKey> getNotUsedAndValid(String key, ActivationKeyType type) {
        return activationKeyRepository.findNotUsedAndNotExpired(key, type);
    }

    @Transactional(readOnly = false)
    public Boolean markUsed(ActivationKey activationKey) {
        if (activationKey.getUsed() == true) {
            return false;
        }

        activationKey.setUsed(true);
        activationKeyRepository.save(activationKey);

        return true;
    }
}
