package com.frost.bfriend.service;

import com.frost.bfriend.dao.EmailCertificationCodeDao;
import com.frost.bfriend.dao.SmsCertificationDao;
import com.frost.bfriend.dto.UserDto.EmailCertificationRequest;
import com.frost.bfriend.dto.UserDto.SaveRequest;
import com.frost.bfriend.exception.user.CertificationCodeNotFoundException;
import com.frost.bfriend.exception.user.DuplicatedEmailException;
import com.frost.bfriend.exception.user.DuplicatedPhoneException;
import com.frost.bfriend.exception.user.IncorrectCertificationCodeException;
import com.frost.bfriend.repository.UserRepository;
import com.frost.bfriend.util.certification.EmailService;
import com.frost.bfriend.util.certification.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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


    public boolean isEmailDuplicated(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean isPhoneDuplicated(String phone) {
        return userRepository.existsByPhone(phone);
    }

    public void sendCertificationEmail(String email) {
        String certificationCode = emailService.sendCertificationEmail(email);

        emailCertificationCodeDao.saveCertificationCode(email, certificationCode);
    }

    public void checkEmailCertificationCode(EmailCertificationRequest requestDto) {
        if (!emailCertificationCodeDao.existByEmail(requestDto.getEmail())) {
            throw new CertificationCodeNotFoundException("해당 메일의 인증 코드가 존재하지 않습니다.");
        }
        if (!emailCertificationCodeDao.getCertificationCode(requestDto.getEmail())
                .equals(requestDto.getCertificationCode())) {
            throw new IncorrectCertificationCodeException("메일 인증 코드가 일치하지 않습니다.");
        }
        emailCertificationCodeDao.removeCertificationCode(requestDto.getEmail());
    }

    public void sendCertificationSms(String phone) {
        String certificationCode = smsService.sendCertificationSms(phone);

        smsCertificationDao.saveCertificationCode(phone, certificationCode);
    }

    public void checkSmsCertificationCode(SmsCertificationRequest requestDto) {
        if(!smsCertificationDao.existByPhone(requestDto.getPhone())) {
            throw new CertificationCodeNotFoundException("해당 휴대폰의 인증 코드가 존재하지 않습니다.");
        }
        if(!smsCertificationDao.getCertificationCode(requestDto.getPhone())
                .equals(requestDto.getCertificationCode())) {
            throw new IncorrectCertificationCodeException("SMS 인증 코드가 일치하지 않습니다.");
        }
        smsCertificationDao.removeCertificationCode(requestDto.getPhone());
    }

    public void createUser(SaveRequest saveRequest) {
        if (isEmailDuplicated(saveRequest.getEmail())) {
            throw new DuplicatedEmailException("중복된 이메일입니다.");
        }
        if (isPhoneDuplicated(saveRequest.getPhone())) {
            throw new DuplicatedPhoneException("중복된 휴대폰 번호입니다.");
        }
    }
}