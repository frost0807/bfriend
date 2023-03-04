package com.frost.bfriend.service;

import com.frost.bfriend.dto.ReplyDto.SaveReplyRequest;
import com.frost.bfriend.entity.MatchPost;
import com.frost.bfriend.entity.Reply;
import com.frost.bfriend.entity.User;
import com.frost.bfriend.exception.matchpost.ForbiddenReplyException;
import com.frost.bfriend.exception.matchpost.MatchPostNotFoundException;
import com.frost.bfriend.exception.matchpost.ReplyNotFoundException;
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

import static com.frost.bfriend.dto.ReplyDto.UpdateReplyRequest;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
    private MatchPost matchPost;
    private Reply parentReply;
    private Reply childReply;
    private SaveReplyRequest saveParentReplyRequest;
    private SaveReplyRequest saveChildReplyRequest;
    private UpdateReplyRequest updateParentReplyRequest;
    private UpdateReplyRequest updateChildReplyRequest;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .id(1L)
                .email("guest1@bfriend.com")
                .activityPoint(0)
                .build();

        user2 = User.builder()
                .id(2L)
                .email("guest2@bfriend.com")
                .activityPoint(0)
                .build();

        user3 = User.builder()
                .id(3L)
                .email("guest3@bfriend.com")
                .build();

        matchPost = MatchPost.builder()
                .id(1L)
                .writer(createUser())
                .build();

        parentReply = Reply.builder()
                .id(1L)
                .user(createUser2())
                .matchPost(createMatchPost())
                .build();

        childReply = Reply.builder()
                .id(2L)
                .user(createUser())
                .matchPost(createMatchPost())
                .parentReply(createParentReply())
                .build();

        saveParentReplyRequest = SaveReplyRequest.builder()
                .matchPostId(1L)
                .comment("reply")
                .build();

        saveChildReplyRequest = SaveReplyRequest.builder()
                .matchPostId(1L)
                .parentReplyId(1L)
                .comment("reply")
                .build();

        updateParentReplyRequest = UpdateReplyRequest.builder()
                .replyId(1L)
                .matchPostId(1L)
                .comment("댓글입니다.")
                .build();

        updateChildReplyRequest = UpdateReplyRequest.builder()
                .replyId(2L)
                .parentReplyId(1L)
                .matchPostId(1L)
                .comment("대댓글입니다.")
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
        return matchPost;
    }

    private Reply createParentReply() {
        return parentReply;
    }

    private Reply createChildReply() {
        return childReply;
    }

    private SaveReplyRequest createSaveParentReplyRequest() {
        return saveParentReplyRequest;
    }

    private SaveReplyRequest createSaveChildReplyRequest() {
        return saveChildReplyRequest;
    }

    private UpdateReplyRequest createUpdateParentReplyRequest() {
        return updateParentReplyRequest;
    }

    private UpdateReplyRequest createUpdateChildReplyRequest() {
        return updateChildReplyRequest;
    }

    @DisplayName("댓글, 대댓글을 작성하는데 게시물이 존재하지 않거나 삭제되었을 경우")
    @Test
    void isMatchPostNotFoundException() {
        given(userRepository.findByIdAndIsDeletedFalse(1L)).willReturn(Optional.of(user1));
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

        assertThrows(ReplyNotFoundException.class,
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
        createUser2().increaseActivityPoint(5);
        userRepository.save(createUser2());
    }

    @DisplayName("대댓글을 성공적으로 작성한 경우")
    @Test
    void isChildReplyCreatedSuccessfully() {
        given(userRepository.findByIdAndIsDeletedFalse(1L)).willReturn(Optional.of(createUser()));
        given(matchPostRepository.findById(1L)).willReturn(Optional.of(createMatchPost()));
        given(replyRepository.findById(1L)).willReturn(Optional.of(createParentReply()));

        matchPostService.saveReply(1L, createSaveChildReplyRequest());
        createUser().increaseActivityPoint(5);
        userRepository.save(createUser());
    }

    @DisplayName("본인의 댓글이 아닌 댓글을 수정하려고 하는 경우")
    @Test
    void isForbiddenReplyExceptionThrown() {
        given(userRepository.findByIdAndIsDeletedFalse(3L)).willReturn(Optional.of(createUser3()));
        given(matchPostRepository.findById(1L)).willReturn(Optional.of(createMatchPost()));
        given(replyRepository.findByIdAndIsDeletedFalse(1L)).willReturn(Optional.of(createParentReply()));

        assertThrows(ForbiddenReplyException.class,
                () -> matchPostService.updateReply(3L, createUpdateParentReplyRequest()));
    }

    @DisplayName("댓글이 존재하지 않는 대댓글을 수정하려고 하는 경우")
    @Test
    void isReplyNotFoundExceptionThrown() {
        given(userRepository.findByIdAndIsDeletedFalse(1L)).willReturn(Optional.of(createUser()));
        given(matchPostRepository.findById(1L)).willReturn(Optional.of(createMatchPost()));
        given(replyRepository.findByIdAndIsDeletedFalse(2L)).willReturn(Optional.of(createChildReply()));
        given(replyRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(ReplyNotFoundException.class,
                () -> matchPostService.updateReply(1L, createUpdateChildReplyRequest()));

        verify(userRepository, times(1)).findByIdAndIsDeletedFalse(1L);
        verify(matchPostRepository, times(1)).findById(1L);
        verify(replyRepository, times(1)).findByIdAndIsDeletedFalse(2L);
        verify(replyRepository, times(1)).findById(1L);
    }

    @DisplayName("댓글이 성공적으로 수정된 경우")
    @Test
    void isParentReplyUpdatedSuccessfully() {
        given(userRepository.findByIdAndIsDeletedFalse(2L)).willReturn(Optional.of(createUser2()));
        given(matchPostRepository.findById(1L)).willReturn(Optional.of(createMatchPost()));
        given(replyRepository.findByIdAndIsDeletedFalse(1L)).willReturn(Optional.of(createParentReply()));

        matchPostService.updateReply(2L, createUpdateParentReplyRequest());

        verify(userRepository, times(1)).findByIdAndIsDeletedFalse(2L);
        verify(matchPostRepository, times(1)).findById(1L);
        verify(replyRepository, times(1)).findByIdAndIsDeletedFalse(1L);
        verify(replyRepository, times(1)).save(any(Reply.class));
    }

    @DisplayName("대댓글이 성공적으로 수정된 경우")
    @Test
    void isChildReplyUpdatedSuccessfully() {
        given(userRepository.findByIdAndIsDeletedFalse(1L)).willReturn(Optional.of(createUser()));
        given(matchPostRepository.findById(1L)).willReturn(Optional.of(createMatchPost()));
        given(replyRepository.findByIdAndIsDeletedFalse(2L)).willReturn(Optional.of(createChildReply()));
        given(replyRepository.findById(1L)).willReturn(Optional.of(createParentReply()));

        matchPostService.updateReply(1L, createUpdateChildReplyRequest());

        verify(userRepository, times(1)).findByIdAndIsDeletedFalse(1L);
        verify(matchPostRepository, times(1)).findById(1L);
        verify(replyRepository, times(1)).findByIdAndIsDeletedFalse(2L);
        verify(replyRepository, times(1)).save(any(Reply.class));
    }

    @DisplayName("댓글이 성공적으로 삭제된 경우")
    @Test
    void isParentReplyDeletedSuccessfully() {
        given(userRepository.findByIdAndIsDeletedFalse(2L)).willReturn(Optional.of(createUser2()));
        given(replyRepository.findByIdAndIsDeletedFalse(1L)).willReturn(Optional.of(createParentReply()));

        matchPostService.deleteReply(2L, 1L);

        verify(userRepository, times(1)).findByIdAndIsDeletedFalse(2L);
        verify(replyRepository, times(1)).findByIdAndIsDeletedFalse(1L);
        verify(replyRepository, times(1)).save(any());
    }

    @DisplayName("대댓글이 성공적으로 삭제된 경우")
    @Test
    void isChildReplyDeletedSuccessfully() {
        given(userRepository.findByIdAndIsDeletedFalse(1L)).willReturn(Optional.of(createUser()));
        given(replyRepository.findByIdAndIsDeletedFalse(2L)).willReturn(Optional.of(createChildReply()));

        matchPostService.deleteReply(1L, 2L);

        verify(userRepository, times(1)).findByIdAndIsDeletedFalse(1L);
        verify(replyRepository, times(1)).findByIdAndIsDeletedFalse(2L);
        verify(replyRepository, times(1)).save(any());
    }
}