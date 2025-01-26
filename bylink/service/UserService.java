package com.bylink.service;

import com.bylink.dto.auth.SignupRequest;
import com.bylink.entity.User;
import com.bylink.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final WalletService walletService;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + phoneNumber));
    }

    public UserDetails loadUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }

    @Transactional
    public User registerUser(SignupRequest request) {
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new RuntimeException("Phone number already registered");
        }

        if (request.getCinNumber() != null && userRepository.existsByCinNumber(request.getCinNumber())) {
            throw new RuntimeException("CIN number already registered");
        }

        if (request.getFiscalNumber() != null && userRepository.existsByFiscalNumber(request.getFiscalNumber())) {
            throw new RuntimeException("Fiscal number already registered");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUserType(request.getUserType());
        user.setCinNumber(request.getCinNumber());
        user.setFiscalNumber(request.getFiscalNumber());
        user.setCompanyName(request.getCompanyName());
        user.setHasPatente(request.isHasPatente());
        user.setVerified(false);

        User savedUser = userRepository.save(user);
        walletService.createWallet(savedUser);
        
        return savedUser;
    }

    @Transactional
    public User verifyUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setVerified(true);
        return userRepository.save(user);
    }
}
