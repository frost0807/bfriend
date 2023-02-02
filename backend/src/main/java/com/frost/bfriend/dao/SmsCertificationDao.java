package com.frost.bfriend.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

import static com.frost.bfriend.common.constants.SmsConstants.*;

@Repository
@RequiredArgsConstructor
public class SmsCertificationDao implements CertificationDao {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void saveCertificationCode(String phone, String certificationCode) {
        redisTemplate.opsForValue()
                .set(CERTIFICATION_CODE + phone, certificationCode,
                        Duration.ofSeconds(CERTIFICATION_CODE_DURATION));
    }

    public boolean existCertificationCodeByPhone(String phone) {
        return redisTemplate.hasKey(CERTIFICATION_CODE + phone);
    }

    @Override
    public String getCertificationCode(String phone) {
        return redisTemplate.opsForValue().get(CERTIFICATION_CODE + phone);
    }

    @Override
    public void removeCertificationCode(String phone) {
        redisTemplate.delete(CERTIFICATION_CODE + phone);
    }

    @Override
    public void saveCertificationIdentifier(String phone, String identifier) {
        redisTemplate.opsForValue()
                .set(CERTIFICATION_IDENTIFIER + phone, identifier,
                        Duration.ofSeconds(SMS_CERTIFICATION_IDENTIFIER_EXPIRY_SECONDS));
    }

    public boolean existCertificationIdentifierByPhone(String phone) {
        return redisTemplate.hasKey(CERTIFICATION_IDENTIFIER + phone);
    }

    @Override
    public String getCertificationIdentifier(String phone) {
        return redisTemplate.opsForValue().get(CERTIFICATION_IDENTIFIER + phone);
    }

    @Override
    public void removeCertificationIdentifier(String key) {

    }
}