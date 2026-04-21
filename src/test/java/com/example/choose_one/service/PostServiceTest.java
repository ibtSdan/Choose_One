package com.example.choose_one.service;

import com.example.choose_one.common.error.PostErrorCode;
import com.example.choose_one.common.error.UserErrorCode;
import com.example.choose_one.common.exception.ApiException;
import com.example.choose_one.entity.PostEntity;
import com.example.choose_one.entity.UserEntity;
import com.example.choose_one.model.post.PostRequest;
import com.example.choose_one.repository.PostRepository;
import com.example.choose_one.repository.UserRepository;
import com.example.choose_one.repository.VoteRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VoteRepository voteRepository;

    @Nested
    class create {

        @Test
        void 게시글_작성_성공() {
            var postRequest = new PostRequest("제목", "A", "B");
            var userId = 1L;
            var userEntity = UserEntity.builder()
                    .id(1L)
                    .userId("ex")
                    .password("1234")
                    .role("ROLE_USER")
                    .build();
            mockSecurityContext(userId);

            when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

            var result = postService.create(postRequest);
            assertNotNull(result);
            verify(postRepository).save(any(PostEntity.class));
        }

        @Test
        void 게시글_작성_실패_유저없음() {
            var postRequest = new PostRequest("제목", "A", "B");
            var userId = 1L;
            mockSecurityContext(userId);

            when(userRepository.findById(userId))
                    .thenThrow(new ApiException(UserErrorCode.USER_NOT_FOUND));
            assertThrows(ApiException.class, () -> postService.create(postRequest));
            verify(postRepository, never()).save(any(PostEntity.class));
        }
    }

    @Nested
    class view {

        @Test
        void 게시글_조회_성공() {
            Long postId = 1L;
            var entity = PostEntity.builder()
                    .id(postId)
                    .user(UserEntity.builder()
                            .id(1L)
                            .userId("ex")
                            .password("1234")
                            .role("ROLE_USER")
                            .build())
                    .title("제목")
                    .contentA("A")
                    .contentB("B")
                    .voteCountA(12L)
                    .voteCountB(9L)
                    .build();
            when(postRepository.findById(postId)).thenReturn(Optional.of(entity));
            when(voteRepository.countByPostIdAndVoteOption(postId, 'A'))
                    .thenReturn(entity.getVoteCountA());
            when(voteRepository.countByPostIdAndVoteOption(postId, 'B'))
                    .thenReturn(entity.getVoteCountB());

            var result = postService.view(postId);

            assertEquals("제목", result.getData().getTitle());
            assertEquals(12L, result.getData().getCountA());
            assertEquals(9L, result.getData().getCountB());
        }

        @Test
        void 게시글_조회_실패_게시글없음() {
            Long postId = 1L;
            when(postRepository.findById(postId)).thenThrow(new ApiException(PostErrorCode.POST_NOT_FOUND));
            assertThrows(ApiException.class, () -> postService.view(postId));
            verify(voteRepository, never()).countByPostIdAndVoteOption(postId, 'A');
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
