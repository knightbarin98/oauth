package com.curame.oauth.security.event;

import com.curame.oauth.models.entity.User;
import com.curame.oauth.services.IServiceUser;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessErrorHandler implements AuthenticationEventPublisher {

    private static Logger log = LoggerFactory.getLogger(AuthenticationSuccessErrorHandler.class);

    @Autowired
    private IServiceUser service;

    @Override
    public void publishAuthenticationSuccess(Authentication authentication) {
        //ESto cuando quiere lanzar el evento para cuando se autentica el cliente y no usuarios
        if (authentication.getDetails() instanceof WebAuthenticationDetails) {
            return;
        }

        UserDetails user = (UserDetails) authentication.getDetails();
        String message = "SUCCESS LOG IN: " + user.getUsername();
        log.info(message);

        User userdb = service.findByUsername(authentication.getName());

        if (userdb.getLoginTries() != null && userdb.getLoginTries() > 0) {
            userdb.setLoginTries(0);
            service.update(userdb, userdb.getId());
        }

    }

    @Override
    public void publishAuthenticationFailure(AuthenticationException e, Authentication authentication) {
        String message = "ERROR LOG IN: " + e.getMessage();
        log.error(message);

        try {
            User user = service.findByUsername(authentication.getName());
            if (user.getLoginTries() == null) {
                user.setLoginTries(0);
            }
            log.info("Intentos actual es de :" + user.getLoginTries());
            user.setLoginTries(user.getLoginTries() + 1);
            log.info("Intentos actual es de :" + user.getLoginTries());

            if (user.getLoginTries() >= 3) {
                log.info(String.format("El usuario %s deshabilitado por maximo intentos", user.getUsername()));
                user.setEnabled(false);
            }
            service.update(user, user.getId());

        } catch (FeignException ex) {
            log.error(String.format("El usuario %s no existe en el sistema", authentication.getName()));
        }
    }
}
