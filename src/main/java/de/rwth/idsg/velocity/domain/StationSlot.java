package de.rwth.idsg.velocity.domain;

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
@EqualsAndHashCode(of = {"stationSlotId"})
@ToString(includeFieldNames = true)
public class StationSlot implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "slot_gen")
    @Column(name = "station_slot_id")
    @Getter @Setter
    private long stationSlotId;

    @Column(name = "station_slot_position")
    @Getter @Setter
    private Integer stationSlotPosition;

    @Column(name = "manufacturer_id", updatable = false)
    @Getter @Setter
    private String manufacturerId;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    @Getter @Setter
    private OperationState state;

    @Column(name = "is_occupied")
    @Getter @Setter
    private Boolean isOccupied;

    @OneToOne
    @JoinColumn(name = "pedelec_id")
    @Getter @Setter
    private Pedelec pedelec;

    @ManyToOne
    @JoinColumn(name = "station_id")
    @Getter @Setter
    private Station station;
}
