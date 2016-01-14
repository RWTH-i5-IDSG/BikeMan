package de.rwth.idsg.bikeman.app.service;

import de.rwth.idsg.bikeman.app.dto.CreateCustomerDTO;
import de.rwth.idsg.bikeman.app.exception.AppException;
import de.rwth.idsg.bikeman.app.repository.CustomerRepository;
import de.rwth.idsg.bikeman.domain.Customer;
import de.rwth.idsg.bikeman.service.ActivationKeyService;
import de.rwth.idsg.bikeman.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service("CustomerServiceApp")
@Slf4j
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ActivationKeyService activationKeyService;

    @Autowired
    private MailService mailService;

    public CreateCustomerDTO create(CreateCustomerDTO dto) throws AppException {
        CreateCustomerDTO obj = customerRepository.create(dto);

        //TODO: createActivationKey
        //TODO: sendActivationEmail

        return obj;
    }

    public Optional<Customer> get(String login) {
        return customerRepository.findByLogin(login);
    }

    @Transactional
    public Boolean resetPassword(String login) {
        Optional<Customer> customer = this.get(login);

        if (!customer.isPresent())
            return false;

        return this.resetPassword(customer.get());
    }

    public Boolean resetPassword(Customer customer) {
        String key = activationKeyService.createForPasswordReset(customer);

        mailService.sendPasswortResetEmail(customer, key);

        return true;
    }

}
