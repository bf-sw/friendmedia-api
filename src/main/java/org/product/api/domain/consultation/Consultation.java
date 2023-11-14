package org.product.api.domain.consultation;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.product.api.code.LoginType;

import javax.persistence.*;

@Slf4j
@Getter
@Setter
@Accessors(chain = true)
@DynamicInsert
@DynamicUpdate
@Entity
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column(name="phone")
    @ColumnTransformer(
            read="FUNC_DEC(phone)",
            write="FUNC_ENC(?)"
    )
    private String phone;

    @Column
    private String orderNo;

    @Column
    private String channel;

    @Column
    private String inType;

    @Column
    private String consultType;

    @Column
    private String level1;

    @Column
    private String level2;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private Long consultDate;

    @Column
    private String department;

    @Column
    @Enumerated(EnumType.STRING)
    private LoginType counselorType;

    @Column
    private String counselorId;

    @Column
    private String counselorNm;

    @Column
    private boolean complaint;

    @Column
    private String consultStatus;

    @Column
    private Long createdAt;

    @Column
    private String createdBy;

    @Column
    private Long modifiedAt;

    @Column
    private String modifiedBy;

    @Column
    private Long deletedAt;

    @Column
    private String deletedBy;

    @Column
    private boolean deleted;
}
