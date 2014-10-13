package de.rwth.idsg.bikeman.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 13.10.2014
 */
@MappedSuperclass
public abstract class AbstractTimestampClass {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", nullable = false, updatable = false)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated", nullable = false, updatable = true)
    private Date updated;

    @PrePersist
    protected void prePersist() {
        updated = created = new Date();
    }

    @PreUpdate
    protected void preUpdate() {
        updated = new Date();
    }
}