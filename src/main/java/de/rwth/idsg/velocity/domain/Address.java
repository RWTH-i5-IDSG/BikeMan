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
@Table(name = "T_ADDRESS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@TableGenerator(name="address_gen", initialValue=0, allocationSize=1)
@ToString(includeFieldNames = true)
@EqualsAndHashCode(of = {"addressId"})
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "address_gen")
    @Column(name = "address_id")
    @Getter @Setter
    private long addressId;

    @Column(name = "street_and_housenumber")
    @Getter @Setter
    private String streetAndHousenumber;

    @Column(name = "zip")
    @Getter @Setter
    private String zip;

    @Column(name = "city")
    @Getter @Setter
    private String city;

    @Column(name = "country")
    @Getter @Setter
    private String country;

}
