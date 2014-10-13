package de.rwth.idsg.bikeman.ixsi.processor.query;

import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.ixsi.dto.query.BookingTargetsInfoResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.PedelecDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.StationDTO;
import de.rwth.idsg.bikeman.ixsi.processor.Processor;
import de.rwth.idsg.bikeman.ixsi.repository.QueryIXSIRepository;
import de.rwth.idsg.bikeman.ixsi.schema.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Slf4j
@Component
public class BookingTargetsInfoRequestProcessor implements
        Processor<BookingTargetsInfoRequestType, BookingTargetsInfoResponseType> {

    @Inject private QueryIXSIRepository queryIXSIRepository;

    @Override
    public BookingTargetsInfoResponseType process(BookingTargetsInfoRequestType request) {
        BookingTargetsInfoResponseType response = new BookingTargetsInfoResponseType();

        BookingTargetsInfoResponseDTO responseDTO = queryIXSIRepository.bookingTargetInfos();

        // List to hold stationIds for placegroup
        List<ProbabilityPlaceIDType> placeIds = new ArrayList<>();

        // response timestamp
        try {
            GregorianCalendar c = new GregorianCalendar();
            c.setTime(new Date(responseDTO.getTimestamp()));
            response.setTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
        } catch (Exception e) {
            log.error(e.getMessage());
        }


        // BEGIN response pedelecs
        List<PedelecDTO> pedelecs = responseDTO.getPedelecs();
        List<BookingTargetType> bookingTargets = new ArrayList<>();
        for (PedelecDTO ped : pedelecs) {
            BookingTargetType target = new BookingTargetType();

            // set pedelecId
            BookeeIDType id = new BookeeIDType();
            NMTOKEN token = new NMTOKEN();
            token.setValue(String.valueOf(ped.getPedelecId()));
            id.setValue(token);
            target.setID(id);

            // set manufacturerId
            TextType name = new TextType();
            name.setText(ped.getManufacturerId());
            target.getName().add(name);

            // set placeGroupId
            target.setPlaceIDOrPlaceGroupIDOrAreaID(IXSIConstants.PlaceGroup.id);

            // set maxDistance
            NonNegativeInteger nni = new NonNegativeInteger();
            nni.setValue(BigInteger.valueOf(ped.getMaxDistance()));
            target.setMaxDistance(nni);

            // set class
            ClassType clazz = new ClassType();
            NMTOKEN clazzToken = new NMTOKEN();
            clazzToken.setValue(IXSIConstants.bookeeClassType);
            clazz.setValue(clazzToken);
            target.setClazz(clazz);

            // set engine
            EngineType engine = new EngineType();
            NMTOKEN engineToken = new NMTOKEN();
            engineToken.setValue(IXSIConstants.engineType);
            engine.setValue(engineToken);
            target.setEngine(engine);

            bookingTargets.add(target);
        }
        response.getBookee().addAll(bookingTargets);
        // END response pedelecs

        // BEGIN response stations
        List<StationDTO> stations = responseDTO.getStations();
        List<PlaceType> places = new ArrayList<>();
        for (StationDTO stat : stations) {
            PlaceType place = new PlaceType();

            // set placeID
            PlaceIDType id = new PlaceIDType();
            NMTOKEN token = new NMTOKEN();
            token.setValue(String.valueOf(stat.getStationId()));
            id.setValue(token);
            place.setID(id);

            // add the placeId to placeGroup list
            ProbabilityPlaceIDType probPlaceId = new ProbabilityPlaceIDType();
            probPlaceId.setID(id);
            placeIds.add(probPlaceId);

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
            NMTOKEN provToken = new NMTOKEN();
            provToken.setValue(IXSIConstants.Provider.id);
            provId.setValue(provToken);
            place.setProviderID(provId);

            // set place description
            TextType desc = new TextType();

            // TODO for max: compose description from dto.note and dto.address
            //desc.setText(stat.getDescription());

            place.getDescription().add(desc);

            places.add(place);
        }
        response.getPlace().addAll(places);
        // END response stations

        // BEGIN response providers
        ProviderType provider = new ProviderType();

        // set providerId
        ProviderIDType providerId = new ProviderIDType();
        NMTOKEN providerIdToken = new NMTOKEN();
        providerIdToken.setValue(IXSIConstants.Provider.id);
        providerId.setValue(providerIdToken);
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
        NMTOKEN placeGroupIdToken = new NMTOKEN();
        placeGroupIdToken.setValue(IXSIConstants.PlaceGroup.id);
        placeGroupId.setValue(placeGroupIdToken);
        placegroup.setID(placeGroupId);

        // set placegroup placeIds
        // TODO workaround, perform new repository request here?
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
}