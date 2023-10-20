package org.product.api.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor  
public enum LoginType {

    GW("그룹웨어", true),
    IS("임시계정", true);

    @Getter
    private String desc;

    @Getter
    private boolean active;
}
