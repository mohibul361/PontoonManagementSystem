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
@Table(name = "pontoon_type")
public class PontoonType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String typeName;

    @Column(nullable = false, length = 20)
    private String description;
}
