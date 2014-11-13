package de.rwth.idsg.bikeman.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "T_STATION_SLOT",
        indexes = {
                @Index(columnList="pedelec_id", unique = true),
                @Index(columnList="station_id", unique = false) })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@TableGenerator(name="slot_gen", initialValue=0, allocationSize=1)
@EqualsAndHashCode(of = {"stationSlotId", "manufacturerId"})
@ToString(includeFieldNames = true)
@Getter
@Setter
public class StationSlot implements Serializable {
    private static final long serialVersionUID = -1341816716937219400L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "slot_gen")
    @Column(name = "station_slot_id")
    private long stationSlotId;

    @Column(name = "station_slot_position")
    private Integer stationSlotPosition;

    @Column(name = "manufacturer_id", updatable = false)
    private String manufacturerId;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private OperationState state;

    @Column(name = "is_occupied")
    private Boolean isOccupied;

    @OneToOne
    @JoinColumn(name = "pedelec_id")
    private Pedelec pedelec;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "error_info")
    private String errorInfo;
}
