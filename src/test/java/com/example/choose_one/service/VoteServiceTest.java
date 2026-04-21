package com.example.choose_one.service;

import com.example.choose_one.common.error.PostErrorCode;
import com.example.choose_one.common.error.UserErrorCode;
import com.example.choose_one.common.exception.ApiException;
import com.example.choose_one.entity.PostEntity;
import com.example.choose_one.entity.UserEntity;
import com.example.choose_one.entity.VoteEntity;
import com.example.choose_one.model.vote.VoteRequest;
import com.example.choose_one.repository.PostRepository;
import com.example.choose_one.repository.UserRepository;
import com.example.choose_one.repository.VoteRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VoteServiceTest {

    @InjectMocks
    private VoteService voteService;

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @Nested
    class 투표_성공 {

        @Test
        void 투표_성공() {
            var userId = 1L;
            var postId = 1L;
            mockSecurityContext(userId);
            var voteRequest = new VoteRequest(postId, 'A');
            var user = UserEntity.builder()
                    .id(1L)
                    .userId("ex")
                    .password("1234")
                    .role("ROLE_USER")
                    .build();
            var post = PostEntity.builder()
                    .id(postId)
                    .user(user)
                    .title("제목")
                    .contentA("A")
                    .contentB("B")
                    .voteCountA(12L)
                    .voteCountB(9L)
                    .build();

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(postRepository.findById(postId)).thenReturn(Optional.of(post));
            when(voteRepository.existsByUserIdAndPostId(userId, postId)).thenReturn(false);

            var result = voteService.create(voteRequest);

            verify(voteRepository).save(any(VoteEntity.class));
            verify(postRepository).save(any(PostEntity.class));
        }
    }

    @Nested
    class 투표_실패 {

        @Test
        void 투표_실패_유저_없음() {
            var userId = 1L;
            var postId = 1L;
            var voteRequest = new VoteRequest(postId, 'A');
            mockSecurityContext(userId);
            when(userRepository.findById(userId)).thenThrow(new ApiException(UserErrorCode.USER_NOT_FOUND));
            assertThrows(ApiException.class, () -> voteService.create(voteRequest));
            verify(postRepository, never()).findById(postId);
        }

        @Test
        void 투표_실패_postId_없음() {
            var userId = 1L;
            var postId = 1L;
            var voteRequest = new VoteRequest(postId, 'A');
            mockSecurityContext(userId);
            var user = UserEntity.builder()
                    .id(1L)
                    .userId("ex")
                    .password("1234")
                    .role("ROLE_USER")
                    .build();
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(postRepository.findById(postId)).thenThrow(new ApiException(PostErrorCode.POST_NOT_FOUND));
            assertThrows(ApiException.class, () -> voteService.create(voteRequest));
            verify(voteRepository, never()).save(any(VoteEntity.class));
        }

        @Test
        void 투표_실패_잘못된_옵션() {
            var userId = 1L;
            var postId = 1L;
            mockSecurityContext(userId);
            var voteRequest = new VoteRequest(postId, 'C');
            var user = UserEntity.builder()
                    .id(1L)
                    .userId("ex")
                    .password("1234")
                    .role("ROLE_USER")
                    .build();
            var post = PostEntity.builder()
                    .id(postId)
                    .user(user)
                    .title("제목")
                    .contentA("A")
                    .contentB("B")
                    .voteCountA(12L)
                    .voteCountB(9L)
                    .build();

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(postRepository.findById(postId)).thenReturn(Optional.of(post));

            assertThrows(ApiException.class, () -> voteService.create(voteRequest));
            verify(voteRepository, never()).save(any(VoteEntity.class));
        }

        @Test
        void 투표_실패_중복된_투표() {
            var userId = 1L;
            var postId = 1L;
            mockSecurityContext(userId);
            var voteRequest = new VoteRequest(postId, 'B');
            var user = UserEntity.builder()
                    .id(1L)
                    .userId("ex")
                    .password("1234")
                    .role("ROLE_USER")
                    .build();
            var post = PostEntity.builder()
                    .id(postId)
                    .user(user)
                    .title("제목")
                    .contentA("A")
                    .contentB("B")
                    .voteCountA(12L)
                    .voteCountB(9L)
                    .build();

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(postRepository.findById(postId)).thenReturn(Optional.of(post));
            when(voteRepository.existsByUserIdAndPostId(userId, postId)).thenReturn(true);

            assertThrows(ApiException.class, () -> voteService.create(voteRequest));
            verify(voteRepository, never()).save(any(VoteEntity.class));
        }
    }

    private void mockSecurityContext(Long userId) {
        var auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(userId);
        var context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);

        SecurityContextHolder.setContext(context);
    }
}
