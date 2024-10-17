package com.biwta.pontoon.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * @author nasimkabir
 * ৪/১২/২৩
 */
@Data
@Entity
public class Port {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String portName;

    @ManyToMany
    private List<Route> route;
}
