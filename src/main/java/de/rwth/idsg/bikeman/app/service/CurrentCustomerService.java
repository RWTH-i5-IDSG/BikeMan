package de.rwth.idsg.bikeman.app.service;

import de.rwth.idsg.bikeman.app.dto.ChangeTariffDTO;
import de.rwth.idsg.bikeman.app.dto.ViewBookedTariffDTO;
import de.rwth.idsg.bikeman.app.dto.ViewCustomerDTO;
import de.rwth.idsg.bikeman.app.repository.CardAccountRepository;
import de.rwth.idsg.bikeman.app.repository.CustomerRepository;
import de.rwth.idsg.bikeman.domain.*;
import de.rwth.idsg.bikeman.service.UserService;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;


@Service
@Slf4j
public class CurrentCustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Inject
    private UserService userService;

    @Inject
    CardAccountRepository cardAccountRepository;

    public ViewCustomerDTO get() throws DatabaseException {
        return customerRepository.findOne(
                userService.getUserWithAuthorities().getUserId());
    }

    public ViewBookedTariffDTO getTariff() {
        Customer customer = this.getCurrentCustomer();
        BookedTariff currentTariff = customer.getCardAccount().getCurrentTariff();

        ViewBookedTariffDTO dto = new ViewBookedTariffDTO(
            currentTariff.getTariff().getTariffId(),
            currentTariff.getName(),
            customer.getCardAccount().getAutoRenewTariff(),
            currentTariff.getBookedUntil()
        );

        return dto;
    }

    public void setTariff(ChangeTariffDTO dto) {
        Customer customer = this.getCurrentCustomer();
        BookedTariff currentTariff = customer.getCardAccount().getCurrentTariff();


        if (currentTariff.getTariff().getTariffId().equals( dto.getTariffId() )) {
            // TODO: HTTP status code
            return;
        }

        if ( (currentTariff.getBookedUntil() != null)
                        && (currentTariff.getBookedUntil().compareTo( LocalDateTime.now() ) == 1) ) {
            // TODO: HTTP status code
            return;
        }

        cardAccountRepository.setCurrentTariff(customer.getCardAccount().getCardAccountId(), dto.getTariffId());

        // enable automatic renewal after a tariff change
        this.enableAutomaticRenewal();
    }

    public void enableAutomaticRenewal() {
        cardAccountRepository.setAutomaticRenewal(this.getCurrentCustomer().getCardAccount().getCardAccountId(), true);
    }

    public void disableAutomaticRenewal() {
        CardAccount cardAccount = this.getCurrentCustomer().getCardAccount();

        if (cardAccount.getCurrentTariff().getTariff().getCategory().equals( TariffCategory.Default )) {
            return;
            //TODO: HTTP response: not possible, if default tariff is selected
        }

        cardAccountRepository.setAutomaticRenewal(cardAccount.getCardAccountId(), false);
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