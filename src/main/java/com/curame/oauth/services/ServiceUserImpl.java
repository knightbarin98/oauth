package com.curame.oauth.services;

import com.curame.oauth.clients.UserClient;
import com.curame.oauth.models.entity.User;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceUserImpl implements IServiceUser, UserDetailsService {

    private static Logger log = LoggerFactory.getLogger(ServiceUserImpl.class);

    @Autowired
    private UserClient client;


    @Override
    public User findByUsername(String username) {
        return client.findByUsername(username);
    }

    @Override
    public User update(User user, Long id) {
        return client.update(user, id);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        try {
            User user = client.findByUsername(s);

            List<GrantedAuthority> authorities = user.getRoles()
                    .stream().map(role -> {
                        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getName());
                        return grantedAuthority;
                    })
                    .peek(grantedAuthority -> log.info("Role : " + grantedAuthority.getAuthority()))
                    .collect(Collectors.toList());
            log.info("Usuario autenticado " + s);
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    user.getEnabled(),
                    true, true, true, authorities
            );

        } catch (FeignException ex) {
            log.error("Error en login, no existe el susuario en el sistema");
            throw new UsernameNotFoundException("Error en login, no existe el usuario en el sistema");
        }
    }
}
