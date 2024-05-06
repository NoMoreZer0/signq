package com.kz.signq.service.impl;

import com.kz.signq.db.DigitalSignatureDb;
import com.kz.signq.exception.SignException;
import com.kz.signq.model.DigitalSignature;
import com.kz.signq.model.Petition;
import com.kz.signq.model.User;
import com.kz.signq.service.SignatureService;
import com.kz.signq.utils.ErrorCodeUtil;
import com.kz.signq.utils.SignatureCheckUtil;
import com.kz.signq.utils.SignatureUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SignatureServiceImpl implements SignatureService {

    private final SignatureUtil signatureUtil;

    private final SignatureCheckUtil signatureCheckUtil;

    private final DigitalSignatureDb db;

    @Override
    public void signApplication(User user, Petition petition, byte[] dataSnapshot, String certificateStore, String password) throws SignException {

        String applicationData;
        byte[] encodedCertificate;
        byte[] sign;
        try {
            applicationData = signatureUtil.sha256BytesToHex(dataSnapshot);
            var keyStore = signatureUtil.getKeyStore(certificateStore, password);
            var alias = signatureUtil.getFirstAlias(keyStore);
            var certificate = (X509Certificate) keyStore.getCertificate(alias);
            var privateKey = (PrivateKey) keyStore.getKey(alias, password.toCharArray());
            encodedCertificate = certificate.getEncoded();
            sign = signatureUtil.signData(privateKey, dataSnapshot);
        } catch (Exception e) {
            throw new SignException(ErrorCodeUtil.ESP_ERROR_GENERAL_ERROR.name(), e.getMessage());
        }
        var iin = user.getIin();
        if (iin == null) {
            throw new SignException(ErrorCodeUtil.ESP_ERROR_IIN_NOT_FOUND.name(), "У пользователя нету ИИН");
        }
        if (signatureUtil.hasSignature(iin, petition.getId())) {
            throw new SignException(ErrorCodeUtil.ESP_ERROR_APPLICATION_ALREADY_SIGNED.name(), "Вы уже подписали эту петицию");
        }
        signatureUtil.saveDigitalSignature(petition.getId(), iin, applicationData, encodedCertificate, sign);
    }

    /**
     * 1. Проверка сущесвует ли подписанная запись
     * 2. Проверика истек ли сертификат
     * 3. Проверить ИИН пользователя, ИИН application и ИИН из сертификата
     * 4. Проверить Issuer Нуц
     * 5. Проверка на отозвонные CRL
     */
    @Override
    public void checkCertificate(User user, Petition petition, byte[] dataSnapshot, String certificateStore, String password) throws SignException {
        X509Certificate certificate;
        String applicationData;
        try {
            var keyStore = signatureUtil.getKeyStore(certificateStore, password);
            var alias = signatureUtil.getFirstAlias(keyStore);
            certificate = (X509Certificate) keyStore.getCertificate(alias);
            applicationData = signatureUtil.sha256BytesToHex(dataSnapshot);
        } catch (IOException e) {
            throw new SignException(ErrorCodeUtil.ESP_PASSWORD_INCORRECT.name(), e.getMessage());
        } catch (Exception e) {
            throw new SignException(ErrorCodeUtil.ESP_ERROR_GENERAL_ERROR.name(), e.getMessage());
        }

        var iin = user.getIin();
        if (iin == null) {
            throw new SignException(ErrorCodeUtil.ESP_ERROR_IIN_NOT_FOUND.name(), "У пользователя нету ИИН");
        }

        if (!signatureCheckUtil.checkSubjectIIN(certificate, iin)) {
            throw new SignException(ErrorCodeUtil.ESP_ERROR_IIN_MISMATCH.name(), "ИИН подписанта не соответствует ИИН из хранилища");
        }

        if (signatureCheckUtil.checkExistence(applicationData, iin)) {
            throw new SignException(ErrorCodeUtil.ESP_ERROR_APPLICATION_ALREADY_SIGNED.name(), "Петиция уже подписана заявителем");
        }

        try {
            certificate.checkValidity();
        } catch (CertificateNotYetValidException e) {
            throw new SignException(ErrorCodeUtil.ESP_ERROR_CERTIFICATE_NOT_EFFECTIVE_YET.name(), "Не наступил срок действия сертификата");
        } catch (CertificateExpiredException e) {
            throw new SignException(ErrorCodeUtil.ESP_ERROR_CERTIFICATE_EXPIRED.name(), "Истек срок действия сертификата");
        }

        if (!signatureCheckUtil.checkIssuer(certificate)) {
            throw new SignException(ErrorCodeUtil.ESP_ERROR_ISSUER_INVALID.name(), "Издатель не является НУЦ");
        }
    }

    @Override
    public List<DigitalSignature> getAllSigned(String iin) {
        return db.findAllByUserIin(iin);
    }

    @Override
    public byte[] createDataSnapshot(Petition petition) throws NoSuchAlgorithmException {
        var applicationString = signatureUtil.objectToString(petition);
        return signatureUtil.dataToSha256Bytes(applicationString);
    }
}
