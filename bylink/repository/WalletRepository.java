package com.bylink.repository;

import com.bylink.entity.Wallet;
import com.bylink.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByUser(User user);
    Optional<Wallet> findByWalletNumber(String walletNumber);
}
