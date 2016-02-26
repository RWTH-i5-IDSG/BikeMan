package de.rwth.idsg.bikeman.repository.helper;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by swam on 05/08/15.
 */
public class JpaHelper {

    public static <T> T getSingleResult(TypedQuery<T> query) {
        query.setMaxResults(1);
        List<T> list = query.getResultList();
        if (list == null || list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }
    
}
