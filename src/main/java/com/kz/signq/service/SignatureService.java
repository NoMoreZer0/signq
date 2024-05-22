package com.kz.signq.service;

import com.kz.signq.exception.SignException;
import com.kz.signq.model.DigitalSignature;
import com.kz.signq.model.Petition;
import com.kz.signq.model.User;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

public interface SignatureService {
    void signApplication(User user, Petition petition, byte[] dataSnapshot, String certificateStore, String password) throws SignException;

    void checkCertificate(User user, Petition petition, byte[] dataSnapshot, String certificateStore, String password) throws SignException;

    byte[] createDataSnapshot(Petition application) throws NoSuchAlgorithmException;

    List<DigitalSignature> getAllSigned(String iin);

    String checkCertificateXml(User user, UUID petitionId, String xml) throws SignException;

    void saveSignatureXml(String userIin, UUID petitionId, String signature);
}
