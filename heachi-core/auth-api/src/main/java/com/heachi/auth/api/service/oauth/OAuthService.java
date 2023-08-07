package com.heachi.auth.api.service.oauth;

import com.heachi.auth.api.service.oauth.kakao.OAuthKakaoAdapter;
import com.heachi.auth.api.service.oauth.request.OAuthRegisterRequest;
import com.heachi.auth.api.service.oauth.response.OAuthResponse;
import com.heachi.mysql.define.user.constant.UserPlatformType;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.heachi.mysql.define.user.constant.UserPlatformType.*;

@Service
public class OAuthService {
    private final Map<UserPlatformType, OAuthAdapter> platformMap;

    public OAuthService() {
        this.platformMap = new HashMap<>() {{
            put(KAKAO, new OAuthKakaoAdapter());
        }};
    }

    public OAuthResponse login(UserPlatformType platformType, String code) {
        OAuthAdapter oAuthAdapter = getOAuthAdapter(platformType);

        return oAuthAdapter.login(platformType, code);
    }


    public OAuthResponse register(UserPlatformType platformType, OAuthRegisterRequest request) {
        OAuthAdapter oAuthAdapter = getOAuthAdapter(platformType);

        return oAuthAdapter.register(request);
    }

    private OAuthAdapter getOAuthAdapter(UserPlatformType platformType) {
        return platformMap.get(platformType);
    }
}
