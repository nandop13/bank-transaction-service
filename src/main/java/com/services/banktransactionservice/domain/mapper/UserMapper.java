package com.services.banktransactionservice.domain.mapper;

import com.services.banktransactionservice.domain.dto.UserDTO;
import com.services.banktransactionservice.domain.entity.UserTransaction;

public class UserMapper {
    public static UserDTO toDTO(UserTransaction user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .surname(user.getSurname())
                .build();
    }

    public static UserTransaction toEntity(UserDTO userDTO) {
        return UserTransaction.builder()
                .userId(userDTO.getUserId())
                .name(userDTO.getName())
                .surname(userDTO.getSurname())
                .build();
    }
}
