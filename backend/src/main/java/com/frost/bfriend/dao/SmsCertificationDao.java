package com.frost.bfriend.dao;

import com.frost.bfriend.constants.SmsConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

import static com.frost.bfriend.constants.SmsConstants.*;

@Repository
@RequiredArgsConstructor
public class SmsCertificationDao implements CertificationDao {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void saveCertificationCode(String phone, String certificationCode) {
        redisTemplate.opsForValue()
                .set(CERTIFICATION_KEY + phone, certificationCode,
                        Duration.ofMinutes(CERTIFICATION_DURATION));
    }

    public boolean existByPhone(String phone) {
        return redisTemplate.hasKey(CERTIFICATION_KEY + phone);
    }

    @Override
    public String getCertificationCode(String phone) {
        return redisTemplate.opsForValue().get(CERTIFICATION_KEY + phone);
    }

    @Override
    public void removeCertificationCode(String phone) {
        redisTemplate.delete(CERTIFICATION_KEY + phone);
    }
}