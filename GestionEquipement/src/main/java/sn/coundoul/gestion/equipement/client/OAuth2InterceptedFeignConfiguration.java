package sn.coundoul.gestion.equipement.client;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import sn.coundoul.gestion.equipement.security.oauth2.AuthorizationHeaderUtil;

public class OAuth2InterceptedFeignConfiguration {

    @Bean(name = "oauth2RequestInterceptor")
    public RequestInterceptor getOAuth2RequestInterceptor(AuthorizationHeaderUtil authorizationHeaderUtil) {
        return new TokenRelayRequestInterceptor(authorizationHeaderUtil);
    }
}
