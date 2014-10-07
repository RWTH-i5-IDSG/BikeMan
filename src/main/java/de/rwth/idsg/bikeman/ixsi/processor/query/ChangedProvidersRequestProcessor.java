package de.rwth.idsg.bikeman.ixsi.processor.query;

import de.rwth.idsg.bikeman.ixsi.dto.query.ChangedProvidersResponseDTO;
import de.rwth.idsg.bikeman.ixsi.processor.Processor;
import de.rwth.idsg.bikeman.ixsi.repository.QueryIXSIRepository;
import de.rwth.idsg.bikeman.ixsi.schema.ChangedProvidersRequestType;
import de.rwth.idsg.bikeman.ixsi.schema.ChangedProvidersResponseType;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

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
        ChangedProvidersResponseDTO responseDTO = queryIXSIRepository.changedProviders();

        ChangedProvidersResponseType response = new ChangedProvidersResponseType();
//        response.getProvider().addAll(responseDTO.getProviders());

        return response;
    }
}
