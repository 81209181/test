package com.hkt.btu.sd.core.service;

import java.security.GeneralSecurityException;

@SuppressWarnings("unused")
public interface SdSensitiveDataService {
    byte[] encrypt(byte[] plaintext) throws GeneralSecurityException;
    byte[] encryptFromString(String plaintext) throws GeneralSecurityException;
    byte[] encryptFromStringSafe(String plaintext);

    byte[] decrypt(byte[] cipherMessage) throws GeneralSecurityException;
    String decryptToString(byte[] cipherMessage) throws GeneralSecurityException;
    String decryptToStringSafe(byte[] cipherMessage);

    void clearCachedKeys();
}
