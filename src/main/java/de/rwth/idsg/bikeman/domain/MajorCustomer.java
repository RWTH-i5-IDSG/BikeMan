package de.rwth.idsg.bikeman.domain;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

/**
 * Created by swam on 09/10/14.
 */
@Entity
@DiscriminatorValue("majorCustomer")
@Table(name="T_MAJOR_CUSTOMER")
public class MajorCustomer extends Manager {
    private static final long serialVersionUID = 1724868961860110834L;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    private Set<CardAccount> cardAccounts;

}