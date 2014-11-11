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
public interface SubscriptionIXSIRepository {
    // Subscr. Administration response
    HeartBeatResponseDTO heartBeat();

    // Subscr. Response
    AvailabilitySubscriptionResponseDTO availabilitySubscription();
    AvailabilitySubscriptionStatusResponseDTO availabilitySubscriptionStatus();
    PlaceAvailabilitySubscriptionResponseDTO placeAvailabilitySubscription();
    PlaceAvailabilitySubscriptionStatusResponseDTO placeAvailabilitySubscriptionStatus();
//    BookingAlertSubscriptionResponseDTO bookingAlertSubscription();
//    BookingAlertSubscriptionStatusResponseDTO bookingAlertSubscriptionStatus();

    // Response Message
    CompleteAvailabilityResponseDTO completeAvailability();
    CompletePlaceAvailabilityResponseDTO completePlaceAvailability();
//    CompleteBookingAlertResponseDTO completeBookingAlert();
}
