package de.rwth.idsg.bikeman.domain.ixsi;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 05.11.2014
 */
@Getter
@Setter
@EqualsAndHashCode(of = {"systemId"})
@ToString(includeFieldNames = true)
@Entity
@Table(name = "IXSI_CLIENT_SYSTEM",
    indexes = {
        @Index(columnList="ip_address", unique = true) })
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class IxsiClientSystem implements Serializable {
    private static final long serialVersionUID = -5147323593181333685L;

    /**
     * As defined by the SystemIDType of IXSI
     */
    @Id
    @Column(name = "system_id", nullable = false)
    private String systemId;

    @Column(name = "ip_address", nullable = false, unique = true)
    private String ipAddress;

}
