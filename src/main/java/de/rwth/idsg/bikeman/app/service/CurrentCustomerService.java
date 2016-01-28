package de.rwth.idsg.bikeman.app.service;

import de.rwth.idsg.bikeman.app.dto.*;
import de.rwth.idsg.bikeman.app.exception.AppErrorCode;
import de.rwth.idsg.bikeman.app.exception.AppException;
import de.rwth.idsg.bikeman.app.repository.CustomerRepository;
import de.rwth.idsg.bikeman.app.repository.TransactionRepository;
import de.rwth.idsg.bikeman.domain.*;
import de.rwth.idsg.bikeman.repository.CardAccountRepository;
import de.rwth.idsg.bikeman.repository.TariffRepository;
import de.rwth.idsg.bikeman.repository.UserRepository;
import de.rwth.idsg.bikeman.service.UserService;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class CurrentCustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CardAccountRepository cardAccountRepository;

    @Autowired
    private TariffRepository tariffRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ViewCustomerDTO get() throws DatabaseException {
        return customerRepository.findOne(
                userService.getUserWithAuthorities().getUserId());
    }

    @Transactional
    public Boolean changePin(ChangePinDTO dto) {
        Customer customer = this.getCurrentCustomer();
        CardAccount cardAccount = customer.getCardAccount();

        if (!passwordEncoder.matches(dto.getPassword(), customer.getPassword())) {
            return false;
        }

        cardAccount.setCardPin(dto.getCardPin());
        cardAccountRepository.save(cardAccount);

        return true;
    }

    @Transactional
    public Boolean changePassword(ChangePasswordDTO dto) {
        Customer customer = this.getCurrentCustomer();

        if (!passwordEncoder.matches(dto.getOldPassword(), customer.getPassword())) {
            return false;
        }

        if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
            throw new AppException("Password and confirmation do not match", AppErrorCode.VALIDATION_FAILED);
        }

        userService.changePassword(dto.getPassword());

        return true;
    }

    @Transactional
    public Boolean changeAddress(ChangeAddressDTO dto) {
        Customer customer = this.getCurrentCustomer();

        // TODO: do not replace old address in the database but mark it as old and create a new object
        Address address = customer.getAddress();

        address.setStreetAndHousenumber(dto.getStreetAndHousenumber());
        address.setZip(dto.getZip());
        address.setCity(dto.getCity());
        address.setCountry(dto.getCountry());

        userRepository.save(customer);

        return true;
    }

    @Transactional(readOnly = true)
    public List<ViewTransactionDTO> getClosedTransactions(Integer page) throws DatabaseException {
        return transactionRepository.findByCustomerPaginated(this.getCurrentCustomer(), page);
    }

    @Transactional(readOnly = true)
    public Optional<ViewTransactionDTO> getOpenTransaction() throws DatabaseException {
        List<ViewTransactionDTO> transactions = transactionRepository.findOpenByCustomer(this.getCurrentCustomer());

        if (transactions.isEmpty()) {
            return Optional.empty();
        }

        // currently only the oldest open transaction is being returned
        return Optional.of(transactions.get(0));
    }

    public ViewBookedTariffDTO getTariff() {
        Customer customer = this.getCurrentCustomer();
        BookedTariff currentTariff = customer.getCardAccount().getCurrentTariff();

        ViewBookedTariffDTO dto = ViewBookedTariffDTO.builder()
            .tariffId(currentTariff.getTariff().getTariffId())
            .name(currentTariff.getName())
            .automaticRenewal(customer.getCardAccount().getAutoRenewTariff())
            .expiryDateTime(currentTariff.getBookedUntil())
            .build();

        return dto;
    }

    @Transactional
    public ChangeTariffDTO setTariff(ChangeTariffDTO dto) throws AppException {
        Customer customer = this.getCurrentCustomer();
        BookedTariff currentTariff = customer.getCardAccount().getCurrentTariff();

        if (currentTariff.getTariff().getTariffId().equals( dto.getTariffId() )) {
            throw new AppException("Old and new tariff are equal!", AppErrorCode.CONSTRAINT_FAILED);
        }
        if ( (currentTariff.getBookedUntil() != null)
            && (currentTariff.getBookedUntil().compareTo( LocalDateTime.now() ) == 1) ) {

            throw new AppException("Tariff change not possible due to active subscription!", AppErrorCode.CONSTRAINT_FAILED);
        }


        BookedTariff updateBookedTariff = new BookedTariff();
        updateBookedTariff.setBookedFrom(new LocalDateTime());

        if (tariffRepository.findByTariffId(dto.getTariffId()).getTerm() == null) {
            updateBookedTariff.setBookedUntil(null);
        } else {
            updateBookedTariff.setBookedUntil(new LocalDateTime().plusDays(
                tariffRepository.findByTariffId(dto.getTariffId()).getTerm()
            ));
        }

        updateBookedTariff.setTariff(tariffRepository.findByTariffId(dto.getTariffId()));
        customer.getCardAccount().setCurrentTariff(updateBookedTariff);
        this.enableAutomaticRenewal();

	    return dto;
    }

    @Transactional
    public Boolean enableAutomaticRenewal() throws AppException {
        this.getCurrentCustomer().getCardAccount().setAutoRenewTariff(true);
        return true;
    }

    @Transactional
    public Boolean disableAutomaticRenewal() throws AppException {
        CardAccount cardAccount = this.getCurrentCustomer().getCardAccount();

        if (cardAccount.getCurrentTariff().getTariff().getCategory().equals( TariffCategory.Default )) {
            return false;
        }

        cardAccount.setAutoRenewTariff(false);
        return true;
    }


    public Customer getCurrentCustomer () {
        User user = userService.getUserWithAuthorities();

        if (user instanceof Customer) {
            return (Customer) user;
        } else {
            return null;
        }
    }
}
