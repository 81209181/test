package com.hkt.btu.common.core.service;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Map;

public interface BtuSensitiveDataService {
    String decryptToStringSafe(byte[] cipherMessage);
    String decryptToString(byte[] cipherMessage) throws GeneralSecurityException;
    byte[] decrypt(byte[] cipherMessage) throws GeneralSecurityException;

    byte[] encryptFromStringSafe(String plaintext);
    byte[] encryptFromString(String plaintext) throws GeneralSecurityException;
    byte[] encrypt(byte[] plaintext) throws GeneralSecurityException;

    @SuppressWarnings("unused") // used in BtuCacheEnum
    void reloadCachedKeys();
    @SuppressWarnings("unused") // used in BtuCacheEnum
    Map<Integer, Key> loadCachedKey();
}
