package de.rwth.idsg.bikeman.psinterface;

import com.google.common.collect.Sets;
import de.rwth.idsg.bikeman.domain.Station;
import de.rwth.idsg.bikeman.utils.ItemIdComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 31.07.2018
 */
public class IgnoreUtils {

    // TODO: change the value
    private static final String STATION_MANUFACTURER_ID = "change-me-1";

    // TODO: change the values
    private static final Set<Integer> SLOT_POSITIONS = Sets.newHashSet(
            -1,
            -1,
            -1
    );

    // TODO: change the values
    private static final Set<String> PEDELEC_MANUFACTURER_IDS = Sets.newHashSet(
            "change-me-2",
            "change-me-3",
            "change-me-4"
    );

    public static List<String> getSlotsToDelete(Station station, ItemIdComparator<String> idComparator) {
        if (STATION_MANUFACTURER_ID.equals(station.getManufacturerId())) {
            return new ArrayList<>();
        } else {
            return idComparator.getForDelete();
        }
    }

    public static boolean ignorePedelec(String pedelecManufacturerId) {
        return PEDELEC_MANUFACTURER_IDS.contains(pedelecManufacturerId);
    }

    public static boolean ignoreSlot(String stationManufacturerId, Integer slotPosition) {
        return STATION_MANUFACTURER_ID.equals(stationManufacturerId)
                && slotPosition != null
                && SLOT_POSITIONS.contains(slotPosition);
    }
}
