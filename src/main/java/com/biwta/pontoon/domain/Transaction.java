package com.biwta.pontoon.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
@Data
@Entity
@Table(name = "TRANSACTIONS")
public class Transaction {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "TRANSACTIONS_SEQ", sequenceName = "TRANSACTIONS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRANSACTIONS_SEQ")
    private long id;

    @Column(name = "DESCIPTION", nullable = false)
    private String description;

    @Column(name = "ADD_DATE", nullable = false)
    private LocalDateTime addDate;

    @Column(name = "ADD_USER", length = 50, nullable = false)
    private String addUser;

    @Column(name = "ADD_TERM", length = 50, nullable = false)
    private String addTerm;

    @Column(name = "ADD_IP", length = 32, nullable = false)
    private String addIp;

    @Column(name = "MOD_USER", length = 50)
    private String modUser;

    @Column(name = "MOD_TERM", length = 50)
    private String modTerm;

    @Column(name = "MOD_IP", length = 32)
    private String modIp;

    @Column(name = "MOD_DATE")
    private LocalDateTime modDate;

}
