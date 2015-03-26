package de.rwth.idsg.bikeman.domain;

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
@Getter
@Setter
public class Authority implements Serializable {
    private static final long serialVersionUID = -9117880235041725893L;

    @NotNull
    @Size(min = 0, max = 50)
    @Id
    private String name;

    public Authority() {
    }

}
