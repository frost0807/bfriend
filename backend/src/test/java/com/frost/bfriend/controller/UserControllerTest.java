//package com.frost.bfriend.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.frost.bfriend.common.util.cookie.CookieHandler;
//import com.frost.bfriend.common.util.interceptor.LoginCheckInterceptor;
//import com.frost.bfriend.exception.user.CertificationCodeNotFoundException;
//import com.frost.bfriend.exception.user.DuplicatedEmailException;
//import com.frost.bfriend.exception.user.DuplicatedPhoneException;
//import com.frost.bfriend.exception.user.IncorrectCertificationCodeException;
//import com.frost.bfriend.service.UserService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.nio.charset.StandardCharsets;
//
//import static com.frost.bfriend.dto.UserDto.EmailCertificationRequest;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(UserController.class)
//@MockBean(JpaMetamodelMappingContext.class)
//public class UserControllerTest {
//
//    @MockBean
//    UserService userService;
//
//    @MockBean
//    CookieHandler cookieHandler;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @MockBean
//    LoginCheckInterceptor loginCheckInterceptor;
//
//    @Test
//    @DisplayName("?????? ????????? ?????? ??????")
//    void isDuplicatedEmailExceptionBeingThrownCorrectly() throws Exception {
////        given(userService.existByEmail(anyString())).willReturn(true);
////
////        mockMvc.perform(
////                        get("/users/email/{email}", "frost@email.com"))
////                .andDo(print())
////                .andExpect(content().string("true"));
////
////        verify(userService).existByEmail(anyString());
//    }
////
////    @Test
////    @DisplayName("?????? ????????? ?????? ??????")
////    void isDuplicatedEmailCheckSucceed() throws Exception {
////        given(userService.existByEmail(anyString())).willReturn(true);
////
////        mockMvc.perform(
////                        get("/users/email/{email}", "frost@email.com"))
////                .andDo(print())
////                .andExpect(status().isOk());
////
////        verify(userService).existByEmail(anyString());
////    }
////
////    @Test
////    @DisplayName("?????? ????????? ?????? ?????? ??????")
////    void isDuplicatedPhoneExceptionBeingThrownCorrectly() throws Exception {
////        doThrow(new DuplicatedPhoneException("????????? ????????? ???????????????."))
////                .when(userService).existsByPhone(anyString());
////
////        mockMvc.perform(
////                        get("/users/phone/{phone}", "01012345678"))
////                .andDo(print())
////                .andExpect(status().isConflict())
////                .andExpect(jsonPath("$.code").value("DuplicatedPhoneException"))
////                .andExpect(jsonPath("$.message").value("????????? ????????? ???????????????."));
////
////        verify(userService).existsByPhone(anyString());
////    }
////
////    @Test
////    @DisplayName("?????? ????????? ?????? ?????? ??????")
////    void isDuplicatedPhoneCheckSucceed() throws Exception {
////        given(userService.existsByPhone(anyString())).willReturn(true);
////
////        mockMvc.perform(
////                        get("/users/phone/{phone}", "01012345678"))
////                .andDo(print())
////                .andExpect(status().isOk());
////
////        verify(userService).existsByPhone(anyString());
////    }
////
////    @Test
////    @DisplayName("????????? ???????????? ?????? ??????")
////    void isEmailCertificationCodeSent() throws Exception {
////        doNothing().when(userService).sendCertificationEmail(anyString());
////
////        mockMvc.perform(
////                get("/users/email/certification/{email}", "frost@mail.com"))
////                .andDo(print())
////                .andExpect(status().isOk());
////
////        verify(userService).sendCertificationEmail(anyString());
////    }
////
////    @Test
////    @DisplayName("????????? ???????????? ?????? ??????????????? ??????")
////    void isEmailCertificationCodeRemoved() throws Exception {
////        doThrow(new CertificationCodeNotFoundException("?????? ????????? ??????????????? ???????????? ????????????."))
////                .when(userService).checkEmailCertificationCode(any(EmailCertificationRequest.class));
////
////        mockMvc.perform(
////                post("/users/email/certification")
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .characterEncoding(StandardCharsets.UTF_8)
////                        .content(objectMapper.writeValueAsString(new EmailCertificationRequest("frost@mail.com", "1q2w3e4r"))))
////                .andDo(print())
////                .andExpect(status().isNotFound())
////                .andExpect(jsonPath("$.code").value("CertificationCodeNotFoundException"))
////                .andExpect(jsonPath("$.message").value("?????? ????????? ??????????????? ???????????? ????????????."));
////
////        verify(userService).checkEmailCertificationCode(any(EmailCertificationRequest.class));
////    }
////
////    @Test
////    @DisplayName("????????? ???????????? ?????????")
////    void isEmailCertificationCodeIncorrect() throws Exception {
////        doThrow(new IncorrectCertificationCodeException("?????? ??????????????? ???????????? ????????????."))
////                .when(userService).checkEmailCertificationCode(any(EmailCertificationRequest.class));
////
////        mockMvc.perform(
////                post("/users/email/certification")
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .characterEncoding(StandardCharsets.UTF_8)
////                        .content(objectMapper.writeValueAsString(new EmailCertificationRequest("frost@mail.com", "1q2w3e4r"))))
////                .andDo(print())
////                .andExpect(status().isNotFound())
////                .andExpect(jsonPath("$.code").value("IncorrectCertificationCodeException"))
////                .andExpect(jsonPath("$.message").value("?????? ??????????????? ???????????? ????????????."));
////
////        verify(userService).checkEmailCertificationCode(any(EmailCertificationRequest.class));
////    }
////
////    @Test
////    @DisplayName("????????? ???????????? ??????")
////    void isEmailCertificationCodeCorrect() throws Exception {
////        doNothing().when(userService).checkEmailCertificationCode(any(EmailCertificationRequest.class));
////
////        mockMvc.perform(
////                        post("/users/email/certification")
////                                .contentType(MediaType.APPLICATION_JSON)
////                                .characterEncoding(StandardCharsets.UTF_8)
////                                .content(objectMapper.writeValueAsString(new EmailCertificationRequest("frost@mail.com", "1q2w3e4r"))))
////                .andDo(print())
////                .andExpect(status().isOk());
////
////        verify(userService).checkEmailCertificationCode(any(EmailCertificationRequest.class));
////    }
//}