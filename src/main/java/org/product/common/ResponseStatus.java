package org.product.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor  
public enum ResponseStatus {
    SUCCESS(200, "Success", true),
    NONE_AUTH(401, "호출 권한이 없습니다.", true),
    DATA_NOT_FOUND(404, "조회된 데이터가 없습니다.", true),
    ERROR(500, "API 서버 장애", true);

    @Getter 
    private long status;

    @Getter 
    private String message;

    @Getter 
    private boolean active;
}
