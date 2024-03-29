package org.product.api.domain.consultation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.product.api.base.BaseController;
import org.product.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value="/api/consultation")
@Api(value="ConsultationController")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ConsultationController extends BaseController {

    @Autowired
    ConsultationService consultationService;

    @ApiOperation(value="상담 이력 생성", notes = "상담 이력 생성")
    @PostMapping(value="/v1/regist", produces = APPLICATION_JSON)
    public ApiResponse registConsultation(@Valid @RequestBody ConsultationDto.RegistryForm form) {

        consultationService.registConsultation(form);

        return ApiResponse.ok();
    }

    @ApiOperation(value="상담 이력 수정", notes = "상담 이력 수정")
    @PostMapping(value="/v1/update", produces = APPLICATION_JSON)
    public ApiResponse updateConsultation(@Valid @RequestBody ConsultationDto.UpdateForm form) {

        consultationService.updateConsultation(form);

        return ApiResponse.ok();
    }

    @ApiOperation(value="상담 이력 멀티 수정", notes = "상담 이력 멀티 수정")
    @PostMapping(value="/v1/multi/update", produces = APPLICATION_JSON)
    public ApiResponse updateMultiConsultation(@Valid @RequestBody ConsultationDto.UpdateMultiForm form) {

        consultationService.updateMultiConsultation(form);

        return ApiResponse.ok();
    }

    @ApiOperation(value="상담 이력 삭제", notes = "상담 이력 삭제")
    @PostMapping(value="/v1/delete", produces = APPLICATION_JSON)
    public ApiResponse deleteConsultation(@Valid @RequestBody ConsultationDto.DeleteForm form) {

        consultationService.deleteConsultation(form);

        return ApiResponse.ok();
    }

    @ApiOperation(value="상담 이력 멀티 삭제", notes = "상담 이력 멀티 삭제")
    @PostMapping(value="/v1/multi/delete", produces = APPLICATION_JSON)
    public ApiResponse deleteMultiConsultation(@Valid @RequestBody ConsultationDto.DeleteMultiForm forms) {

        consultationService.deleteMultiConsultation(forms);

        return ApiResponse.ok();
    }

    @ApiOperation(value = "상담 이력 검색", notes = "상담 이력 검색")
    @GetMapping(value = "/v1/search", produces = APPLICATION_JSON)
    public ApiResponse<Page<ConsultationDto.BasicInfo>> search(@Valid @ModelAttribute ConsultationDto.SearchForm condition,
                                                               @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                                                               @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
                                                               @RequestParam(value = "sort", defaultValue = "id", required = false) String sort) {

        log.info("[CONSULTATION][SERVICE][ConsultationService][search][REQ]", condition.toString());

        PageRequest pageable = PageRequest.of(page, size, Sort.Direction.DESC, sort);
        Page<ConsultationDto.BasicInfo> result = consultationService.search(condition, pageable);

        log.info("[CONSULTATION][SERVICE][ConsultationService][search][RES]", result.getContent().toString());

        return ApiResponse.ok(result);
    }

    @ApiOperation(value = "상담 이력 상세 조회", notes = "상담 이력 상세 조회")
    @GetMapping(value = "/v1/{consultationId}/detail", produces = APPLICATION_JSON)
    public ApiResponse<ConsultationDto.BasicInfo> detail(@NotNull(message = "상담이력 아이디는 필수 입니다.") @PathVariable(name = "consultationId") Long consultationId) {

        log.info("[CONSULTATION][SERVICE][ConsultationService][detail][REQ]", consultationId);

        ConsultationDto.BasicInfo result = consultationService.detail(consultationId);

        log.info("[CONSULTATION][SERVICE][ConsultationService][detail][RES]", result.getContent().toString());

        return ApiResponse.ok(result);
    }

    @ApiOperation(value = "엑셀 다운로드용 상담 이력 목록 조회", notes = "엑셀 다운로드용 상담 이력 목록 조회")
    @GetMapping(value = "/v1/excel", produces = APPLICATION_JSON)
    public ApiResponse<List<ConsultationDto.BasicInfo>> excel(ConsultationDto.SearchForm form) {

        Pageable wholePage = Pageable.unpaged();
        List<ConsultationDto.BasicInfo> results = consultationService.excel(form, wholePage);

        return ApiResponse.ok(results);
    }

}
