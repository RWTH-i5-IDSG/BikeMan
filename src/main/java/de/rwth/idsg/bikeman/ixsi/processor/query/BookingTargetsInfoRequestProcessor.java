package de.rwth.idsg.bikeman.ixsi.processor.query;

import de.rwth.idsg.bikeman.ixsi.ErrorFactory;
import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.ixsi.dto.query.BookingTargetsInfoResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.PedelecDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.StationDTO;
import de.rwth.idsg.bikeman.ixsi.repository.QueryIXSIRepository;
import de.rwth.idsg.bikeman.ixsi.schema.*;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewAddressDTO;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.math.BigInteger;
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
        ProviderIDType providerId = new ProviderIDType();
        providerId.setValue(IXSIConstants.Provider.id);
        provider.setID(providerId);

        // set provider name
        provider.setName(IXSIConstants.Provider.name);
        provider.setShortName(IXSIConstants.Provider.shortName);

        // set provider URLs
        provider.setURL(IXSIConstants.Provider.url);
        provider.setLogoURL(IXSIConstants.Provider.logoUrl);
        provider.setInterAppBaseURL(IXSIConstants.Provider.interAppBaseUrl);
        provider.setWebAppBaseURL(IXSIConstants.Provider.webAppBaseUrl);

        response.getProvider().add(provider);
        // END response providers

        // BEGIN response placegroups
        PlaceGroupType placegroup = new PlaceGroupType();

        // set placegroupId
        PlaceGroupIDType placeGroupId = new PlaceGroupIDType();
        placeGroupId.setValue(IXSIConstants.PlaceGroup.id);
        placegroup.setID(placeGroupId);

        // set placegroup placeIds
        List<ProbabilityPlaceIDType> placeIds = getPlaceIdsFromPlaceTypes(places);
        placegroup.getPlaceID().addAll(placeIds);

        // set probability (Why do we even need this?)
        PercentType percent = new PercentType();
        NonNegativeInteger percentValue = new NonNegativeInteger();
        percentValue.setValue(BigInteger.valueOf(100));        
        percent.setValue(percentValue);
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
            BookeeIDType id = new BookeeIDType();
            id.setValue(String.valueOf(ped.getPedelecId()));
            target.setID(id);

            // set manufacturerId
            TextType name = new TextType();
            name.setText(ped.getManufacturerId());
            target.getName().add(name);

            // set placeGroupId
            PlaceGroupIDType placeGroupID = new PlaceGroupIDType();
            placeGroupID.setValue(IXSIConstants.PlaceGroup.id);
            target.setPlaceGroupID(placeGroupID);

            // set maxDistance
            NonNegativeInteger nni = new NonNegativeInteger();
            nni.setValue(BigInteger.valueOf(ped.getMaxDistance()));
            target.setMaxDistance(nni);

            // set class
            ClassType clazz = new ClassType();
            clazz.setValue(IXSIConstants.bookeeClassType);
            target.setClazz(clazz);

            // set engine
            EngineType engine = new EngineType();
            engine.setValue(IXSIConstants.engineType);
            target.setEngine(engine);

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
            PlaceIDType id = new PlaceIDType();
            id.setValue(String.valueOf(stat.getStationId()));
            place.setID(id);

            // set place coordinates
            CoordType coords = new CoordType();
            coords.setLatitude(stat.getLocation_latitude());
            coords.setLongitude(stat.getLocation_longitude());
            place.setCoord(coords);

            // set place capacity
            NonNegativeInteger capacityValue = new NonNegativeInteger();
            capacityValue.setValue(BigInteger.valueOf(stat.getSlotCount()));
            place.setCapacity(capacityValue);

            // set place name
            TextType name = new TextType();
            name.setText(stat.getName());
            place.getName().add(name);

            // set place providerId
            ProviderIDType provId = new ProviderIDType();
            provId.setValue(IXSIConstants.Provider.id);
            place.setProviderID(provId);

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
    public BookingTargetsInfoResponseType invalidSystem() {
        BookingTargetsInfoResponseType b = new BookingTargetsInfoResponseType();
        b.getError().add(ErrorFactory.invalidSystem());
        return b;
    }
}