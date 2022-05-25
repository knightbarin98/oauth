package com.curame.oauth.services;

import com.curame.oauth.models.entity.User;

public interface IServiceUser {

    public User findByUsername(String username);
    public User update(User user, Long id);
}
