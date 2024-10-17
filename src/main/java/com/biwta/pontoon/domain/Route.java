package com.biwta.pontoon.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
@Entity
@Table
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Route extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String routeName;

    @ManyToOne(fetch = FetchType.EAGER)
    private PontoonSection section;

    @Column(nullable = false)
    private Boolean isActive;

    @ManyToMany
    private List<Port> port;

}
