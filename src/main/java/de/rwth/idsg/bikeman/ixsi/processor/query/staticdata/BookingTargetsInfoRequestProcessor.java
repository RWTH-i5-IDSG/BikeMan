package de.rwth.idsg.bikeman.ixsi.processor.query.staticdata;

import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.ixsi.dto.BookingTargetsInfoResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.PedelecDTO;
import de.rwth.idsg.bikeman.ixsi.dto.StationDTO;
import de.rwth.idsg.bikeman.ixsi.processor.api.StaticRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.repository.QueryIXSIRepository;
import de.rwth.idsg.bikeman.web.rest.dto.view.ViewAddressDTO;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import xjc.schema.ixsi.*;

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

    // "VelocityBikeSharing" encoded to Base64
    private static final String BIKE_SHARING_ATTR_ID = "VmVsb2NpdHlCaWtlU2hhcmluZw==";

    @Override
    public BookingTargetsInfoResponseType process(BookingTargetsInfoRequestType request) {
        BookingTargetsInfoResponseDTO dto = queryIXSIRepository.bookingTargetInfos();

        AttributeType placeTypeAttr = new AttributeType()
                .withID(BIKE_SHARING_ATTR_ID)
                .withClazz(AttributeClassType.BIKE_SHARING)
                .withWithText(false);

        // response timestamp
        long timestamp = dto.getTimestamp();

        // pedelecs
        List<BookingTargetType> bookingTargets = getBookingTargetsFromDTO(dto.getPedelecs());

        // stations
        List<PlaceType> places = getPlaceTypesFromDTO(dto.getStations());

        // providers
        ProviderType provider = new ProviderType()
                .withID(IXSIConstants.Provider.id)
                .withName(IXSIConstants.Provider.name)
                .withShortName(IXSIConstants.Provider.shortName);

        // placegroups
        PlaceGroupType placegroup = new PlaceGroupType()
                .withID(IXSIConstants.PlaceGroup.id)
                .withPlaceID(getPlaceIdsFromPlaceTypes(places))
                .withProbability(new PercentType().withValue(100)); // TODO: set probability (Why do we even need this?)

        return new BookingTargetsInfoResponseType()
                .withAttributes(placeTypeAttr)
                .withTimestamp(new DateTime(timestamp))
                .withBookee(bookingTargets)
                .withPlace(places)
                .withProvider(provider)
                .withPlaceGroup(placegroup);
    }

    private List<BookingTargetType> getBookingTargetsFromDTO(List<PedelecDTO> pedelecs) {
        List<BookingTargetType> bookingTargets = new ArrayList<>();
        for (PedelecDTO ped : pedelecs) {

            BookingTargetIDType id = new BookingTargetIDType()
                    .withBookeeID(ped.getManufacturerId())
                    .withProviderID(IXSIConstants.Provider.id);

            // TODO: In our case pedelecs have no names, for now we just set the manufacturerId
            TextType name = new TextType()
                    .withText(ped.getManufacturerId());

            bookingTargets.add(new BookingTargetType()
                    .withID(id)
                    .withName(name)
                    .withPlaceGroupID(IXSIConstants.PlaceGroup.id)
                    .withClazz(IXSIConstants.bookeeClassType)
                    .withEngine(IXSIConstants.engineType));
        }
        return bookingTargets;
    }

    private List<ProbabilityPlaceIDType> getPlaceIdsFromPlaceTypes(List<PlaceType> placeTypes) {
        List<ProbabilityPlaceIDType> placeIds = new ArrayList<>();
        for (PlaceType placeType : placeTypes) {
            placeIds.add(new ProbabilityPlaceIDType().withID(placeType.getID()));
        }
        return placeIds;
    }

    private List<PlaceType> getPlaceTypesFromDTO(List<StationDTO> stations) {
        List<PlaceType> places = new ArrayList<>();
        for (StationDTO stat : stations) {

            ViewAddressDTO viewAddressDTO = stat.getAddress();

            AddressType address = new AddressType()
                    .withCountry(viewAddressDTO.getCountry())
                    .withPostalCode(viewAddressDTO.getZip())
                    .withCity(viewAddressDTO.getCity())
                    .withStreetHouseNr(viewAddressDTO.getStreetAndHousenumber());

            CoordType coords = new CoordType()
                    .withLatitude(stat.getLocation_latitude())
                    .withLongitude(stat.getLocation_longitude());

            GeoPositionType geoPosition = new GeoPositionType()
                    .withAddress(address)
                    .withCoord(coords);

            TextType name = new TextType()
                    .withText(stat.getName())
                    .withLanguage(IXSIConstants.DEFAULT_LANGUAGE);

            places.add(new PlaceType()
                    .withAttributeID(BIKE_SHARING_ATTR_ID)
                    .withGeoPosition(geoPosition)
                    .withID(stat.getManufacturerId())
                    .withCapacity((int) stat.getSlotCount())
                    .withName(name)
                    .withProviderID(IXSIConstants.Provider.id));
        }
        return places;
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public BookingTargetsInfoResponseType buildError(ErrorType e) {
        return new BookingTargetsInfoResponseType().withError(e);
    }
}
