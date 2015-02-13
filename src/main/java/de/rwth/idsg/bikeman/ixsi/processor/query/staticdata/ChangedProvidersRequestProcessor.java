package de.rwth.idsg.bikeman.ixsi.processor.query.staticdata;

import de.rwth.idsg.bikeman.ixsi.IXSIConstants;
import de.rwth.idsg.bikeman.ixsi.dto.query.ChangedProvidersResponseDTO;
import de.rwth.idsg.bikeman.ixsi.processor.api.StaticRequestProcessor;
import de.rwth.idsg.bikeman.ixsi.repository.QueryIXSIRepository;
import de.rwth.idsg.bikeman.ixsi.schema.ChangedProvidersRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.ChangedProvidersResponseType;
import de.rwth.idsg.bikeman.ixsi.schema.ErrorType;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 26.09.2014
 */
@Component
public class ChangedProvidersRequestProcessor implements
        StaticRequestProcessor<ChangedProvidersRequestType, ChangedProvidersResponseType> {

    @Inject private QueryIXSIRepository queryIXSIRepository;

    @Override
    public ChangedProvidersResponseType process(ChangedProvidersRequestType request) {
        long timestamp = request.getTimestamp().getMillis();
        ChangedProvidersResponseDTO responseDTO = queryIXSIRepository.changedProviders(timestamp);

        if (responseDTO.isProvidersChanged()) {
            return new ChangedProvidersResponseType().withProvider(IXSIConstants.Provider.id);
        } else {
            return new ChangedProvidersResponseType();
        }
    }

    // -------------------------------------------------------------------------
    // Error handling
    // -------------------------------------------------------------------------

    @Override
    public ChangedProvidersResponseType buildError(ErrorType e) {
        return new ChangedProvidersResponseType().withError(e);
    }
}
