package org.product.api.domain.consultation;

import lombok.extern.slf4j.Slf4j;
import org.product.api.base.BaseService;
import org.product.common.DateUtils;
import org.product.common.ResponseStatus;
import org.product.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
            checkSecretKey();

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
                    .setConsultDate(form.getConsultDate())
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
}
