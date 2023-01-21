package com.frost.bfriend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frost.bfriend.exception.user.CertificationCodeNotFoundException;
import com.frost.bfriend.exception.user.DuplicatedEmailException;
import com.frost.bfriend.exception.user.DuplicatedPhoneException;
import com.frost.bfriend.exception.user.IncorrectCertificationCodeException;
import com.frost.bfriend.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static com.frost.bfriend.dto.UserDto.EmailCertificationRequest;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class UserControllerTest {

    @MockBean
    UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("중복 이메일 검증 실패")
    void isDuplicatedEmailExceptionBeingThrownCorrectly() throws Exception {
        doThrow(new DuplicatedEmailException("중복된 이메일입니다."))
                .when(userService).isEmailDuplicated(anyString());

        mockMvc.perform(
                        get("/users/email/{email}", "frost@email.com"))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("DuplicatedEmailException"))
                .andExpect(jsonPath("$.message").value("중복된 이메일입니다."));

        verify(userService).isEmailDuplicated(anyString());
    }

    @Test
    @DisplayName("중복 이메일 검증 성공")
    void isDuplicatedEmailCheckSucceed() throws Exception {
        given(userService.isEmailDuplicated(anyString())).willReturn(true);

        mockMvc.perform(
                        get("/users/email/{email}", "frost@email.com"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).isEmailDuplicated(anyString());
    }

    @Test
    @DisplayName("중복 휴대폰 번호 검증 실패")
    void isDuplicatedPhoneExceptionBeingThrownCorrectly() throws Exception {
        doThrow(new DuplicatedPhoneException("중복된 휴대폰 번호입니다."))
                .when(userService).isPhoneDuplicated(anyString());

        mockMvc.perform(
                        get("/users/phone/{phone}", "01012345678"))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("DuplicatedPhoneException"))
                .andExpect(jsonPath("$.message").value("중복된 휴대폰 번호입니다."));

        verify(userService).isPhoneDuplicated(anyString());
    }

    @Test
    @DisplayName("중복 휴대폰 번호 검증 성공")
    void isDuplicatedPhoneCheckSucceed() throws Exception {
        given(userService.isPhoneDuplicated(anyString())).willReturn(true);

        mockMvc.perform(
                        get("/users/phone/{phone}", "01012345678"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).isPhoneDuplicated(anyString());
    }

    @Test
    @DisplayName("이메일 인증코드 전송 성공")
    void isEmailCertificationCodeSent() throws Exception {
        doNothing().when(userService).sendCertificationEmail(anyString());

        mockMvc.perform(
                get("/users/email/certification/{email}", "frost@mail.com"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).sendCertificationEmail(anyString());
    }

    @Test
    @DisplayName("이메일 인증코드 입력 시간초과로 실패")
    void isEmailCertificationCodeRemoved() throws Exception {
        doThrow(new CertificationCodeNotFoundException("해당 메일의 인증코드가 존재하지 않습니다."))
                .when(userService).checkEmailCertificationCode(any(EmailCertificationRequest.class));

        mockMvc.perform(
                post("/users/email/certification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(new EmailCertificationRequest("frost@mail.com", "1q2w3e4r"))))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("CertificationCodeNotFoundException"))
                .andExpect(jsonPath("$.message").value("해당 메일의 인증코드가 존재하지 않습니다."));

        verify(userService).checkEmailCertificationCode(any(EmailCertificationRequest.class));
    }

    @Test
    @DisplayName("이메일 인증코드 불일치")
    void isEmailCertificationCodeIncorrect() throws Exception {
        doThrow(new IncorrectCertificationCodeException("메일 인증코드가 일치하지 않습니다."))
                .when(userService).checkEmailCertificationCode(any(EmailCertificationRequest.class));

        mockMvc.perform(
                post("/users/email/certification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(new EmailCertificationRequest("frost@mail.com", "1q2w3e4r"))))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("IncorrectCertificationCodeException"))
                .andExpect(jsonPath("$.message").value("메일 인증코드가 일치하지 않습니다."));

        verify(userService).checkEmailCertificationCode(any(EmailCertificationRequest.class));
    }

    @Test
    @DisplayName("이메일 인증코드 일치")
    void isEmailCertificationCodeCorrect() throws Exception {
        doNothing().when(userService).checkEmailCertificationCode(any(EmailCertificationRequest.class));

        mockMvc.perform(
                        post("/users/email/certification")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .content(objectMapper.writeValueAsString(new EmailCertificationRequest("frost@mail.com", "1q2w3e4r"))))
                .andDo(print())
                .andExpect(status().isOk());

        verify(userService).checkEmailCertificationCode(any(EmailCertificationRequest.class));
    }
}