package org.product.api.base;

import lombok.extern.slf4j.Slf4j;
import org.product.common.ApiResponse;
import org.product.exception.ApiException;
import org.product.exception.CriticalException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@CrossOrigin
public abstract class BaseController {

    protected static final String APPLICATION_JSON = "application/json; charset=UTF-8";

    @Value("${spring.profiles.active}")
    String properties;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ApiResponse processValidationError(MethodArgumentNotValidException ex) {
        FieldError fieldError = (FieldError) ex.getBindingResult().getFieldErrors().get(0);
        return ApiResponse.error(fieldError.getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ApiException.class})
    public ApiResponse handleApiException(ApiException e) {
        log.error("ApiException ::: {}", e.getMessage());
        e.printStackTrace();
        return ApiResponse.error(e.getStatus(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler({CriticalException.class})
    public ApiResponse handleCriticalException(CriticalException e) {
        log.error("ApiException ::: {}", e.getMessage());
        e.printStackTrace();
        return ApiResponse.error(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public ApiResponse handleException(Exception e) {
        log.error("Exception ::: {}", e.getMessage());
        e.printStackTrace();
        return ApiResponse.error(e.getMessage());
    }
}
