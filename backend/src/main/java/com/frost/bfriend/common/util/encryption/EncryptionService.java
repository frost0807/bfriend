package com.frost.bfriend.common.util.encryption;

public interface EncryptionService {
    public String encrypt(String rawPassword);

    public boolean isSamePassword(String rawPassword, String encodedPassword);
}
