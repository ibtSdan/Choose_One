package com.example.choose_one.component;

import com.example.choose_one.entity.UserEntity;
import com.example.choose_one.model.customuser.CustomUserDetails;
import com.example.choose_one.repository.UserRepository;
import com.example.choose_one.service.TokenService;
import org.apache.catalina.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;

@Component
@Profile("test") // 테스트 프로파일에서만 실행되도록 설정
public class TestDataRunner implements CommandLineRunner{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void run(String... args) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter("tokens.csv"));

        for (int i = 1; i <= 500; i++) {
            String username = "testuser" + i;
            String password = "password" + i;

            /*
            var encodePw = passwordEncoder.encode(password);
            var entity = UserEntity.builder()
                    .userId(username)
                    .password(encodePw)
                    .role("ROLE_USER")
                    .build();
            userRepository.save(entity);
             */

            var authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            var authentication = authenticationManager.authenticate(authenticationToken);

            var user = (CustomUserDetails) authentication.getPrincipal();

            var userId = user.getUserId();
            var authorities = user.getAuthorities();

            var accessToken = tokenService.issueAccessToken(userId, authorities);

            // CSV 파일에 기록
            writer.write(String.valueOf(accessToken.getToken()));
            writer.newLine();
        }
        writer.close();
        System.out.println("=== 500개의 테스트 유저와 tokens.csv 생성이 완료되었습니다 ===");
    }
}
