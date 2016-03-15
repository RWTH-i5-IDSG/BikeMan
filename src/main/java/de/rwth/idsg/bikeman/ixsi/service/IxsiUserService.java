package de.rwth.idsg.bikeman.ixsi.service;

import de.rwth.idsg.bikeman.domain.BookedTariff;
import de.rwth.idsg.bikeman.domain.CardAccount;
import de.rwth.idsg.bikeman.domain.CustomerType;
import de.rwth.idsg.bikeman.domain.MajorCustomer;
import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.domain.TariffType;
import de.rwth.idsg.bikeman.repository.CardAccountRepository;
import de.rwth.idsg.bikeman.repository.MajorCustomerRepository;
import de.rwth.idsg.bikeman.repository.TariffRepository;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xjc.schema.ixsi.UserFeatureClassType;
import xjc.schema.ixsi.UserFeatureType;
import xjc.schema.ixsi.UserInfoType;
import xjc.schema.ixsi.UserType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by max on 06/05/15.
 */
@Service
@Slf4j
public class IxsiUserService {

    @Autowired private CardAccountRepository cardAccountRepository;
    @Autowired private MajorCustomerRepository majorCustomerRepository;
    @Autowired private TariffRepository tariffRepository;

    public List<UserType> createUsers(List<UserType> users) {
        List<UserType> acceptedUsers = new ArrayList<>();

        for (UserType user : users) {
            try {
                acceptedUsers.add(createUserInternal(user));
            } catch (Exception e) {
                log.debug("Could not create user: {}", user);
            }
        }

        return acceptedUsers;
    }

    private UserType createUserInternal(UserType user) {
        UserInfoType info = user.getID();
        List<UserFeatureType> features = user.getFeatures();

        // extract values from k/v store
        String cardPinValue = this.getPin(features);
        String majorCustomerName = this.getMajorCustomer(features);

        CardAccount account = new CardAccount();
        account.setCardId(info.getUserID());
        account.setOperationState(OperationState.fromValue(user.getState().value()));
        account.setCardPin(cardPinValue);
        account.setOwnerType(CustomerType.MAJOR_CUSTOMER);

        // TODO:
        // Create a different tariff type for IXSI users, since the transaction/usage/consumption logic varies
        // from regular users, and assign to this tariff
        BookedTariff tariff = new BookedTariff();
        tariff.setBookedFrom(new LocalDateTime());
        tariff.setBookedUntil(null);
        tariff.setTariff(tariffRepository.findByName(TariffType.Ticket2000));
        tariff.setUsedCardAccount(account);
        account.setCurrentTariff(tariff);

        // find corresponding major customer
        MajorCustomer maj = majorCustomerRepository.findByName(majorCustomerName);
        account.setUser(maj);
        cardAccountRepository.save(account);
        return user;
    }

    public List<UserType> changeUsers(List<UserType> users) {
        List<UserType> acceptedUsers = new ArrayList<>();

        for (UserType user : users) {
            try {
                acceptedUsers.add(changeUserInternal(user));
            } catch (DatabaseException e) {
                log.debug("Could not perform changes for user: {}", user);
            }
        }
        return acceptedUsers;
    }

    private UserType changeUserInternal(UserType user) {
        UserInfoType info = user.getID();
        List<UserFeatureType> features = user.getFeatures();

        // extract values from k/v store
        String cardPinValue = this.getPin(features);
        String majorCustomerName = this.getMajorCustomer(features);

        // obtain majorCustomer from db
        MajorCustomer maj = majorCustomerRepository.findByName(majorCustomerName);

        // obtain cardAccount using mc-name
        CardAccount account = cardAccountRepository.findByCardIdAndMajorCustomerName(info.getUserID(),
            majorCustomerName).orElseThrow(() -> new DatabaseException("No Entity"));

        // TODO is OperationState going to be changed?
//                if (account.getOperationState() == OperationState.DELETED)
//                    throw new DatabaseException("Cannot change deleted users.");

        account.setUser(maj);
        account.setCardPin(cardPinValue);

        String userState = user.getState().value();
        account.setOperationState(OperationState.fromValue(userState));

        cardAccountRepository.save(account);
        return user;
    }

    private String getPin(List<UserFeatureType> features) {
        for (UserFeatureType feat : features) {
            if (feat.getClazz() == UserFeatureClassType.RFID_CARD_PIN) {
                return feat.getValue();
            }
        }
        return null;
    }

    private String getMajorCustomer(List<UserFeatureType> features) {
        for (UserFeatureType feat : features) {
            if (feat.getClazz() == UserFeatureClassType.MAJOR_CUSTOMER_NAME) {
                return feat.getValue();
            }
        }
        return null;
    }
}
