package com.frost.bfriend.service;

import com.frost.bfriend.dao.EmailCertificationCodeDao;
import com.frost.bfriend.dao.SmsCertificationDao;
import com.frost.bfriend.dto.UserDto;
import com.frost.bfriend.dto.UserDto.EmailCertificationRequest;
import com.frost.bfriend.dto.UserDto.SaveRequest;
import com.frost.bfriend.entity.User;
import com.frost.bfriend.exception.user.*;
import com.frost.bfriend.repository.UserRepository;
import com.frost.bfriend.util.certification.EmailService;
import com.frost.bfriend.util.certification.SmsService;
import com.frost.bfriend.util.encryption.EncryptionService;
import com.frost.bfriend.util.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.frost.bfriend.dto.UserDto.*;
import static com.frost.bfriend.dto.UserDto.SmsCertificationRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final EmailService emailService;
    private final SmsService smsService;
    private final UserRepository userRepository;
    private final EmailCertificationCodeDao emailCertificationCodeDao;
    private final SmsCertificationDao smsCertificationDao;
    private final EncryptionService encryptionService;
    private final TokenProvider tokenProvider;

    @Transactional(readOnly = true)
    public boolean isEmailDuplicated(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean isPhoneDuplicated(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Transactional
    public void sendCertificationEmail(String email) {
        String certificationCode = emailService.sendCertificationEmail(email);

        emailCertificationCodeDao.saveCertificationCode(email, certificationCode);
    }

    @Transactional
    public String checkEmailCertificationCode(EmailCertificationRequest requestDto) {
        String email = requestDto.getEmail();
        String certificationCode = requestDto.getCertificationCode();
        String identifier = UUID.randomUUID().toString();

        if (!emailCertificationCodeDao.existCertificationCodeByEmail(email)) {
            throw new CertificationCodeNotFoundException("해당 메일의 인증 코드가 존재하지 않습니다.");
        }
        if (!emailCertificationCodeDao.getCertificationCode(email)
                .equals(certificationCode)) {
            throw new IncorrectCertificationCodeException("메일 인증 코드가 일치하지 않습니다.");
        }

        emailCertificationCodeDao.removeCertificationCode(email);
        emailCertificationCodeDao.saveCertificationIdentifier(email, identifier);
        return identifier;
    }

    @Transactional
    public void sendCertificationSms(String phone) {
        String certificationCode = smsService.sendCertificationSms(phone);

        smsCertificationDao.saveCertificationCode(phone, certificationCode);
    }

    @Transactional
    public String checkSmsCertificationCode(SmsCertificationRequest requestDto) {
        String phone = requestDto.getPhone();
        String certificationCode = requestDto.getCertificationCode();
        String identifier = UUID.randomUUID().toString();

        if (!smsCertificationDao.existCertificationCodeByPhone(phone)) {
            throw new CertificationCodeNotFoundException("해당 휴대폰의 인증 코드가 존재하지 않습니다.");
        }
        if (!smsCertificationDao.getCertificationCode(phone)
                .equals(certificationCode)) {
            throw new IncorrectCertificationCodeException("SMS 인증 코드가 일치하지 않습니다.");
        }

        smsCertificationDao.removeCertificationCode(phone);
        smsCertificationDao.saveCertificationIdentifier(phone, identifier);
        return identifier;
    }

    @Transactional
    public void createUser(SaveRequest requestDto, String emailIdentifier, String smsIdentifier) {
        checkDuplicated(requestDto);
        checkEmailCertified(requestDto, emailIdentifier);
        checkSmsCertified(requestDto, smsIdentifier);

        requestDto.encryptPassword(encryptionService);
        userRepository.save(requestDto.toEntity());
    }

    private void checkDuplicated(SaveRequest requestDto) {
        if (isEmailDuplicated(requestDto.getEmail())) {
            throw new DuplicatedEmailException("중복된 이메일입니다.");
        }
        if (isPhoneDuplicated(requestDto.getPhone())) {
            throw new DuplicatedPhoneException("중복된 휴대폰 번호입니다.");
        }
    }

    private void checkEmailCertified(SaveRequest requestDto, String emailIdentifier) {
        if (!emailCertificationCodeDao.existCertificationIdentifierByEmail(requestDto.getEmail())) {
            throw new CertificationIdentifierNotFoundException("이메일 인증 기록이 없습니다.");
        }
        if(!emailCertificationCodeDao.getCertificationIdentifier(requestDto.getEmail()).equals(emailIdentifier)) {
            throw new CertificationIdentifierMismatchException("이메일 인증 기록이 유효하지 않습니다.");
        }
    }

    private void checkSmsCertified(SaveRequest requestDto, String smsIdentifier) {
        if (!smsCertificationDao.existCertificationIdentifierByPhone(requestDto.getPhone())) {
            throw new CertificationIdentifierNotFoundException("휴대폰 인증 기록이 없습니다.");
        }
        if(!smsCertificationDao.getCertificationIdentifier(requestDto.getPhone()).equals(smsIdentifier)) {
            throw new CertificationIdentifierMismatchException("휴대폰 인증 기록이 유효하지 않습니다.");
        }
    }

    public String login(LoginRequest requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 이메일입니다."));

        if(!requestDto.isPasswordCorrect(encryptionService, user.getPassword())) {
            throw new UserNotFoundException("잘못된 비밀번호 입니다.");
        }
        if(user.getIsDeleted()) {
            throw new UserNotFoundException("탈퇴한 사용자 입니다.");
        }

        return tokenProvider.createAccessToken(user);
    }
}