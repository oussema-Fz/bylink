package com.bylink.repository;

import com.bylink.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByCinNumber(String cinNumber);
    Optional<User> findByFiscalNumber(String fiscalNumber);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByCinNumber(String cinNumber);
    boolean existsByFiscalNumber(String fiscalNumber);
}
