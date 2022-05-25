package com.curame.oauth.clients;

import com.curame.oauth.models.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "servicio-usuarios")
public interface UserClient {
    @GetMapping("/users/search/search-username")
    public User findByUsername(@RequestParam("username") String username);

    @PutMapping("/users/{id}")
    public User update(@RequestBody User user, Long id);
}
