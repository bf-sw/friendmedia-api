package org.product.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.product.common.DateUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

@Slf4j
public class CommonInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        final String requestURI = request.getRequestURI();

        if (requestURI.contains(".css") ||
                requestURI.contains(".js") ||
                requestURI.contains(".ico") ||
                requestURI.contains(".html") ||
                requestURI.contains(".otf") ||
                requestURI.contains(".jpg") ||
                requestURI.contains(".png") ||
                requestURI.contains(".gif") ||
                requestURI.contains(".mp4") ||
                requestURI.startsWith("/swagger-resources") ||
                requestURI.startsWith("/error") ||
                requestURI.startsWith("/csrf") ||
                requestURI.equals("/")) {
            return true;
        }

        Enumeration<String> paramNames 	= request.getParameterNames();
        log.info("******************************************************");
        log.info("[INTERCEPTOR][URL] : {}", requestURI);
        while (paramNames.hasMoreElements()) {
            String key 		= (String) paramNames.nextElement();
            String value 	= request.getParameter(key);

            if ("_".equals(key)) continue;
            log.info("[INTERCEPTOR][PARAMETER][{}] : {}", key, value);
        }
        log.info("******************************************************");

        long now = DateUtils.getNow();
        long startTime = 1676109600; // 2023-02-11 19:00:00
        long endTime = 1679374800; // 2023-02-12 09:00:00

        // 긴급 사용 중지 필요할 경우 설정
        if (now >= startTime && now <= endTime) {
            //if (false) {
            log.info("긴급 사용 중지 설정 시작 :::");
            if (requestURI.contains("swagger") || requestURI.contains("/api/v1/auth/hello") || requestURI.contains("/api/v2/bf")) {
                return true;
            }
            throw new Exception("suspension");
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }
}
