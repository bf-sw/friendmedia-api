package org.product.api.domain.consultation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotEmpty;

public class ConsultationDto {

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @ApiModel("ConsultationDto-RegistryForm")
    public static class RegistryForm {
        @ApiModelProperty(value = "고객명", notes = "", example = "홍길동", required = true)
        @NotEmpty(message = "고객명을 입력해주세요.")
        private String name;

        @ApiModelProperty(value = "고객 연락처", notes = "", example = "01011112222", required = true)
        @NotEmpty(message = "고객 연락처를 입력해주세요.")
        private String phone;

        @ApiModelProperty(value = "주문번호", notes = "", example = "Z20200504738", required = true)
        @NotEmpty(message = "주문번호를 입력해주세요.")
        private String orderNo;

        @ApiModelProperty(value = "채널", notes = "", example = "", required = true)
        @NotEmpty(message = "채널을 입력해주세요.")
        private String channel;

        @ApiModelProperty(value = "인입유형", notes = "", example = "", required = true)
        @NotEmpty(message = "인입유형을 입력해주세요.")
        private String inType;

        @ApiModelProperty(value = "상담유형 품목", notes = "", example = "", required = true)
        @NotEmpty(message = "상담유형 품목을 입력해주세요.")
        private String consultType;

        @ApiModelProperty(value = "상담유형 대분류", notes = "", example = "", required = true)
        @NotEmpty(message = "상담유형 대분류를 입력해주세요.")
        private String level1;

        @ApiModelProperty(value = "상담유형 중분류", notes = "", example = "", required = true)
        @NotEmpty(message = "상담유형 중분류를 입력해주세요.")
        private String level2;

        @ApiModelProperty(value = "상담이력", notes = "", example = "", required = true)
        @NotEmpty(message = "상담이력을 입력해주세요.")
        private String content;

        @ApiModelProperty(value = "상담일자", notes = "", example = "", required = true)
        @NotEmpty(message = "상담일자를 입력해주세요.")
        private String consultDate;
    }
}
