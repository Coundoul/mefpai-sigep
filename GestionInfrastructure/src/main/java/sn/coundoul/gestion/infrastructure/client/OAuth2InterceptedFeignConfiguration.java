package sn.coundoul.gestion.infrastructure.client;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import sn.coundoul.gestion.infrastructure.security.oauth2.AuthorizationHeaderUtil;

public class OAuth2InterceptedFeignConfiguration {

    @Bean(name = "oauth2RequestInterceptor")
    public RequestInterceptor getOAuth2RequestInterceptor(AuthorizationHeaderUtil authorizationHeaderUtil) {
        return new TokenRelayRequestInterceptor(authorizationHeaderUtil);
    }
}
