package com.frost.bfriend.dao;

public interface CertificationDao {
    void saveCertificationCode(String key, String certificationCode);

    String getCertificationCode(String key);

    void removeCertificationCode(String key);

    void saveCertificationIdentifier(String key, String value);

    String getCertificationIdentifier(String key);

    void removeCertificationIdentifier(String key);
}
