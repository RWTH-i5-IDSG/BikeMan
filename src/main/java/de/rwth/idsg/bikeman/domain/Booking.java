package de.rwth.idsg.bikeman.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 13.11.2014
 */
@Entity
@Table(name="T_BOOKING")
@TableGenerator(name="booking_gen", initialValue=0, allocationSize=1)
@EqualsAndHashCode(of = {"bookingId"})
@ToString(includeFieldNames = true)
@Getter
@Setter
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "booking_gen")
    @Column(name = "booking_id")
    private long bookingId;

    @Column(name = "ixsi_booking_id")
    private String ixsiBookingId;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @OneToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

}
