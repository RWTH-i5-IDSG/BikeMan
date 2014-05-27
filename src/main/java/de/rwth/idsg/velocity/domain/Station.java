package de.rwth.idsg.velocity.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;


@Entity
@Table(name = "T_STATION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@TableGenerator(name="station_gen", initialValue=0, allocationSize=1)
@EqualsAndHashCode(of = {"stationId", "manufacturerId"})
@ToString(includeFieldNames = true)
public class Station implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "station_gen")
    @Column(name = "station_id")
    @Getter @Setter
    private long stationId;

    @Column(name = "manufacturer_id", updatable = false)
    @Getter @Setter
    private String manufacturerId;

    @Column(name = "name")
    @Getter @Setter
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    @Getter @Setter
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "station", orphanRemoval = true)
    @Getter @Setter
    private Set<StationSlot> stationSlots;

    @Column(name = "location_latitude", scale = 18, precision = 24)
    @Getter @Setter
    private BigDecimal locationLatitude;

    @Column(name = "location_longitude", scale = 18, precision = 24)
    @Getter @Setter
    private BigDecimal locationLongitude;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "note")
    @Getter @Setter
    private String note;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    @Getter @Setter
    private OperationState state;

}
