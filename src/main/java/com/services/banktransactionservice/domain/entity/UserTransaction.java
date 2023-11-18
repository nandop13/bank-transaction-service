package com.services.banktransactionservice.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserTransaction {
    @Id
    private Integer userId;
    private String name;
    private String surname;
    private String nationalIdentificationNumber;

}
