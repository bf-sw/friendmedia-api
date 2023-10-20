package org.product.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.util.regex.Pattern;

@Slf4j
public class UtilManager {

    public static final String EMPTY_STRING = "";
    public static final String NULL = "null";
    public static final String UNDEFINED = "undefined";
    public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[?!@#$%^&*()+=])(?=\\S+$).{8,20}$";
    public static final String PASSWORD_PATTERN_WIDTH_NUMBER = "^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{4,20}$";
    public static final String LOGINID_PATTERN_WIDTH_NUMBER = "^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{4,20}$";
    public static final String LOGINID_PATTERN = "^(?=.*[a-z])(?=\\S+$).{4,20}$";
    private static final String MOBILE_PATTERN = "^01(?:0|1|[6-9])(\\d{3}|\\d{4})(\\d{4})$";

    public static boolean isNotEmpty(@Nullable String object) {
        return !isEmpty(object);
    }

    public static boolean isEmpty(@Nullable String str) {
        return (str == null || NULL.equals(str.toLowerCase()) || EMPTY_STRING.equals(str) || UNDEFINED.equals(str.toLowerCase()) || UNDEFINED.equals(str.toUpperCase()));
    }

    public static boolean isMobileNumberType(String mobile) {
        if (isNotEmpty(mobile)) {
            return Pattern.compile(MOBILE_PATTERN).matcher(mobile).matches();
        }
        return false;
    }

    public static boolean isMatchedLoginId(String loginId) {
        boolean result = false;
        if (isNotEmpty(loginId)) {
            if (Pattern.matches(LOGINID_PATTERN, loginId) || Pattern.matches(LOGINID_PATTERN_WIDTH_NUMBER, loginId)) {
                result = true;
            }
        }
        return result;
    }

    public static boolean isMatchedPassword(String password) {
        if (isNotEmpty(password)) {
            //비밀 번호는 8~20자리로 숫자,소문자,대문자,특수문자(?!@#$%^&*()+=)가 포함되어야 합니다.
            // 비밀번호는 영문 + 숫자 조합으로 대체
            return Pattern.matches(PASSWORD_PATTERN_WIDTH_NUMBER, password);
        }
        return false;
    }
}
