package com.hkt.btu.common.core.service.impl;

import com.hkt.btu.common.core.service.BtuCacheService;
import com.hkt.btu.common.core.service.BtuSensitiveDataService;
import com.hkt.btu.common.core.service.constant.BtuCacheEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.security.*;
import java.security.cert.CertificateException;

// ref: https://stackoverflow.com/questions/15554296/simple-java-aes-encrypt-decrypt-example
// encryption :     AES-256
// key length:      32 byte (256 bit)
// alias length:    4 byte (32 bit)
// iv length:       16 byte random (128 bit)
// output length:   4(alias length) + 16(iv length)  + encrypted input byte length + 16(GCM auth tag length)
//                  = 36 byte + input byte length
public class BtuSensitiveDataServiceImpl implements BtuSensitiveDataService {

    private static final Logger LOG = LogManager.getLogger(BtuSensitiveDataServiceImpl.class);

    private final SecureRandom SECURE_RANDOM = new SecureRandom();

    // crypto config
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int KEY_ALIAS_BYTE_LENGTH = 4; // 32 bit (int byte length)
    private static final int IV_BIT_LENGTH = 128;
    private static final int IV_BYTE_LENGTH = IV_BIT_LENGTH / 8;

    // keystore
    @Value("${servicedesk.aesKeystore.path}")
    private String KEYSTORE_PATH;
    @Value("${servicedesk.aesKeystore.storePass}")
    private String STORE_PASS;

    // key
    private static final int LATEST_KEY_ALIAS = 1; // [INPUT] alias of latest key
    private static final char[] KEY_PASS = {'s', 'e', 'r', 'v', 'i', 'c', 'e', 'd', 'e', 's', 'k'};

    @Resource(name = "cacheService")
    BtuCacheService cacheService;

    @Override
    public String decryptToStringSafe(byte[] cipherMessage) {
        if (cipherMessage == null) {
            return null;
        }

        try {
            byte[] bytePlaintext = decrypt(cipherMessage);
            return new String(bytePlaintext);
        } catch (GeneralSecurityException | BufferUnderflowException e) {
            LOG.error(e.getMessage(), e);
            return "DECRYPT ERROR";
        }
    }

    @Override
    public String decryptToString(byte[] cipherMessage) throws GeneralSecurityException {
        byte[] bytePlaintext = decrypt(cipherMessage);
        return new String(bytePlaintext);
    }


    @Override
    public byte[] decrypt(byte[] cipherMessage) throws GeneralSecurityException {
        // 1. deserialize byte message (alias + iv + ciphertext)
        ByteBuffer byteBuffer = ByteBuffer.wrap(cipherMessage);

        // 1a. get key
        int alias = byteBuffer.getInt();
        Key key = getKey();
        if (key == null) {
            throw new GeneralSecurityException("Key not found. (alias=" + alias + ")");
        }

        // 1b. get iv
        byte[] iv = new byte[IV_BYTE_LENGTH];
        byteBuffer.get(iv);

        // 1c. get ciphertext
        byte[] ciphertext = new byte[byteBuffer.remaining()];
        byteBuffer.get(ciphertext);

        // 2. decrypt with key and iv
        final Cipher CIPHER = Cipher.getInstance(TRANSFORMATION);
        GCMParameterSpec paramSpec = new GCMParameterSpec(IV_BIT_LENGTH, iv);
        CIPHER.init(Cipher.DECRYPT_MODE, key, paramSpec);

        return CIPHER.doFinal(ciphertext);
    }


    @Override
    public byte[] encryptFromStringSafe(String plaintext) {
        if (StringUtils.isEmpty(plaintext)) {
            return null;
        }

        try {
            byte[] bytePlaintext = plaintext.getBytes();
            return encrypt(bytePlaintext);
        } catch (GeneralSecurityException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }


    @Override
    public byte[] encryptFromString(String plaintext) throws GeneralSecurityException {
        byte[] bytePlaintext = plaintext.getBytes();
        return encrypt(bytePlaintext);
    }

    @Override
    public byte[] encrypt(byte[] plaintext) throws GeneralSecurityException {
        // 0. get latest key
        Key latestKey = getKey();

        // 1. create initialization vector (so that the same plaintext and key will always create different ciphertext)
        byte[] iv = new byte[IV_BYTE_LENGTH];
        SECURE_RANDOM.nextBytes(iv);

        // 2. encrypt with key and iv
        final Cipher CIPHER = Cipher.getInstance(TRANSFORMATION);
        GCMParameterSpec paramSpec = new GCMParameterSpec(IV_BIT_LENGTH, iv);
        CIPHER.init(Cipher.ENCRYPT_MODE, latestKey, paramSpec);
        byte[] ciphertext = CIPHER.doFinal(plaintext);

        // 3. serialize to a byte message: key alias + iv + ciphertext(with auth tag)
        ByteBuffer byteBuffer = ByteBuffer.allocate(KEY_ALIAS_BYTE_LENGTH + iv.length + ciphertext.length);
        byteBuffer.putInt(LATEST_KEY_ALIAS);
        byteBuffer.put(iv);
        byteBuffer.put(ciphertext);

        return byteBuffer.array();
    }

    @Override
    public synchronized void reloadCachedKeys() {
        cacheService.reloadCachedObject(BtuCacheEnum.SENSITIVE_DATA.getCacheName());
    }

    private Key getKey() {
        return (Key) cacheService.getCachedObjectByCacheName(BtuCacheEnum.SENSITIVE_DATA.getCacheName());
    }

    @Override
    public Key loadCachedKey() {
        LOG.info("Cached key (alias={})",LATEST_KEY_ALIAS);
        final String aliasString = String.valueOf(LATEST_KEY_ALIAS);
        if (StringUtils.isEmpty(STORE_PASS)) {
            LOG.error("StorePass is empty.");
            return null;
        }
        // get keystore
        InputStream keystoreStream;
        try {
            keystoreStream = new FileInputStream(KEYSTORE_PATH);
            KeyStore keystore = KeyStore.getInstance("JCEKS");
            keystore.load(keystoreStream, STORE_PASS.toCharArray());
            // check key exist in keystore
            if (!keystore.containsAlias(aliasString)) {
                LOG.error("Alias " + aliasString + " not found in keystore.");
                return null;
            }
            return keystore.getKey(aliasString, KEY_PASS);
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

}
