package com.frost.bfriend.service;

import com.frost.bfriend.dao.EmailCertificationCodeDao;
import com.frost.bfriend.dto.UserDto.EmailCertificationRequest;
import com.frost.bfriend.dto.UserDto.SaveRequest;
import com.frost.bfriend.exception.user.CertificationCodeNotFoundException;
import com.frost.bfriend.exception.user.DuplicatedEmailException;
import com.frost.bfriend.exception.user.DuplicatedPhoneException;
import com.frost.bfriend.exception.user.IncorrectCertificationCodeException;
import com.frost.bfriend.repository.UserRepository;
import com.frost.bfriend.util.certification.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final EmailCertificationCodeDao emailCertificationCodeDao;

    public void createUser(SaveRequest saveRequest) {
        if (isEmailDuplicated(saveRequest.getEmail())) {
            throw new DuplicatedEmailException("중복된 이메일입니다.");
        }
        if (isPhoneDuplicated(saveRequest.getPhone())) {
            throw new DuplicatedPhoneException("중복된 휴대폰 번호입니다.");
        }
    }

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
            throw new CertificationCodeNotFoundException("해당 메일의 인증코드가 존재하지 않습니다.");
        }
        if (!emailCertificationCodeDao.getCertificationCode(requestDto.getEmail())
                .equals(requestDto.getCertificationCode())) {
            throw new IncorrectCertificationCodeException("메일 인증코드가 일치하지 않습니다.");
        }
        removeCode(requestDto);
    }

    private void removeCode(EmailCertificationRequest requestDto) {
        emailCertificationCodeDao.removeCertificationCode(requestDto.getEmail());
    }
}