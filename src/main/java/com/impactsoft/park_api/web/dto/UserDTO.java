package com.impactsoft.park_api.web.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
public class UserDTO {
    private String username;
    private String password;
}
