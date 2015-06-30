package de.rwth.idsg.bikeman.domain;

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
@Table(name = "T_STATION",
        indexes = {
                @Index(columnList="address_id", unique = true) })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@TableGenerator(name="station_gen", initialValue=0, allocationSize=1)
@EqualsAndHashCode(callSuper = false, of = {"stationId", "manufacturerId"})
@ToString(includeFieldNames = true, exclude = {"stationSlots"})
@Getter
@Setter
public class Station extends AbstractTimestampClass implements Serializable {
    private static final long serialVersionUID = 4263316848138899690L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "station_gen")
    @Column(name = "station_id")
    private long stationId;

    @Column(name = "manufacturer_id", updatable = false)
    private String manufacturerId;

    @Column(name = "name")
    private String name;

    @Column(name = "endpoint_address")
    private String endpointAddress;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "station", orphanRemoval = true)
    private Set<StationSlot> stationSlots;

    @Column(name = "location_latitude", scale = 18, precision = 24)
    private BigDecimal locationLatitude;

    @Column(name = "location_longitude", scale = 18, precision = 24)
    private BigDecimal locationLongitude;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "note")
    private String note;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private OperationState state = OperationState.INOPERATIVE;

    @Column(name = "firmware_version")
    private String firmwareVersion;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "error_info")
    private String errorInfo;
}
