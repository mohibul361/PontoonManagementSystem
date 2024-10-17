package com.biwta.pontoon.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
public class LoshkorTransferHistory extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private Employee employee;
    private List<Pontoon> pontoon;
    private Date effectiveDate;
    private Date tillDate;
}
