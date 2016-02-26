package de.rwth.idsg.bikeman.web.rest.dto.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import de.rwth.idsg.bikeman.web.rest.dto.modify.CardAccountBaseDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wolfgang Kluth on 23/02/16.
 */


public class CardAccountBatchDeserializer extends JsonDeserializer<List<CardAccountBaseDTO>> {

    private final static String CSV_SEPARATOR = ",";

    @Override
    public List<CardAccountBaseDTO> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {

        String rawBatch = jsonParser.getText();

        List<CardAccountBaseDTO> cardAccounts = new ArrayList<>();

        // convert string into hashmap
        String lines[] = rawBatch.split("\\r?\\n");

        for (String line : lines) {
            String[] idAndPin = line.split(CSV_SEPARATOR);

            if (idAndPin.length != 2) {
                throw new IOException("CSV-String has not the right format: " + rawBatch);
            }

            CardAccountBaseDTO cardAccountBaseDTO = new CardAccountBaseDTO();
            cardAccountBaseDTO.setCardId(idAndPin[0]);
            cardAccountBaseDTO.setCardPin(idAndPin[1]);
            cardAccounts.add(cardAccountBaseDTO);
        }

        return cardAccounts;
    }

}
