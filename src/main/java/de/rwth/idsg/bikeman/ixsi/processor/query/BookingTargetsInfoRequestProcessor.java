package de.rwth.idsg.bikeman.ixsi.processor.query;

import de.rwth.idsg.bikeman.ixsi.dto.query.BookingTargetsInfoResponseDTO;
import de.rwth.idsg.bikeman.ixsi.dto.query.PedelecDTO;
import de.rwth.idsg.bikeman.ixsi.processor.Processor;
import de.rwth.idsg.bikeman.ixsi.repository.QueryIXSIRepository;
import de.rwth.idsg.bikeman.ixsi.schema.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Slf4j
@Component
public class BookingTargetsInfoRequestProcessor implements
        Processor<BookingTargetsInfoRequestType, BookingTargetsInfoResponseType> {

    @Inject
    private QueryIXSIRepository queryIXSIRepository;

    @Override
    public BookingTargetsInfoResponseType process(BookingTargetsInfoRequestType request) {
        BookingTargetsInfoResponseType response = new BookingTargetsInfoResponseType();

        BookingTargetsInfoResponseDTO responseDTO = queryIXSIRepository.bookingTargetInfos();

        response.setTimestamp(responseDTO.getTimestamp());

        List<PedelecDTO> pedelecs = responseDTO.getPedelecs();
        List<BookingTargetType> targets = new List<BookingTargetType>;
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
//            target.getPlaceIDOrPlaceGroupIDOrAreaID(
            //TODO ...

        }

        return response;
    }
}