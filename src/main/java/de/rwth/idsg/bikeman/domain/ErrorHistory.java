package de.rwth.idsg.bikeman.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Wolfgang Kluth on 19/02/16.
 */

@Entity
@Table(name = "T_ERROR_HISTORY")
@TableGenerator(name = "error_history_gen", initialValue = 0, allocationSize = 1)
@ToString(includeFieldNames = true)
@EqualsAndHashCode
@Getter
@Setter
public class ErrorHistory implements Serializable {
    private static final long serialVersionUID = -1059619797439373217L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "error_history_gen")
    @Column(name = "error_history_id")
    private long errorHistoryId;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "error_info")
    private String errorInfo;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "error_type")
    @Enumerated(EnumType.STRING)
    private ErrorType errorType;

    @Column(name = "manufacturer_id")
    private String manufacturerId;


    @PrePersist
    private void prePersist() {
        createdAt = new Date();
    }
}