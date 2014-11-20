package de.rwth.idsg.bikeman.ixsi.processor.query.staticdata;

import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.ixsi.dto.query.BookingTargetsInfoResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.PedelecDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.StationDTO;
import de.rwth.idsg.bikeman.ixsi.processor.api.StaticRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.repository.QueryIXSIRepository;
import de.rwth.idsg.bikeman.ixsi.schema.*;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewAddressDTO;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Slf4j
@Component
public class BookingTargetsInfoRequestProcessor implements
        StaticRequestProcessor<BookingTargetsInfoRequestType, BookingTargetsInfoResponseType> {

    @Inject private QueryIXSIRepository queryIXSIRepository;

    @Override
    public BookingTargetsInfoResponseType process(BookingTargetsInfoRequestType request) {
        BookingTargetsInfoResponseType response = new BookingTargetsInfoResponseType();

        BookingTargetsInfoResponseDTO responseDTO = queryIXSIRepository.bookingTargetInfos();

        // response timestamp
        long timestamp = responseDTO.getTimestamp();
        response.setTimestamp(new DateTime(timestamp));

        // BEGIN response pedelecs
        List<PedelecDTO> pedelecs = responseDTO.getPedelecs();
        List<BookingTargetType> bookingTargets = getBookingTargetsFromDTO(pedelecs);
        response.getBookee().addAll(bookingTargets);
        // END response pedelecs

        // BEGIN response stations
        List<StationDTO> stations = responseDTO.getStations();
        List<PlaceType> places = getPlaceTypesFromDTO(stations);
        response.getPlace().addAll(places);
        // END response stations

        // BEGIN response providers
        ProviderType provider = new ProviderType();

        // set providerId
        provider.setID(IXSIConstants.Provider.id);

        // set provider name
        provider.setName(IXSIConstants.Provider.name);
        provider.setShortName(IXSIConstants.Provider.shortName);

        // set provider URLs
        //TODO: new IXSI does not contain these.
//        provider.setURL(IXSIConstants.Provider.url);
//        provider.setLogoURL(IXSIConstants.Provider.logoUrl);
//        provider.setInterAppBaseURL(IXSIConstants.Provider.interAppBaseUrl);
//        provider.setWebAppBaseURL(IXSIConstants.Provider.webAppBaseUrl);

        response.getProvider().add(provider);
        // END response providers

        // BEGIN response placegroups
        PlaceGroupType placegroup = new PlaceGroupType();

        // set placegroupId
        placegroup.setID(IXSIConstants.PlaceGroup.id);

        // set placegroup placeIds
        List<ProbabilityPlaceIDType> placeIds = getPlaceIdsFromPlaceTypes(places);
        placegroup.getPlaceID().addAll(placeIds);

        // TODO: set probability (Why do we even need this?)
        PercentType percent = new PercentType();
        percent.setValue(100);
        placegroup.setProbability(percent);

        response.getPlaceGroup().add(placegroup);
        // END response placegroups

        return response;
    }

    private List<BookingTargetType> getBookingTargetsFromDTO(List<PedelecDTO> pedelecs) {
        List<BookingTargetType> bookingTargets = new ArrayList<>();
        for (PedelecDTO ped : pedelecs) {
            BookingTargetType target = new BookingTargetType();

            // set pedelecId
            target.setID(ped.getManufacturerId());

            // TODO: In our case pedelecs have no names,
            // for now we just set the manufacturerId
            TextType name = new TextType();
            name.setText(ped.getManufacturerId());
            target.getName().add(name);

            target.setMaxDistance(ped.getMaxDistance());
            target.setPlaceGroupID(IXSIConstants.PlaceGroup.id);
            target.setClazz(IXSIConstants.bookeeClassType);
            target.setEngine(IXSIConstants.engineType);

            bookingTargets.add(target);
        }

        return bookingTargets;
    }

    private List<ProbabilityPlaceIDType> getPlaceIdsFromPlaceTypes(List<PlaceType> placeTypes) {
        List<ProbabilityPlaceIDType> placeIds = new ArrayList<>();

        for (PlaceType placeType : placeTypes) {
            // add the placeId to placeGroup list
            ProbabilityPlaceIDType probPlaceId = new ProbabilityPlaceIDType();
            probPlaceId.setID(placeType.getID());
            placeIds.add(probPlaceId);
        }

        return placeIds;
    }

    private List<PlaceType> getPlaceTypesFromDTO(List<StationDTO> stations) {
        List<PlaceType> places = new ArrayList<>();
        for (StationDTO stat : stations) {
            PlaceType place = new PlaceType();

            // set placeID
            place.setID(stat.getManufacturerId());

            // set place coordinates
            CoordType coords = new CoordType();
            coords.setLatitude(stat.getLocation_latitude());
            coords.setLongitude(stat.getLocation_longitude());
            place.setCoord(coords);

            // set place capacity
            place.setCapacity(stat.getSlotCount());

            // set place name
            TextType name = new TextType();
            name.setText(stat.getName());
            place.getName().add(name);

            // set place providerId
            place.setProviderID(IXSIConstants.Provider.id);

            // set place description
            TextType desc = new TextType();
            desc.setText(String.format("Note: %s, Address: %s", stat.getNote(), formatAddress(stat.getAddress())));

            place.getDescription().add(desc);

            places.add(place);
        }

        return places;
    }

    private String formatAddress(ViewAddressDTO dto) {
        return String.format("%s, %s %s, %s", dto.getStreetAndHousenumber(), dto.getZip(), dto.getCity(), dto.getCountry());
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public BookingTargetsInfoResponseType buildError(ErrorType e) {
        BookingTargetsInfoResponseType b = new BookingTargetsInfoResponseType();
        b.getError().add(e);
        return b;
    }
}
