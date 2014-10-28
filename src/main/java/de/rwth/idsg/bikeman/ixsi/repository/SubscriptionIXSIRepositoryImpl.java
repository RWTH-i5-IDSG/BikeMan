package de.rwth.idsg.bikeman.ixsi.repository;

import de.rwth.idsg.bikeman.ixsi.dto.subscription.AvailabilitySubscriptionResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.subscription.AvailabilitySubscriptionStatusResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.subscription.CompleteAvailabilityResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.subscription.CompletePlaceAvailabilityResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.subscription.HeartBeatResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.subscription.PlaceAvailabilitySubscriptionResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.subscription.PlaceAvailabilitySubscriptionStatusResponseDTO;

/**
 * Created by max on 06/10/14.
 */
public class SubscriptionIXSIRepositoryImpl implements SubscriptionIXSIRepository {
    @Override
    public HeartBeatResponseDTO heartBeat() {
        return null;
    }

    @Override
    public AvailabilitySubscriptionResponseDTO availabilitySubscription() {
        return null;
    }

    @Override
    public AvailabilitySubscriptionStatusResponseDTO availabilitySubscriptionStatus() {
        return null;
    }

    @Override
    public PlaceAvailabilitySubscriptionResponseDTO placeAvailabilitySubscription() {
        return null;
    }

    @Override
    public PlaceAvailabilitySubscriptionStatusResponseDTO placeAvailabilitySubscriptionStatus() {
        return null;
    }

//    @Override
//    public BookingAlertSubscriptionResponseDTO bookingAlertSubscription() {
//        return null;
//    }
//
//    @Override
//    public BookingAlertSubscriptionStatusResponseDTO bookingAlertSubscriptionStatus() {
//        return null;
//    }

    @Override
    public CompleteAvailabilityResponseDTO completeAvailability() {
        return null;
    }

    @Override
    public CompletePlaceAvailabilityResponseDTO completePlaceAvailability() {
        return null;
    }

//    @Override
//    public CompleteBookingAlertResponseDTO completeBookingAlert() {
//        return null;
//    }
}
