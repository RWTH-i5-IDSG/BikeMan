package de.rwth.idsg.bikeman.service;

import de.rwth.idsg.bikeman.domain.ErrorHistory;
import de.rwth.idsg.bikeman.domain.ErrorType;
import de.rwth.idsg.bikeman.repository.ErrorHistoryRepository;
import de.rwth.idsg.bikeman.web.rest.dto.view.ErrorHistoryEntryDTO;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Wolfgang Kluth on 19/02/16.
 */

@Service
@Transactional
public class ErrorHistoryService {

    @Autowired
    private ErrorHistoryRepository errorHistoryRepository;

    public void createAndSaveErrorHistoryEntry(ErrorType errorType, String errorCode, String errorInfo,
                                               String manufacturerId) {
        ErrorHistory errorHistory = new ErrorHistory();
        errorHistory.setErrorType(errorType);
        errorHistory.setErrorCode(errorCode);
        errorHistory.setErrorInfo(errorInfo);
        errorHistory.setManufacturerId(manufacturerId);

        errorHistoryRepository.save(errorHistory);
    }

    public List<ErrorHistoryEntryDTO> getErrorHistory() {
        List<ErrorHistory> errorHistoryEntries = errorHistoryRepository.findAllOrderByCreatedAt();

        return errorHistoryEntries.stream().map(e -> convertErrorHistoryToDTO(e)).collect(Collectors.toList());
    }

    private ErrorHistoryEntryDTO convertErrorHistoryToDTO(ErrorHistory errorHistory) {
        return ErrorHistoryEntryDTO.builder()
                                   .createdAt(new LocalDateTime(errorHistory.getCreatedAt()))
                                   .errorCode(errorHistory.getErrorCode())
                                   .errorInfo(errorHistory.getErrorInfo())
                                   .manufacturerId(errorHistory.getManufacturerId())
                                   .errorType(errorHistory.getErrorType())
                                   .build();
    }
}
