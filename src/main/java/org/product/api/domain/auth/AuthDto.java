package org.product.api.domain.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.product.api.base.BaseDto;
import org.product.api.code.LoginType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class AuthDto {

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @ApiModel("Auth-LoginForm")
    public static class LoginForm {
        @ApiModelProperty(value = "아이디", notes = "", example = "superuser", required = true)
        @NotEmpty(message = "그룹웨어 또는 임시계정 아이디를 입력해주세요.")
        private String loginId;

        @ApiModelProperty(value = "비밀번호", notes = "비밀번호 규칙: 영문/숫자/특수 8~20자", example = "aaaaaa1!", required = true)
        @NotEmpty(message = "비밀번호를 입력해주세요.")
        private String password;

        @ApiModelProperty(value = "로그인 타입", notes = "GW, IS", example = "GW", required = true)
        @NotNull(message = "로그인 타입을 입력해주세요.")
        private LoginType loginType;
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @ApiModel("AuthDto-TokenAccessForm")
    public static class TokenAccessForm {
        @ApiModelProperty(value = "access token", notes = "", example = "", required = true)
        @NotNull(message = "Access Token 을 입력해주세요.")
        private String accessToken;
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @ApiModel("AuthDto-ApiUserInfo")
    public static class ApiUserInfo extends BaseDto {
        LoginType loginType;
        String loginId;
        String password;
        String name;
        String deptName;
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @ApiModel("AuthDto-UserInfo")
    public static class UserInfo extends BaseDto {
        boolean admin;
        LoginType loginType;
        String loginId;
        String password;
        String name;
        String department;
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    public static class ApiResponse {
        ApiUserInfo data;

        Status status;
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    public static class Status {
        String code;

        String message;

        public boolean hasError() {
            return !"200".equals(code);
        }
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    public static class GwApiResponse {
        ApiUserInfo data;

        String code;
    }
}
