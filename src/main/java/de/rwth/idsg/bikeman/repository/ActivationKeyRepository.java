package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.ActivationKeyType;
import de.rwth.idsg.bikeman.domain.Customer;

public interface ActivationKeyRepository {
    String create(Customer customer, ActivationKeyType type);
}
