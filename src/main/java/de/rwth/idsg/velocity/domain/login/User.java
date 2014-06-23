package de.rwth.idsg.velocity.domain.login;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

/**
 * A user.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type")
@Table(name="T_USER",
        indexes = {
                @Index(columnList="login", unique = true)})
@TableGenerator(name="user_gen", initialValue=0, allocationSize=1)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@ToString(includeFieldNames = true)
@EqualsAndHashCode(of = {"userId"})
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "user_gen")
    @Column(name = "user_id")
    @Getter @Setter
    private long userId;

    @NotNull
    @Email
    @Size(min = 0, max = 100)
    @Getter @Setter
    private String login;

    @JsonIgnore
    @Size(min = 0, max = 100)
    @Getter @Setter
    private String password;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "T_USER_AUTHORITY",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "name", referencedColumnName = "name")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @Getter @Setter
    private Set<Authority> authorities;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @Getter @Setter
    private Set<PersistentToken> persistentTokens;

}
