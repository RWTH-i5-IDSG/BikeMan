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
@Table(name = "T_ADDRESS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@TableGenerator(name="address_gen", initialValue=0, allocationSize=1)
@ToString(includeFieldNames = true)
@EqualsAndHashCode(of = {"addressId"})
@Getter
@Setter
public class Address implements Serializable {
    private static final long serialVersionUID = -1059619797439373147L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "address_gen")
    @Column(name = "address_id")
    private long addressId;

    @Column(name = "street_and_housenumber")
    private String streetAndHousenumber;

    @Column(name = "zip")
    private String zip;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

}
