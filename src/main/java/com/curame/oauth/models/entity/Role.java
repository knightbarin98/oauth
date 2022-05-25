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
public class Role {

    private Long id;
    private String name;
    private List<User> users;
}
