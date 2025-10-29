package com.impactsoft.park_api.web.dto.mapper;

import com.impactsoft.park_api.entities.User;
import com.impactsoft.park_api.web.dto.UserDTO;
import com.impactsoft.park_api.web.dto.UserResponseDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

public class UserMapper {

    public static User toUser(UserDTO createDTO) {
        return new ModelMapper().map(createDTO, User.class);
    }

    public static UserResponseDTO toDTO (User createUser) {
        String role = createUser.getRole().name().substring("ROLE_".length());
        PropertyMap<User, UserResponseDTO> props = new PropertyMap<User, UserResponseDTO>() {
            @Override
            protected void configure() {
                map().setRole(role);
            }
        };
        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(props);
        return mapper.map(createUser, UserResponseDTO.class);
    }

}
