package org.product.api.domain.consultation;

import lombok.extern.slf4j.Slf4j;
import org.product.api.base.BaseService;
import org.product.common.DateUtils;
import org.product.common.ResponseStatus;
import org.product.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class ConsultationService extends BaseService {

    @Autowired
    ConsultationRepository consultationRepository;

    /*
     * 상담이력 등록
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public void registConsultation(ConsultationDto.RegistryForm form) {

        try {
            log.info("[CONSULTATION][SERVICE][ConsultationService][registConsultation][form]", form.toString());

            Consultation consultation = new Consultation();
            consultation
                    .setName(form.getName())
                    .setPhone(form.getPhone())
                    .setOrderNo(form.getOrderNo())
                    .setChannel(form.getChannel())
                    .setInType(form.getInType())
                    .setConsultType(form.getConsultType())
                    .setLevel1(form.getLevel1())
                    .setLevel2(form.getLevel2())
                    .setContent(form.getContent())
                    .setConsultDate(DateUtils.parseToEpochSecond(form.getConsultDate().replaceAll("-", "")))
                    .setDepartment(getDepartment())
                    .setCounselorType(getLoginType())
                    .setCounselorId(getLoginId())
                    .setCounselorNm(getName())
                    .setCreatedAt(DateUtils.getNow())
                    .setCreatedBy(getName())
                    .setDeleted(false);

            consultationRepository.save(consultation);

        } catch (ApiException e) {
            log.error("[CONSULTATION][SERVICE][ConsultationService][registConsultation][ERROR]");
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            log.error("[CONSULTATION][SERVICE][ConsultationService][registConsultation][ERROR]");
            e.printStackTrace();
            throw new ApiException(ResponseStatus.ERROR);
        }
    }

    /*
     * 상담이력 수정
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateConsultation(ConsultationDto.UpdateForm form) {

        try {
            log.info("[CONSULTATION][SERVICE][ConsultationService][updateConsultation][form]", form.toString());

            Optional<Consultation> consultationOptional = consultationRepository.findById(form.getId());

            if (Objects.isNull(consultationOptional)) {
                throw new ApiException("조회 가능한 상담이력이 없습니다.");
            }

            Consultation consultation = consultationOptional.get();

            if (consultation.isDeleted()) {
                throw new ApiException("이미 삭제된 상담이력입니다.");
            }

            consultation
                    .setName(form.getName())
                    .setPhone(form.getPhone())
                    .setOrderNo(form.getOrderNo())
                    .setChannel(form.getChannel())
                    .setInType(form.getInType())
                    .setConsultType(form.getConsultType())
                    .setLevel1(form.getLevel1())
                    .setLevel2(form.getLevel2())
                    .setContent(form.getContent())
                    .setConsultDate(DateUtils.parseToEpochSecond(form.getConsultDate().replaceAll("-", "")))
                    .setDepartment(getDepartment())
                    .setCounselorType(getLoginType())
                    .setCounselorId(getLoginId())
                    .setCounselorNm(getName())
                    .setModifiedAt(DateUtils.getNow())
                    .setModifiedBy(getName());

            consultationRepository.save(consultation);

        } catch (ApiException e) {
            log.error("[CONSULTATION][SERVICE][ConsultationService][updateConsultation][ERROR]");
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            log.error("[CONSULTATION][SERVICE][ConsultationService][updateConsultation][ERROR]");
            e.printStackTrace();
            throw new ApiException(ResponseStatus.ERROR);
        }
    }

    public Page<ConsultationDto.BasicInfo> search(ConsultationDto.SearchForm condition, PageRequest pageable) {

        try {
            Page<Consultation> goods = consultationRepository.findAll(condition.getCondition(), pageable);
            return goods.map(this::toBasicInfo);
        } catch (ApiException e) {
            log.error("[CONSULTATION][SERVICE][ConsultationService][search][ERROR]");
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            log.error("[CONSULTATION][SERVICE][ConsultationService][search][ERROR]");
            e.printStackTrace();
            throw new ApiException(ResponseStatus.ERROR);
        }
    }

    private ConsultationDto.BasicInfo toBasicInfo(Consultation consultation) {

        ConsultationDto.BasicInfo basicInfo = new ConsultationDto.BasicInfo();

        basicInfo
                .setId(consultation.getId())
                .setName(consultation.getName())
                .setPhone(consultation.getPhone())
                .setOrderNo(consultation.getOrderNo())
                .setChannel(consultation.getChannel())
                .setInType(consultation.getInType())
                .setConsultType(consultation.getConsultType())
                .setLevel1(consultation.getLevel1())
                .setLevel2(consultation.getLevel2())
                .setContent(consultation.getContent())
                .setConsultDate(DateUtils.parseDateWithDash(consultation.getConsultDate()))
                .setCounselorType(consultation.getCounselorType())
                .setDepartment(consultation.getDepartment())
                .setCounselorId(consultation.getCounselorId())
                .setCounselorNm(consultation.getCounselorNm())
                .setCreatedAt(consultation.getCreatedAt())
                .setCreatedBy(consultation.getCreatedBy())
                .setModifiedAt(consultation.getModifiedAt())
                .setModifiedBy(consultation.getModifiedBy());

        return basicInfo;
    }
}
