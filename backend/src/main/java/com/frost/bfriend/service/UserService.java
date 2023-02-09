package com.frost.bfriend.service;

import com.frost.bfriend.common.util.certification.EmailService;
import com.frost.bfriend.common.util.certification.SmsService;
import com.frost.bfriend.common.util.encryption.EncryptionService;
import com.frost.bfriend.common.util.jwt.TokenProvider;
import com.frost.bfriend.dao.EmailCertificationCodeDao;
import com.frost.bfriend.dao.SmsCertificationDao;
import com.frost.bfriend.entity.User;
import com.frost.bfriend.exception.user.*;
import com.frost.bfriend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.frost.bfriend.dto.UserDto.*;

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
    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean existByPhone(String phone) {
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
    public void saveUser(SaveRequest requestDto, String emailIdentifier, String smsIdentifier) {
        checkDuplicated(requestDto);
        checkEmailCertified(requestDto, emailIdentifier);
        checkSmsCertified(requestDto, smsIdentifier);

        requestDto.encryptPassword(encryptionService);
        userRepository.save(requestDto.toEntity());
    }

    @Transactional(readOnly = true)
    protected void checkDuplicated(SaveRequest requestDto) {
        if (existByEmail(requestDto.getEmail())) {
            throw new DuplicatedEmailException("중복된 이메일입니다.");
        }
        if (existByPhone(requestDto.getPhone())) {
            throw new DuplicatedPhoneException("중복된 휴대폰 번호입니다.");
        }
    }

    @Transactional(readOnly = true)
    protected void checkEmailCertified(SaveRequest requestDto, String emailIdentifier) {
        if (!emailCertificationCodeDao.existCertificationIdentifierByEmail(requestDto.getEmail())) {
            throw new CertificationIdentifierNotFoundException("이메일 인증 기록이 없습니다.");
        }
        if (!emailCertificationCodeDao.getCertificationIdentifier(requestDto.getEmail()).equals(emailIdentifier)) {
            throw new CertificationIdentifierMismatchException("이메일 인증 기록이 유효하지 않습니다.");
        }
    }

    @Transactional(readOnly = true)
    protected void checkSmsCertified(SaveRequest requestDto, String smsIdentifier) {
        if (!smsCertificationDao.existCertificationIdentifierByPhone(requestDto.getPhone())) {
            throw new CertificationIdentifierNotFoundException("휴대폰 인증 기록이 없습니다.");
        }
        if (!smsCertificationDao.getCertificationIdentifier(requestDto.getPhone()).equals(smsIdentifier)) {
            throw new CertificationIdentifierMismatchException("휴대폰 인증 기록이 유효하지 않습니다.");
        }
    }

    @Transactional(readOnly = true)
    public TokenAndName login(LoginRequest requestDto) {
        User user = getUserByEmail(requestDto.getEmail());
        checkValidUser(requestDto, user);
        String accessToken = tokenProvider.createAccessToken(user);
        String name = user.getName();

        return new TokenAndName(accessToken, name);
    }

    private User getUserByEmail(String email) {
        User user = userRepository.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new UserNotFoundException("이메일 혹은 비밀번호를 잘못 입력하셨습니다."));
        return user;
    }

    private void checkValidUser(LoginRequest requestDto, User user) {
        if (!requestDto.isPasswordCorrect(encryptionService, user.getPassword())) {
            throw new UserNotFoundException("이메일 혹은 비밀번호를 잘못 입력하셨습니다.");
        }
        if (user.getIsSuspended()) {
            throw new UserNotFoundException("정지된 회원입니다.");
        }
    }

    @Transactional
    public void issueTemporaryPassword(TemporaryPasswordRequest requestDto) {
        String email = requestDto.getEmail();
        String name = requestDto.getName();

        if (!userRepository.existsByEmailAndName(email, name)) {
            throw new UserNotFoundException("이메일 혹은 이름을 잘못 입력하셨습니다.");
        }
        User user = getUserByEmail(email);
        String newPassword = emailService.sendTemporaryPasswordEmail(requestDto.getEmail());
        user.updatePassword(encryptionService.encrypt(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public void updatePassword(Long userId, UpdatePasswordRequest requestDto) {
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));

        if (!requestDto.checkPassword(encryptionService, user.getPassword())) {
            throw new WrongPasswordException("기존 비밀번호를 잘못 입력하셨습니다.");
        }
        if (requestDto.isAlreadyMyPassword()) {
            throw new AlreadyMyPasswordException("새 비밀번호가 기존 비밀번호와 동일합니다.");
        }
        requestDto.encryptPassword(encryptionService);
        user.updatePassword(requestDto.getNewPassword());
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId, DeleteRequest requestDto) {
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 사용자입니다."));

        if (!requestDto.checkPassword(encryptionService, user.getPassword())) {
            throw new WrongPasswordException("비밀번호를 잘못 입력하셨습니다.");
        }
        user.deleteUser();
        userRepository.save(user);
    }
}