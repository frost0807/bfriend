package com.frost.bfriend.service;

import com.frost.bfriend.dto.ReplyDto.SaveReplyRequest;
import com.frost.bfriend.entity.MatchPost;
import com.frost.bfriend.entity.Reply;
import com.frost.bfriend.entity.User;
import com.frost.bfriend.exception.matchpost.ForbiddenReplyException;
import com.frost.bfriend.exception.matchpost.MatchPostNotFoundException;
import com.frost.bfriend.exception.matchpost.ParentReplyNotFoundException;
import com.frost.bfriend.repository.matchpost.MatchPostRepository;
import com.frost.bfriend.repository.reply.ReplyRepository;
import com.frost.bfriend.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MatchPostServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    MatchPostRepository matchPostRepository;

    @Mock
    ReplyRepository replyRepository;

    @InjectMocks
    MatchPostService matchPostService;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .id(1L)
                .email("guest1@bfriend.com")
                .build();

        user2 = User.builder()
                .id(2L)
                .email("guest2@bfriend.com")
                .build();

        user3 = User.builder()
                .id(3L)
                .email("guest3@bfriend.com")
                .build();
    }

    private User createUser() {
        return user1;
    }

    private User createUser2() {
        return user2;
    }

    private User createUser3() {
        return user3;
    }

    private MatchPost createMatchPost() {
        return MatchPost.builder()
                .id(1L)
                .writer(createUser())
                .build();
    }

    private Reply createParentReply() {
        return Reply.builder()
                .id(1L)
                .user(createUser2())
                .matchPost(createMatchPost())
                .build();
    }

    private Reply createChildReply() {
        return Reply.builder()
                .id(2L)
                .matchPost(createMatchPost())
                .parentReply(createParentReply())
                .build();
    }

    private SaveReplyRequest createSaveParentReplyRequest() {
        return SaveReplyRequest.builder()
                .matchPostId(1L)
                .comment("reply")
                .build();
    }

    private SaveReplyRequest createSaveChildReplyRequest() {
        return SaveReplyRequest.builder()
                .matchPostId(1L)
                .parentReplyId(1L)
                .comment("reply")
                .build();
    }

    @DisplayName("댓글, 대댓글을 작성하는데 게시물이 존재하지 않거나 삭제되었을 경우")
    @Test
    void isMatchPostNotFoundException() {
        given(userRepository.findByIdAndIsDeletedFalse(1L)).willReturn(Optional.of(createUser()));
        given(matchPostRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(MatchPostNotFoundException.class,
                () -> matchPostService.saveReply(1L, createSaveChildReplyRequest()));
    }

    @DisplayName("대댓글을 작성할 때 상위 댓글이 존재하지 않을 경우")
    @Test
    void isParentReplyNotFoundExceptionThrown() {
        given(userRepository.findByIdAndIsDeletedFalse(1L)).willReturn(Optional.of(createUser()));
        given(matchPostRepository.findById(1L)).willReturn(Optional.of(createMatchPost()));
        given(replyRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(ParentReplyNotFoundException.class,
                () -> matchPostService.saveReply(1L, createSaveChildReplyRequest()));
    }

    @DisplayName("대댓글을 작성할 때 로그인한 유저가 게시물 작성자도 아니고 댓글 작성자도 아닌 경우")
    @Test
    void isForbiddenReplyExceptionThrownWhenCreatingChildReply() {
        given(userRepository.findByIdAndIsDeletedFalse(3L)).willReturn(Optional.of(createUser3()));
        given(matchPostRepository.findById(1L)).willReturn(Optional.of(createMatchPost()));
        given(replyRepository.findById(1L)).willReturn(Optional.of(createParentReply()));

        assertThrows(ForbiddenReplyException.class,
                () -> matchPostService.saveReply(3L, createSaveChildReplyRequest()));
    }

    @DisplayName("댓글을 작성하는데 로그인한 유저가 게시물 작성자 일 경우")
    @Test
    void isForbiddenReplyExceptionThrownWhenCreatingParentReply() {
        given(userRepository.findByIdAndIsDeletedFalse(1L)).willReturn(Optional.of(createUser()));
        given(matchPostRepository.findById(1L)).willReturn(Optional.of(createMatchPost()));

        assertThrows(ForbiddenReplyException.class,
                () -> matchPostService.saveReply(1L, createSaveParentReplyRequest()));
    }

    @DisplayName("댓글을 성공적으로 작성한 경우")
    @Test
    void isParentReplyCreatedSuccessfully() {
        given(userRepository.findByIdAndIsDeletedFalse(2L)).willReturn(Optional.of(createUser2()));
        given(matchPostRepository.findById(1L)).willReturn(Optional.of(createMatchPost()));

        matchPostService.saveReply(2L, createSaveParentReplyRequest());
    }

    @DisplayName("대댓글을 성공적으로 작성한 경우")
    @Test
    void isChildReplyCreatedSuccessfully() {
        given(userRepository.findByIdAndIsDeletedFalse(1L)).willReturn(Optional.of(createUser()));
        given(matchPostRepository.findById(1L)).willReturn(Optional.of(createMatchPost()));
        given(replyRepository.findById(1L)).willReturn(Optional.of(createParentReply()));

        matchPostService.saveReply(1L, createSaveChildReplyRequest());
    }
}