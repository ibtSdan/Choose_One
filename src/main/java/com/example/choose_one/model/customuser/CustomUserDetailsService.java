package com.example.choose_one.model.customuser;

import com.example.choose_one.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUserId(username)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        return new CustomUserDetails(
                user.getId(), user.getUserId(), user.getPassword(), user.getRole()
        );
    }
}
