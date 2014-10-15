package de.rwth.idsg.bikeman.ixsi.processor.query;

import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.ixsi.dto.query.ChangedProvidersResponseDTO;
import de.rwth.idsg.bikeman.ixsi.processor.Processor;
import de.rwth.idsg.bikeman.ixsi.repository.QueryIXSIRepository;
import de.rwth.idsg.bikeman.ixsi.schema.ChangedProvidersRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.ChangedProvidersResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.NMTOKEN;
import de.rwth.idsg.bikeman.ixsi.schema.ProviderIDType;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class ChangedProvidersRequestProcessor implements
        Processor<ChangedProvidersRequestType, ChangedProvidersResponseType> {

    @Inject
    private QueryIXSIRepository queryIXSIRepository;

    @Override
    public ChangedProvidersResponseType process(ChangedProvidersRequestType request) {
        long timestamp = request.getTimestamp().toGregorianCalendar().getTimeInMillis();
        ChangedProvidersResponseDTO responseDTO = queryIXSIRepository.changedProviders(timestamp);

        ChangedProvidersResponseType response = new ChangedProvidersResponseType();

        if (responseDTO.isProvidersChanged()) {
            ProviderIDType id = new ProviderIDType();
            NMTOKEN token = new NMTOKEN();
            token.setValue(IXSIConstants.Provider.id);
            id.setValue(token);

            response.getProvider().add(id);
        }

        return response;
    }
}
