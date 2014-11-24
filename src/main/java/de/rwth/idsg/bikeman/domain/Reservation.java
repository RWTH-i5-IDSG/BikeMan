package de.rwth.idsg.bikeman.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Builder;
import org.joda.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 20.11.2014
 */
@Entity
@Builder
@Table(name="T_RESERVATION")
@TableGenerator(name="reservation_gen", initialValue=0, allocationSize=1)
@EqualsAndHashCode(of = {"reservationId"})
@ToString(includeFieldNames = true)
@Getter
@Setter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "reservation_gen")
    @Column(name = "reservation_id")
    private long reservationId;

    @ManyToOne
    @JoinColumn(name = "card_account_id")
    private CardAccount cardAccount;

    @Column(name = "start_datetime", nullable = false, updatable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_datetime", nullable = false, updatable = false)
    private LocalDateTime endDateTime;

    @ManyToOne
    @JoinColumn(name = "pedelec_id")
    private Pedelec pedelec;
}
