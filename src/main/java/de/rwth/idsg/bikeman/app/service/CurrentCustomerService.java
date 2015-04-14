package de.rwth.idsg.bikeman.app.service;

import de.rwth.idsg.bikeman.app.dto.ChangeTariffDTO;
import de.rwth.idsg.bikeman.app.dto.ViewBookedTariffDTO;
import de.rwth.idsg.bikeman.app.dto.ViewCustomerDTO;
import de.rwth.idsg.bikeman.app.exception.AppErrorCode;
import de.rwth.idsg.bikeman.app.exception.AppException;
import de.rwth.idsg.bikeman.app.repository.CustomerRepository;
import de.rwth.idsg.bikeman.domain.*;
import de.rwth.idsg.bikeman.repository.TariffRepository;
import de.rwth.idsg.bikeman.service.UserService;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Slf4j
public class CurrentCustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TariffRepository tariffRepository;

    public ViewCustomerDTO get() throws DatabaseException {
        return customerRepository.findOne(
                userService.getUserWithAuthorities().getUserId());
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
