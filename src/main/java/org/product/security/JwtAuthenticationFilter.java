package org.product.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = tokenProvider.getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                JwtTokenProvider.TokenFormat token = tokenProvider.getTokenInfo(jwt);

                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add( new SimpleGrantedAuthority("ROLE_ADMIN"));

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(token, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                request.setAttribute("loginId", token.getLoginId());
                request.setAttribute("loginType", token.getLoginType());
                request.setAttribute("name", token.getName());
                request.setAttribute("department", token.getDepartment());
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
            response.sendError(HttpStatus.UNAUTHORIZED.value());
        }

        filterChain.doFilter(request, response);
    }
}