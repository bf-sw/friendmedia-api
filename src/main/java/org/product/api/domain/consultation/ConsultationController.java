package org.product.api.domain.consultation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.product.api.base.BaseController;
import org.product.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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

}
