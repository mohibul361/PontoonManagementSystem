package com.biwta.pontoon.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author nasimkabir
 * ৫/২/২৪
 */
@Data
@Entity
@Table(name = "procuring_entry")
public class ProcuringEntry extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false,name = "procuring_name")
    private String procuringName;

    private Boolean isActive;

    private String description;
}
