package com.curame.oauth.security;

import com.curame.oauth.models.entity.User;
import com.curame.oauth.services.IServiceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InfoAditionalToken implements TokenEnhancer {

    @Autowired
    public IServiceUser service;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        Map<String,Object> info = new HashMap<>();

        User user = service.findByUsername(oAuth2Authentication.getName());
        info.put("nombre",user.getFirstname());
        info.put("apellido",user.getLastname());
        info.put("email",user.getEmail());
        user.getProfiles().stream().forEach(profile -> info.put("profile_"+profile.getId(),profile.getProfileName()));

        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(info);

        return oAuth2AccessToken;
    }
}
