package de.rwth.idsg.bikeman.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import java.io.Serializable;

/**
 * @author Sevket Goekay <goekay@dbis.rwth-aachen.de>
 * @since 28.07.2015
 */
@Entity
@Table(name = "T_CARD_KEY")
@TableGenerator(name = "card_key_gen", initialValue = 0, allocationSize = 1)
@EqualsAndHashCode(callSuper = false, of = {"cardKeyId", "name"})
@ToString(includeFieldNames = true, exclude = {"readKey", "writeKey", "applicationKey", "initialApplicationKey"})
@Getter
@Setter
public class CardKey extends AbstractTimestampClass implements Serializable {
    private static final long serialVersionUID = -2101600110715023189L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "card_key_gen")
    @Column(name = "card_key_id")
    private Long cardKeyId;

    @Column(name = "name")
    private String name;

    @Column(name = "read_key")
    private String readKey;

    @Column(name = "write_key")
    private String writeKey;

    @Column(name = "application_key")
    private String applicationKey;

    @Column(name = "initial_application_key")
    private String initialApplicationKey;

    @PrePersist
    public void prePersist() {
        super.prePersist();
    }
}
