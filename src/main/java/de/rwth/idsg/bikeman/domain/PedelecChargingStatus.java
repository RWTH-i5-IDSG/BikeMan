package de.rwth.idsg.bikeman.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 17.06.2015
 */
@Entity
@Table(name = "T_PEDELEC_CHARGING_STATUS")
@EqualsAndHashCode
@ToString(exclude = {"pedelec"})
@Getter
@Setter
public class PedelecChargingStatus implements Serializable {
    private static final long serialVersionUID = 6405839823967890558L;

    @Id
    @OneToOne
    @JoinColumn(name = "pedelec_id")
    private Pedelec pedelec;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private ChargingState state;

    @Column(name = "meter_value")
    private Double meterValue = 0.0;

    @Column(name = "battery_cycle_count")
    private Integer batteryCycleCount = 0;

    @Column(name = "battery_state_of_charge")
    private Double batteryStateOfCharge = 0.0;

    @Column(name = "battery_temperature")
    private Double batteryTemperature = 0.0;

    @Column(name = "battery_voltage")
    private Double batteryVoltage = 0.0;

    @Column(name = "battery_current")
    private Double batteryCurrent = 0.0;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime timestamp;

}
