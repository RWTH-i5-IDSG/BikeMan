package de.rwth.idsg.bikeman.ixsi.service;

import de.rwth.idsg.bikeman.domain.CardAccount;
import de.rwth.idsg.bikeman.domain.CustomerType;
import de.rwth.idsg.bikeman.domain.MajorCustomer;
import de.rwth.idsg.bikeman.domain.OperationState;
import de.rwth.idsg.bikeman.ixsi.schema.UserFeatureClassType;
import de.rwth.idsg.bikeman.ixsi.schema.UserFeatureType;
import de.rwth.idsg.bikeman.ixsi.schema.UserInfoType;
import de.rwth.idsg.bikeman.ixsi.schema.UserType;
import de.rwth.idsg.bikeman.repository.CardAccountRepository;
import de.rwth.idsg.bikeman.repository.MajorCustomerRepository;
import de.rwth.idsg.bikeman.web.rest.exception.DatabaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by max on 06/05/15.
 */
@Service
@Slf4j
public class IxsiUserService {

    @Autowired
    CardAccountRepository cardAccountRepository;
    @Autowired
    MajorCustomerRepository majorCustomerRepository;

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
        String userState = user.getState().value();
        account.setOperationState(OperationState.fromValue(userState));
        account.setCardPin(cardPinValue);
        account.setOwnerType(CustomerType.MAJOR_CUSTOMER);

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
