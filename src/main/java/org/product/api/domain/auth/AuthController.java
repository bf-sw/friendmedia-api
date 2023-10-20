package org.product.api.domain.auth;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.product.api.base.BaseController;
import org.product.api.code.LoginType;
import org.product.common.ApiResponse;
import org.product.security.JwtAuthenticationResponse;
import org.product.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value="/api/auth")
@Api(value="AuthController")
public class AuthController extends BaseController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Value("${biz.jwt.expired}")
    int jwtExpiredMinutes;

    @Autowired
    AuthService authService;

    @ApiOperation(value = "로그인", notes = "아이디와 비밀번호로 로그인, 성공시 jwt리턴")
    @PostMapping(value = "/v1/login", produces = APPLICATION_JSON)
    public ApiResponse<JwtAuthenticationResponse> login(@Valid @RequestBody AuthDto.LoginForm form) {

        AuthDto.UserInfo userInfo = new AuthDto.UserInfo();

        // 그룹웨어 로그인
        if (LoginType.GW.equals(form.getLoginType())) {
            userInfo = authService.gwLogin(form);
        // 임시계정 로그인
        } else {
            userInfo = authService.imsiLogin(form);
        }

        userInfo.setPassword(form.getPassword());

        return ApiResponse.ok(makeAuthResponse(userInfo));
    }

    private JwtAuthenticationResponse makeAuthResponse(AuthDto.UserInfo userInfo) {

        JwtTokenProvider.TokenFormat token = new JwtTokenProvider.TokenFormat();

        token.setLoginType(userInfo.getLoginType());
        token.setLoginId(userInfo.getLoginId());
        token.setPassword(userInfo.getPassword());
        token.setName(userInfo.getName());
        token.setDepartment(userInfo.getDepartment());

        String accessToken = tokenProvider.generateToken(token, jwtExpiredMinutes);

        JwtAuthenticationResponse response = new JwtAuthenticationResponse();

        response
                .setAccessToken(accessToken)
                .setLoginId(userInfo.getLoginId())
                .setName(userInfo.getName())
                .setDepartment(userInfo.getDepartment());

        return response;
    }
}
