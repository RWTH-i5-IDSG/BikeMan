package de.rwth.idsg.bikeman.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Wolfgang Kluth on 16/01/15.
 */

@Entity
@Table(name = "T_TARIFF")
@TableGenerator(name = "tariff_gen", initialValue = 0, allocationSize = 1)
@EqualsAndHashCode(of = {"tariffId"})
@ToString(includeFieldNames = true)
@Getter
@Setter

public class Tariff {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tariff_gen")
    @Column(name = "tariff_id")
    private Long tariffId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tariff_type")
    private TariffType name;

    @Enumerated(EnumType.STRING)
    @Column(name = "tariff_category")
    private TariffCategory category;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "description")
    private String description;

    @Column(name = "term")
    private Integer term;

    @Column(name = "periodic_rate")
    private BigDecimal periodicRate;

}
