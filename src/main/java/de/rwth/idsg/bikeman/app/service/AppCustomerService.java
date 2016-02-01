package de.rwth.idsg.bikeman.app.service;

import de.rwth.idsg.bikeman.app.dto.CreateCustomerDTO;
import de.rwth.idsg.bikeman.app.exception.AppException;
import de.rwth.idsg.bikeman.app.repository.AppCustomerRepository;
import de.rwth.idsg.bikeman.domain.ActivationKey;
import de.rwth.idsg.bikeman.domain.ActivationKeyType;
import de.rwth.idsg.bikeman.domain.Customer;
import de.rwth.idsg.bikeman.domain.User;
import de.rwth.idsg.bikeman.repository.UserRepository;
import de.rwth.idsg.bikeman.service.ActivationKeyService;
import de.rwth.idsg.bikeman.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class AppCustomerService {
    @Autowired
    private AppCustomerRepository appCustomerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivationKeyService activationKeyService;

    @Autowired
    private MailService mailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CreateCustomerDTO create(CreateCustomerDTO dto) throws AppException {
        CreateCustomerDTO obj = appCustomerRepository.create(dto);

        //TODO: createActivationKey
        //TODO: sendActivationEmail

        return obj;
    }

    @Transactional
    public Boolean requestPasswordReset(String login) {
        Optional<Customer> customer = appCustomerRepository.findByLogin(login);

        if (!customer.isPresent()) {
            return false;
        }

        String key = activationKeyService.createForPasswordReset(customer.get());
        mailService.sendPasswortResetEmail(customer.get(), key);

        return true;
    }

    @Transactional
    public Boolean changePassword(String login, String key, String password, String passwordConfirm) {
        Optional<Customer> customer = appCustomerRepository.findByLogin(login);
        Optional<ActivationKey> activationKey =
                activationKeyService.getNotUsedAndValid(key, ActivationKeyType.PASSWORD_RESET);

        if (!customer.isPresent()) {
            log.debug("customer not present");
            return false;
        }

        if (!activationKey.isPresent()) {
            log.debug("activationkey not present");
            return false;
        }

        if (!customer.get().equals(activationKey.get().getCustomer())) {
            log.debug("customer hijacking");
            return false;
        }

        if (password.compareTo(passwordConfirm) != 0) {
            log.debug("passwords unequal");
            return false;
        }

        if (!activationKeyService.markUsed(activationKey.get())) {
            log.debug("activationkey double usage");
            return false;
        }

        User user = customer.get();
        String encryptedPassword = passwordEncoder.encode(password);

        user.setPassword(encryptedPassword);
        userRepository.save(customer.get());

        return true;
    }

    public Boolean validatePasswordResetKey(String key) {
        Optional<ActivationKey> activationKey =
                activationKeyService.getNotUsedAndValid(key, ActivationKeyType.PASSWORD_RESET);

        return activationKey.isPresent();
    }

}
