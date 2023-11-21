package org.product.api.domain.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.product.api.base.BaseService;
import org.product.api.code.LoginType;
import org.product.api.domain.admin.Admin;
import org.product.api.domain.admin.AdminRepository;
import org.product.common.ResponseStatus;
import org.product.common.UtilManager;
import org.product.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class AuthService extends BaseService {

    @Value("${gw.api.login.url}")
    private String gwApiLoginUrl;

    @Value("${bf.api.login.url}")
    private String bfApiLoginUrl;

    @Value("${bf.api.header.code}")
    private String bfServiceCode;

    @Value("${bf.api.header.key}")
    private String bfServiceKey;

    @Autowired
    private AdminRepository adminRepository;

    public AuthDto.UserInfo gwLogin(AuthDto.LoginForm form) {

        try {
            Gson gson = new Gson();

            JSONObject json = new JSONObject();
            json.put("userId", form.getLoginId());
            json.put("password", form.getPassword());

            String result = post(gwApiLoginUrl, json.toJSONString(), null, null);

            if (UtilManager.isNotEmpty(result)) {
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(result);
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(jsonObject.toString());
                String code = root.at("/code").toString().replaceAll("\"", "");
                String data = root.at("/data").toString().replaceAll("\"", "");

                if ("200".equals(code) && UtilManager.isNotEmpty(data)) {
                    AuthDto.GwApiResponse response = gson.fromJson(result, AuthDto.GwApiResponse.class);
                    AuthDto.ApiUserInfo userInfo = response.getData();
                    userInfo.setLoginType(LoginType.GW);
                    return toBasicInfo(userInfo);
                }
            }

            throw new ApiException("로그인 가능한 그룹웨어 정보가 없습니다.");
        } catch (ApiException e) {
            log.error("[AUTH][SERVICE][AuthService][gwLogin][ERROR]");
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            log.error("[AUTH][SERVICE][AuthService][gwLogin][ERROR]");
            e.printStackTrace();
            throw new ApiException(ResponseStatus.ERROR);
        }
    }

    public AuthDto.UserInfo imsiLogin(AuthDto.LoginForm form) {

        try {
            Gson gson = new Gson();

            JSONObject json = new JSONObject();
            json.put("loginId", form.getLoginId());
            json.put("password", form.getPassword());

            String result = post(bfApiLoginUrl, json.toJSONString(), bfServiceCode, bfServiceKey);

            AuthDto.ApiResponse response = gson.fromJson(result, AuthDto.ApiResponse.class);

            if (!response.getStatus().hasError() && Objects.nonNull(response.getData())) {
                AuthDto.ApiUserInfo userInfo = response.getData();
                userInfo.setLoginType(LoginType.IS);
                return toBasicInfo(userInfo);
            } else {
                throw new ApiException(response.getStatus().getMessage());
            }
        } catch (ApiException e) {
            log.error("[AUTH][SERVICE][AuthService][imsiLogin][ERROR]");
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            log.error("[AUTH][SERVICE][AuthService][imsiLogin][ERROR]");
            e.printStackTrace();
            throw new ApiException(ResponseStatus.ERROR);
        }
    }

    private AuthDto.UserInfo toBasicInfo(AuthDto.ApiUserInfo response) {
        AuthDto.UserInfo userInfo = new AuthDto.UserInfo();

        try {
            Optional<Admin> admin = adminRepository.findFirstByCounselorIdAndDeletedFalse(response.getLoginId());

            // 접속 가능 사용자로 등록된 직원만 접속 가능하도록 처리 - 인프라팀 정종영 주임 요청
            if (!admin.isPresent()) {
                throw new ApiException("접속이 허용되지 않은 직원입니다. 관리자에게 문의하시기 바랍니다.");
            }

            userInfo
                    .setLoginType(response.getLoginType())
                    .setLoginId(response.getLoginId())
                    .setName(response.getName())
                    .setDepartment(response.getDeptName());

            userInfo.setAdmin(admin.isPresent());

        } catch (ApiException e) {
            log.error("[AUTH][SERVICE][AuthService][toBasicInfo][ERROR]");
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            log.error("[AUTH][SERVICE][AuthService][toBasicInfo][ERROR]");
            e.printStackTrace();
            throw new ApiException(ResponseStatus.ERROR);
        }
        return userInfo;
    }
}
