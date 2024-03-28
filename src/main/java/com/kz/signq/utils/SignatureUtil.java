package com.kz.signq.utils;

import com.kz.signq.db.DigitalSignatureDb;
import com.kz.signq.model.DigitalSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.*;

@Component
public class SignatureUtil {

    private final DigitalSignatureDb db;

    @Autowired
    public SignatureUtil(DigitalSignatureDb db) {
        this.db = db;
    }

    public byte[] dataToSha256Bytes(String data) throws NoSuchAlgorithmException {
        var digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(data.getBytes(StandardCharsets.UTF_8));
    }
    public String sha256BytesToHex(byte[] hash) {
        var hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            var hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public KeyStore getKeyStore(String certificateStore, String password) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        var keyStore = KeyStore.getInstance("JKS");
        var decodedBytes = Base64.getDecoder().decode(certificateStore);
        var keyStoreStream = new ByteArrayInputStream(decodedBytes);

        keyStore.load(keyStoreStream, password.toCharArray());

        return keyStore;
    }

    public String getFirstAlias(KeyStore keyStore) throws KeyStoreException {
        var aliases = keyStore.aliases();

        if (!aliases.hasMoreElements()) {
            throw new RuntimeException("Хранилище сертификатов не содержит алиасов");
        }

        return aliases.nextElement();
    }

    public byte[] signData(PrivateKey privateKey, byte[] data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        var signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data);
        return signature.sign();
    }

    public void saveDigitalSignature(UUID petitionId, String userIin, String petitionData, byte[] certificate, byte[] sign) {
        var digitalSignature = DigitalSignature.builder()
                .petitionId(petitionId)
                .dateTime(new Date())
                .userIin(userIin)
                .data(petitionData)
                .certificate(certificate)
                .sign(sign)
                .build();
        db.save(digitalSignature);
    }

    public String objectToString(Object object) {
        var result = new StringBuilder();
        result.append(this.getClass().getName());
        result.append("[");

        var allFields = getAllFields(new ArrayList<>(), object.getClass());

        allFields.stream()
                .filter(field -> !"version".equals(field.getName())) // Filtering out the "version" field
                .forEach(field -> {
                    field.setAccessible(true);
                    Object value;
                    try {
                        value = field.get(object);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    var fieldValue = (value == null || value.equals("")) ? "" : String.valueOf(value);
                    result.append(field.getName()).append(": ").append(fieldValue);
                });
        result.append("]");

        return result.toString();
    }

    private static List<Field> getAllFields(List<Field> fields, Class<?> objectClass) {
        fields.addAll(Arrays.asList(objectClass.getDeclaredFields()));
        if (objectClass.getSuperclass() != null) {
            getAllFields(fields, objectClass.getSuperclass());
        }

        return fields;
    }
}
