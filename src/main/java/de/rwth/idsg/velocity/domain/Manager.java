package de.rwth.idsg.velocity.domain;import de.rwth.idsg.velocity.domain.login.User;import javax.persistence.DiscriminatorValue;import javax.persistence.Entity;import javax.persistence.Table;import java.io.Serializable;@Entity@DiscriminatorValue("manager")@Table(name="T_MANAGER")public class Manager extends User implements Serializable {}