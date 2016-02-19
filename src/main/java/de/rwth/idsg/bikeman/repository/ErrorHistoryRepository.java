package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.ErrorHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Wolfgang Kluth on 19/02/16.
 */
public interface ErrorHistoryRepository extends JpaRepository<ErrorHistory, Long> {

    @Query("select eh from ErrorHistory eh order by eh.createdAt desc")
    List<ErrorHistory> findAllOrderByCreatedAt();

}
