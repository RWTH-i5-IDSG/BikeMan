package de.rwth.idsg.bikeman.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import javax.persistence.*;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 20.11.2014
 */
@Entity
@Table(name="T_RESERVATION")
@TableGenerator(name="reservation_gen", initialValue=0, allocationSize=1)
@EqualsAndHashCode(of = {"reservationId"})
@ToString(includeFieldNames = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "reservation_gen")
    @Column(name = "reservation_id")
    private long reservationId;

    @ManyToOne
    @JoinColumn(name = "card_account_id")
    private CardAccount cardAccount;

    @Column(name = "start_datetime", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime startDateTime;

    @Column(name = "end_datetime", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime endDateTime;

    @ManyToOne
    @JoinColumn(name = "pedelec_id")
    private Pedelec pedelec;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private ReservationState state = ReservationState.CREATED;
}
