package com.curame.oauth.models.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class User {

    private Long id;
    private String username;
    private String password;
    private Boolean enabled;
    private String firstname;
    private String lastname;
    private String email;
    private Integer loginTries;
    private Profile profile;
    private List<Role> roles;

}
