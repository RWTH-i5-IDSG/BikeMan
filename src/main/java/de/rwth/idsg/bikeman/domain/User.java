package de.rwth.idsg.bikeman.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

/**
 * A user.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type")
@Table(name = "T_USER",
        indexes = {
                @Index(columnList = "login", unique = true)})
@TableGenerator(name = "user_gen", initialValue = 0, allocationSize = 1)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@ToString(includeFieldNames = true)
@EqualsAndHashCode(callSuper = false, of = {"userId", "login"})
@Getter
@Setter
public class User extends AbstractTimestampClass implements Serializable {
    private static final long serialVersionUID = 9070882912934376688L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "user_gen")
    @Column(name = "user_id")
    private long userId;

    @NotNull
    //@Email removed because of bug (allowed for test@test-com)
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(?:[a-zA-Z]{2,6})$")
    @Size(min = 0, max = 100)
    @Column(name = "login", unique = true)
    private String login;

    @JsonIgnore
    @Size(min = 0, max = 100)
    private String password;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "T_USER_AUTHORITY",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "name", referencedColumnName = "name")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Authority> authorities;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PersistentToken> persistentTokens;

//    // TODO: Create ENUM for UserType?
//    @JsonIgnore
//    @Column(name = "user_type")
//    private String userType;
}
