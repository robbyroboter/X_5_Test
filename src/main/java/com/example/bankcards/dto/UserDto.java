package com.example.bankcards.dto;

import com.example.bankcards.entity.Role;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private boolean enabled;

    private Set<Role.RoleName> roles;
}
