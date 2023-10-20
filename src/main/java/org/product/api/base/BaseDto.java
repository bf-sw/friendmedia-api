package org.product.api.base;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;
import java.util.Objects;

public abstract class BaseDto {

    protected boolean isNull(Object obj) { return Objects.isNull(obj); }

    protected boolean isNotNull(Object obj) { return !Objects.isNull(obj); }

    protected boolean isEmpty(Object obj) { return StringUtils.isEmpty(obj); }

    protected boolean isNotEmpty(Object obj) { return !StringUtils.isEmpty(obj); }

    protected String likeRight(String str) {
        return str + "%";
    }

    protected String likeLeft(String str) {
        return "%" + str;
    }

    protected String likeBoth(String str) {
        return "%" + str + "%";
    }

}
