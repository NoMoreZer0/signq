package com.kz.signq.service;

import com.kz.signq.exception.SignException;
import com.kz.signq.model.Petition;
import com.kz.signq.model.User;

import java.security.NoSuchAlgorithmException;

public interface SignatureService {
    void signApplication(User user, Petition petition, byte[] dataSnapshot, String certificateStore, String password) throws SignException;

    void checkCertificate(User user, Petition petition, byte[] dataSnapshot, String certificateStore, String password) throws SignException;

    byte[] createDataSnapshot(Petition application) throws NoSuchAlgorithmException;
}
