package org.product.api.domain.consultation;

import com.querydsl.core.BooleanBuilder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.product.api.base.BaseDto;
import org.product.api.code.LoginType;
import org.product.common.DateUtils;

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

        @ApiModelProperty(value = "상담일자", notes = "", example = "2023-10-01", required = true)
        @NotEmpty(message = "상담일자를 입력해주세요.")
        private String consultDate;
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @ApiModel("ConsultationDto-UpdateForm")
    public static class UpdateForm extends RegistryForm {
        private long id;
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @ApiModel("ConsultationDto-DeleteForm")
    public static class DeleteForm {
        private long id;
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @ApiModel("ConsultationDto-SearchForm")
    public static class SearchForm extends BaseDto {
        @ApiModelProperty(value = "상담일자 시작일")
        String startDate;

        @ApiModelProperty(value = "상담일자 종료일")
        String endDate;

        @ApiModelProperty(value = "상담일자 시작일", hidden = true)
        long startDateTime;

        @ApiModelProperty(value = "상담일자 종료일", hidden = true)
        long endDateTime;

        @ApiModelProperty(value = "고객명")
        String name;

        @ApiModelProperty(value = "고객 휴대전화번호")
        String phone;

        @ApiModelProperty(value = "주문번호")
        String orderNo;

        @ApiModelProperty(value = "채널")
        String channel;

        @ApiModelProperty(value = "인입유형")
        String inType;

        @ApiModelProperty(value = "상담유형 품목")
        String consultType;

        @ApiModelProperty(value = "상담유형 대분류")
        String level1;

        @ApiModelProperty(value = "상담유형 중분류")
        String level2;

        @ApiModelProperty(value = "삭제여부")
        boolean deleted = false;

        public BooleanBuilder getCondition() {
            QConsultation consultation = QConsultation.consultation;
            BooleanBuilder where = new BooleanBuilder();

            if (isNotNull(startDate) && isNotNull(endDate)) {
                startDateTime = DateUtils.parseMinEpochSecond(startDate.replaceAll("-", ""));
                endDateTime = DateUtils.parseMaxEpochSecond(endDate.replaceAll("-", ""));
                where.and(consultation.consultDate.goe(startDateTime)
                        .and(consultation.consultDate.loe(endDateTime)));
            } else if (isNotNull(startDate) && isNull(endDate)) {
                startDateTime = DateUtils.parseMinEpochSecond(startDate.replaceAll("-", ""));
                where.and(consultation.consultDate.goe(startDateTime));
            } else if (isNull(startDate) && isNotNull(endDate)) {
                endDateTime = DateUtils.parseMaxEpochSecond(endDate.replaceAll("-", ""));
                where.and(consultation.consultDate.loe(endDateTime));
            }

            if (isNotEmpty(name)) where.and(consultation.name.contains(name));

            if (isNotEmpty(phone)) where.and(consultation.phone.contains(phone));

            if (isNotEmpty(orderNo)) where.and(consultation.orderNo.contains(orderNo));

            if (isNotEmpty(channel)) where.and(consultation.channel.eq(channel));

            if (isNotEmpty(inType)) where.and(consultation.inType.eq(inType));

            if (isNotEmpty(consultType)) where.and(consultation.consultType.eq(consultType));

            if (isNotEmpty(level1)) where.and(consultation.level1.eq(level1));

            if (isNotEmpty(level2)) where.and(consultation.level2.eq(level2));

            where.and(consultation.deleted.eq(deleted));
            return where;
        }
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @ApiModel("ConsultationDto-BasicInfo")
    public static class BasicInfo {

        @ApiModelProperty(value = "ID", notes = "", example = "")
        private long id;

        @ApiModelProperty(value = "고객명", notes = "", example = "홍길동")
        private String name;

        @ApiModelProperty(value = "고객 연락처", notes = "", example = "01011112222")
        private String phone;

        @ApiModelProperty(value = "주문번호", notes = "", example = "Z20200504738")
        private String orderNo;

        @ApiModelProperty(value = "채널", notes = "", example = "")
        private String channel;

        @ApiModelProperty(value = "인입유형", notes = "", example = "")
        private String inType;

        @ApiModelProperty(value = "상담유형 품목", notes = "", example = "")
        private String consultType;

        @ApiModelProperty(value = "상담유형 대분류", notes = "", example = "")
        private String level1;

        @ApiModelProperty(value = "상담유형 중분류", notes = "", example = "")
        private String level2;

        @ApiModelProperty(value = "상담이력", notes = "", example = "")
        private String content;

        @ApiModelProperty(value = "상담일자", notes = "", example = "")
        private String consultDate;

        @ApiModelProperty(value = "접수부서", notes = "", example = "")
        private String department;

        @ApiModelProperty(value = "상담사 로그인 유형", notes = "", example = "")
        private LoginType counselorType;

        @ApiModelProperty(value = "상담사 아이디", notes = "", example = "")
        private String counselorId;

        @ApiModelProperty(value = "상담사 명", notes = "", example = "")
        private String counselorNm;

        @ApiModelProperty(value = "등록일시", notes = "", example = "")
        private Long createdAt;

        @ApiModelProperty(value = "등록자", notes = "", example = "")
        private String createdBy;

        @ApiModelProperty(value = "수정일시", notes = "", example = "")
        private Long modifiedAt;

        @ApiModelProperty(value = "수정자", notes = "", example = "")
        private String modifiedBy;
    }
}
