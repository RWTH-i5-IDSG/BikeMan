package de.rwth.idsg.bikeman.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "T_ACTIVATION_KEY")
@TableGenerator(name = "activation_key_gen", initialValue = 0, allocationSize = 1)
@ToString(includeFieldNames = true)
@Getter
@Setter
public class ActivationKey {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "activation_key_gen")
    @Column(name = "activation_key_id")
    private Long activationKeyId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Customer customer;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Column(name = "key")
    private String key;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ActivationKeyType type;

    @Column(name = "used")
    private Boolean used = false;

    @Column(name = "valid_until")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime validUntil;

    @PrePersist
    protected void prePersist() {
        this.createdAt = new Date();
    }
}
