package com.bylink.service;

import com.bylink.entity.User;
import com.bylink.entity.Wallet;
import com.bylink.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    @Transactional
    public Wallet createWallet(User user) {
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setWalletNumber(generateWalletNumber());
        return walletRepository.save(wallet);
    }

    @Transactional
    public Wallet deposit(User user, BigDecimal amount) {
        Wallet wallet = getWalletByUser(user);
        wallet.setBalance(wallet.getBalance().add(amount));
        return walletRepository.save(wallet);
    }

    @Transactional
    public void transfer(User sender, String receiverWalletNumber, BigDecimal amount) {
        Wallet senderWallet = getWalletByUser(sender);
        Wallet receiverWallet = walletRepository.findByWalletNumber(receiverWalletNumber)
                .orElseThrow(() -> new RuntimeException("Receiver wallet not found"));

        if (senderWallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        senderWallet.setBalance(senderWallet.getBalance().subtract(amount));
        receiverWallet.setBalance(receiverWallet.getBalance().add(amount));

        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);
    }

    public Wallet getWalletByUser(User user) {
        return walletRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    private String generateWalletNumber() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }
}
