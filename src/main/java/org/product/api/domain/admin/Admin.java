package org.product.api.domain.admin;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;

@Slf4j
@Getter
@Setter
@Accessors(chain = true)
@DynamicInsert
@DynamicUpdate
@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String counselorId;

    @Column
    private String counselorNm;

    @Column
    private boolean deleted;
}
