package de.rwth.idsg.bikeman.domain.ixsi;

import de.rwth.idsg.bikeman.domain.CardAccount;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by max on 22/10/14.
 */
@Getter
@Setter
@EqualsAndHashCode(of = {"tokenId"})
@ToString(includeFieldNames = true, exclude = {})
@Entity
@Table(name = "IXSI_TOKEN")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@TableGenerator(name="ixsi_token_gen", initialValue=0, allocationSize=1)
public class IxsiToken implements Serializable {
    private static final long serialVersionUID = 621155378340926282L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ixsi_token_gen")
    @Column(name = "token_id", nullable = false)
    private Long tokenId;

    @Column(name = "token_value")
    private String tokenValue;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "card_id")
    private CardAccount cardAccount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", nullable = false, updatable = true)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_used", nullable = true, updatable = true)
    private Date lastUsed;

    @PrePersist
    protected void prePersist() {
        created = new Date();
        lastUsed = null;
    }
}