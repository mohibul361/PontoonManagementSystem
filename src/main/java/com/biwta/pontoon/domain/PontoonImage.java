package com.biwta.pontoon.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author nasimkabir
 * ৬/২/২৪
 */
@Entity
@Data
public class PontoonImage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "pontoon_id")
    private Pontoon pontoon;

    private String imageUrl;
}
