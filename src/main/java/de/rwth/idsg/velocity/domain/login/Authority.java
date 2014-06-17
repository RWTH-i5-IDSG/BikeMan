package de.rwth.idsg.velocity.domain.login;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * An authority (a security role) used by Spring Security.
 */
@Entity
@Table(name = "T_AUTHORITY")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@ToString(includeFieldNames = true)
@EqualsAndHashCode(of = {"name"})
@AllArgsConstructor
public class Authority implements Serializable {

    @NotNull
    @Size(min = 0, max = 50)
    @Id
    @Getter @Setter
    private String name;

    public Authority() {}

}
