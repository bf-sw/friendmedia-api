package org.product.api.base;

import lombok.extern.slf4j.Slf4j;
import org.product.api.code.LoginType;
import org.product.common.ResponseStatus;
import org.product.common.UtilManager;
import org.product.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
public abstract class BaseService <T, ID extends Serializable> {

    @Value("${api.header.code}")
    private String code;

    @Value("${api.header.key}")
    private String key;

    public void checkSecretKey() {
        HttpServletRequest request = getServletRequest();

        String headerCode 	= request.getHeader("code") == null ? "" : request.getHeader("code");
        String headerKey 	= request.getHeader("key") == null ? "" : request.getHeader("key");

        // 서비스 코드 확인
        if (!code.equals(headerCode)) {
            throw new ApiException(ResponseStatus.NONE_AUTH);
        }

        // 서비스 키 확인
        if (!key.equals(headerKey)) {
            throw new ApiException(ResponseStatus.NONE_AUTH);
        }
    }

    public HttpServletRequest getServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }

        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    public LoginType getLoginType() {
        HttpServletRequest servletRequest = getServletRequest();

        if (servletRequest == null) return null;
        Object loginType = servletRequest.getAttribute("loginType");

        return loginType != null ? (LoginType) loginType : null;
    }

    public String getName() {
        HttpServletRequest servletRequest = getServletRequest();

        if (servletRequest == null) return null;
        Object name = servletRequest.getAttribute("name");

        return name != null ? (String) name : null;
    }

    public String getDepartment() {
        HttpServletRequest servletRequest = getServletRequest();

        if (servletRequest == null) return null;
        Object department = servletRequest.getAttribute("department");

        return department != null ? (String) department : null;
    }

    public String getLoginId() {
        HttpServletRequest servletRequest = getServletRequest();

        if (servletRequest == null) return null;
        Object loginId = servletRequest.getAttribute("loginId");

        return loginId != null ? (String) loginId : null;
    }

    public static <T> List<T> toList(final Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    protected boolean isNull(Object obj) { return Objects.isNull(obj); }

    protected boolean isNotNull(Object obj) { return !Objects.isNull(obj); }

    protected boolean isEmpty(Object obj) { return StringUtils.isEmpty(obj); }

    protected boolean isNotEmpty(Object obj) { return !StringUtils.isEmpty(obj); }

    public String post(String url, String body, String code, String secret) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");

        if (UtilManager.isNotEmpty(code)) {
            headers.set("serviceCode", code);
        }

        if (UtilManager.isNotEmpty(secret)) {
            headers.set("secretKey", secret);
        }

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        log.info(request.toString());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.postForEntity(url, request, String.class);

        return result.getBody();
    }
}
