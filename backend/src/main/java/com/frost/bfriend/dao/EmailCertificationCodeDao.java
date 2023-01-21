package com.frost.bfriend.dao;

import com.frost.bfriend.constants.EmailConstants;
import kotlin.OverloadResolutionByLambdaReturnType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

import static com.frost.bfriend.constants.EmailConstants.*;

@Repository
@RequiredArgsConstructor
public class EmailCertificationCodeDao implements CertificationDao{
    private final StringRedisTemplate redisTemplate;

    @Override
    public void saveCertificationCode(String email, String certificationCode) {
        redisTemplate.opsForValue()
                .set(CERTIFICATION_KEY + email, certificationCode,
                        Duration.ofMinutes(CERTIFICATION_DURATION));
    }

    public boolean existByEmail(String email) {
        return redisTemplate.hasKey(CERTIFICATION_KEY + email);
    }

    @Override
    public String getCertificationCode(String email) {
        return redisTemplate.opsForValue().get(CERTIFICATION_KEY + email);
    }

    @Override
    public void removeCertificationCode(String email) {
        redisTemplate.delete(CERTIFICATION_KEY + email);
    }
}
