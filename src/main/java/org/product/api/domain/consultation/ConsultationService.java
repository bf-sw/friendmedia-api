package org.product.api.domain.consultation;

import lombok.extern.slf4j.Slf4j;
import org.product.api.base.BaseService;
import org.product.api.domain.admin.Admin;
import org.product.api.domain.admin.AdminRepository;
import org.product.common.DateUtils;
import org.product.common.ResponseStatus;
import org.product.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ConsultationService extends BaseService {

    @Autowired
    ConsultationRepository consultationRepository;

    @Autowired
    AdminRepository adminRepository;

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
                    .setComplaint(form.getComplaint())
                    .setConsultStatus(form.getConsultStatus())
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

            if (!consultationOptional.isPresent()) {
                throw new ApiException("수정 가능한 상담이력이 없습니다.");
            }

            Consultation consultation = consultationOptional.get();

            if (consultation.isDeleted()) {
                throw new ApiException("이미 삭제된 상담이력입니다.");
            }

            long minTime = DateUtils.fromLocalDateMin(DateUtils.getNowForLocalDate());
            long createTime = consultation.getCreatedAt();

            Optional<Admin> admin = adminRepository.findFirstByCounselorIdAndDeletedFalse(getLoginId());

            // 상담이력은 본인이 작성한 상담이력만 수정 가능
            // 당일 등록된 상담이력만 수정 가능 (해당기능 삭제 요청으로 주석처리)
            // 관리자 수정 가능하도록 기능 수정
            if (!consultation.getCounselorId().equals(getLoginId()) && !admin.isPresent()) {
            //if (!consultation.getCounselorId().equals(getLoginId()) || createTime < minTime) {
                throw new ApiException("상담이력은 관리자 또는 본인이 작성한 상담이력만 수정 가능합니다.");
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
                    .setComplaint(form.getComplaint())
                    .setConsultStatus(form.getConsultStatus())
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

    /*
     * 상담이력 멀티 수정
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateMultiConsultation(ConsultationDto.UpdateMultiForm form) {

        try {
            log.info("[CONSULTATION][SERVICE][ConsultationService][updateMultiConsultation][form]", form.toString());

            List<String> ids = form.getIds();

            ids.stream().forEach(id -> {
                Optional<Consultation> consultationOptional = consultationRepository.findById(Long.valueOf(id));

                if (!consultationOptional.isPresent()) {
                    throw new ApiException("수정 가능한 상담이력이 없습니다.");
                }

                Consultation consultation = consultationOptional.get();

                if (consultation.isDeleted()) {
                    throw new ApiException("이미 삭제된 상담이력입니다.");
                }

                long minTime = DateUtils.fromLocalDateMin(DateUtils.getNowForLocalDate());
                long createTime = consultation.getCreatedAt();

                Optional<Admin> admin = adminRepository.findFirstByCounselorIdAndDeletedFalse(getLoginId());

                // 상담이력은 본인이 작성한 상담이력만 수정 가능
                // 당일 등록된 상담이력만 수정 가능 (해당기능 삭제 요청으로 주석처리)
                // 관리자 수정 가능하도록 기능 수정
                if (!consultation.getCounselorId().equals(getLoginId()) && !admin.isPresent()) {
                    //if (!consultation.getCounselorId().equals(getLoginId()) || createTime < minTime) {
                    throw new ApiException("상담이력은 관리자 또는 본인이 작성한 상담이력만 수정 가능합니다.");
                }

                consultation
                        .setChannel(form.getChannel())
                        .setInType(form.getInType())
                        .setConsultType(form.getConsultType())
                        .setLevel1(form.getLevel1())
                        .setLevel2(form.getLevel2())
                        .setCounselorType(getLoginType())
                        .setConsultStatus(form.getConsultStatus())
                        .setModifiedAt(DateUtils.getNow())
                        .setModifiedBy(getName());

                consultationRepository.save(consultation);
            });
        } catch (ApiException e) {
            log.error("[CONSULTATION][SERVICE][ConsultationService][updateMultiConsultation][ERROR]");
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            log.error("[CONSULTATION][SERVICE][ConsultationService][updateMultiConsultation][ERROR]");
            e.printStackTrace();
            throw new ApiException(ResponseStatus.ERROR);
        }
    }

    /*
     * 상담이력 삭제
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteConsultation(ConsultationDto.DeleteForm form) {

        try {
            log.info("[CONSULTATION][SERVICE][ConsultationService][deleteConsultation][form] {}", form.toString());

            Optional<Consultation> consultationOptional = consultationRepository.findById(form.getId());

            if (!consultationOptional.isPresent()) {
                throw new ApiException("삭제 가능한 상담이력이 없습니다.");
            }

            Consultation consultation = consultationOptional.get();

            if (consultation.isDeleted()) {
                throw new ApiException("이미 삭제된 상담이력입니다.");
            }

            Optional<Admin> admin = adminRepository.findFirstByCounselorIdAndDeletedFalse(getLoginId());

            // 관리자로 등록된 상담사만 상담이력 삭제 가능
            if (!admin.isPresent()) {
                throw new ApiException("삭제 권한이 없습니다. 관리자에게 문의하시기 바랍니다.");
            }

            consultation
                    .setDeletedAt(DateUtils.getNow())
                    .setDeletedBy(getName())
                    .setDeleted(true);

            consultationRepository.save(consultation);

        } catch (ApiException e) {
            log.error("[CONSULTATION][SERVICE][ConsultationService][deleteConsultation][ERROR]");
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            log.error("[CONSULTATION][SERVICE][ConsultationService][deleteConsultation][ERROR]");
            e.printStackTrace();
            throw new ApiException(ResponseStatus.ERROR);
        }
    }

    /*
     * 여러개 상담이력 삭제
     * */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteMultiConsultation(ConsultationDto.DeleteMultiForm forms) {

        try {
            log.info("[CONSULTATION][SERVICE][ConsultationService][deleteMultiConsultation][forms] {}", forms.toString());

            Optional<Admin> admin = adminRepository.findFirstByCounselorIdAndDeletedFalse(getLoginId());

            // 관리자로 등록된 상담사만 상담이력 삭제 가능
            if (!admin.isPresent()) {
                throw new ApiException("삭제 권한이 없습니다. 관리자에게 문의하시기 바랍니다.");
            }

            forms.getIds().stream().forEach(form -> {
                long counselorId = form.getId();
                Optional<Consultation> consultationOptional = consultationRepository.findById(counselorId);

                if (!consultationOptional.isPresent()) {
                    throw new ApiException("삭제 가능한 상담이력이 없습니다.");
                }

                Consultation consultation = consultationOptional.get();

                if (consultation.isDeleted()) {
                    throw new ApiException("이미 삭제된 상담이력입니다.");
                }

                consultation
                        .setDeletedAt(DateUtils.getNow())
                        .setDeletedBy(getName())
                        .setDeleted(true);

                consultationRepository.save(consultation);
            });
        } catch (ApiException e) {
            log.error("[CONSULTATION][SERVICE][ConsultationService][deleteMultiConsultation][ERROR]");
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            log.error("[CONSULTATION][SERVICE][ConsultationService][deleteMultiConsultation][ERROR]");
            e.printStackTrace();
            throw new ApiException(ResponseStatus.ERROR);
        }
    }

    /*
     * 상담이력 목록 조회
     * */
    public Page<ConsultationDto.BasicInfo> search(ConsultationDto.SearchForm condition, PageRequest pageable) {

        try {
            Page<Consultation> consultations = consultationRepository.findAll(condition.getCondition(), pageable);
            return consultations.map(this::toBasicInfo);
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

    /*
     * 상담이력 상세 조회
     * */
    public ConsultationDto.BasicInfo detail(long consultationId) {

        try {
            Optional<Consultation> consultationOptional = consultationRepository.findById(consultationId);

            if (!consultationOptional.isPresent()) {
                throw new ApiException("조회 가능한 상담이력이 없습니다.");
            }

            Consultation consultation = consultationOptional.get();

            if (consultation.isDeleted()) {
                throw new ApiException("이미 삭제된 상담이력입니다.");
            }

            return toBasicInfo(consultation);
        } catch (ApiException e) {
            log.error("[CONSULTATION][SERVICE][ConsultationService][detail][ERROR]");
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            log.error("[CONSULTATION][SERVICE][ConsultationService][detail][ERROR]");
            e.printStackTrace();
            throw new ApiException(ResponseStatus.ERROR);
        }
    }

    /*
     * 엑셀 생성용 상담이력 목록 조회
     * */
    public List<ConsultationDto.BasicInfo> excel(ConsultationDto.SearchForm form, Pageable wholePage) {

        try {
            log.info("[CONSULTATION][SERVICE][ConsultationService][excel][getLoginId] {}", getLoginId());
            log.info("[CONSULTATION][SERVICE][ConsultationService][excel][getName] {}", getName());
            log.info("[CONSULTATION][SERVICE][ConsultationService][excel][form] {}", form.toString());

            Page<Consultation> consultations = consultationRepository.findAll(form.getCondition(), wholePage);
            return consultations.getContent().stream().map(this::toBasicInfo).sorted(Comparator.comparing(ConsultationDto.BasicInfo::getCreatedAt)).collect(Collectors.toList());
        } catch (ApiException e) {
            log.error("[CONSULTATION][SERVICE][ConsultationService][excel][ERROR]");
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            log.error("[CONSULTATION][SERVICE][ConsultationService][excel][ERROR]");
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
                .setGoodsNm(consultation.getGoodsNm())
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
                .setComplaint(consultation.isComplaint())
                .setConsultStatus(consultation.getConsultStatus())
                .setCreatedAt(consultation.getCreatedAt())
                .setCreatedBy(consultation.getCreatedBy())
                .setModifiedAt(consultation.getModifiedAt())
                .setModifiedBy(consultation.getModifiedBy());

        return basicInfo;
    }
}
