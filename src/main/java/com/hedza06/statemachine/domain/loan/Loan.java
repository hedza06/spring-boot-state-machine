package com.hedza06.statemachine.domain.loan;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "loan")
@DynamicInsert
@DynamicUpdate
public class Loan implements Serializable {

    @Serial
    private static final long serialVersionUID = 8888863597931035346L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private LoanState state;

    private String assignee;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "declined_by")
    private String declinedBy;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

}
