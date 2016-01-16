package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.ActivationKey;
import de.rwth.idsg.bikeman.domain.ActivationKeyType;
import de.rwth.idsg.bikeman.domain.Customer;

import java.util.Optional;

public interface ActivationKeyRepository {
    String create(Customer customer, ActivationKeyType type);
    ActivationKey save(ActivationKey activationKey);

    Optional<ActivationKey> findNotUsedAndNotExpired(String key, ActivationKeyType type);
}
