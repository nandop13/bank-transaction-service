package com.services.banktransactionservice.infrastructure.persistence;

import com.services.banktransactionservice.domain.entity.UserTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserTransaction, Integer> {
    @Override
    Optional<UserTransaction> findById(Integer userId);
}
