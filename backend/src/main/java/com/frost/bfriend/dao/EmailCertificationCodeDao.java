package com.frost.bfriend.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

import static com.frost.bfriend.common.constants.EmailConstants.*;

@Repository
@RequiredArgsConstructor
public class EmailCertificationCodeDao implements CertificationDao {
    private final StringRedisTemplate redisTemplate;

    @Override
    public void saveCertificationCode(String email, String certificationCode) {
        redisTemplate.opsForValue()
                .set(CERTIFICATION_CODE + email, certificationCode,
                        Duration.ofSeconds(CERTIFICATION_CODE_DURATION));
    }

    public boolean existCertificationCodeByEmail(String email) {
        return redisTemplate.hasKey(CERTIFICATION_CODE + email);
    }

    @Override
    public String getCertificationCode(String email) {
        return redisTemplate.opsForValue().get(CERTIFICATION_CODE + email);
    }

    @Override
    public void removeCertificationCode(String email) {
        redisTemplate.delete(CERTIFICATION_CODE + email);
    }

    @Override
    public void saveCertificationIdentifier(String email, String identifier) {
        redisTemplate.opsForValue()
                .set(CERTIFICATION_IDENTIFIER + email, identifier,
                        Duration.ofSeconds(EMAIL_CERTIFICATION_IDENTIFIER_DURATION));
    }

    public boolean existCertificationIdentifierByEmail(String email) {
        return redisTemplate.hasKey(CERTIFICATION_IDENTIFIER + email);
    }

    @Override
    public String getCertificationIdentifier(String email) {
        return redisTemplate.opsForValue().get(CERTIFICATION_IDENTIFIER + email);
    }

    @Override
    public void removeCertificationIdentifier(String email) {
        redisTemplate.delete(CERTIFICATION_IDENTIFIER + email);
    }
}
