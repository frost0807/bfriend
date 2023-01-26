package com.frost.bfriend.util.encryption;

public interface EncryptionService {
    public String encrypt(String rawPassword);

    public boolean isSamePassword(String rawPassword, String encodedPassword);
}
