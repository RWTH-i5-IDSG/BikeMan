package de.rwth.idsg.bikeman.repository;

import de.rwth.idsg.bikeman.domain.Tariff;
import de.rwth.idsg.bikeman.domain.TariffType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Wolfgang Kluth on 19/01/15.
 */

public interface TariffRepository extends JpaRepository<Tariff, Long> {

    public Tariff findByName(TariffType name);

    @Query("select t.name from Tariff t")
    public List<TariffType> findAllNames();

}
