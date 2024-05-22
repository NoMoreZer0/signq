package com.kz.signq.utils;

import com.kz.signq.db.DigitalSignatureDb;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.cert.X509Certificate;

@Component
@RequiredArgsConstructor
public class SignatureCheckUtil {

    private static final String ISSUER = "ҰЛТТЫҚ КУӘЛАНДЫРУШЫ ОРТАЛЫҚ (RSA)";

    private final DigitalSignatureDb db;

    public boolean checkExistence(String data, String userIin) {
        var opt = db.findByData(data);
        return opt.isPresent() && opt.get().getUserIin().equals(userIin);
    }

    public boolean checkSubjectIIN(X509Certificate certificate, String userIin) {
        String subjectDN = certificate.getSubjectX500Principal().getName("RFC1779");
        String iin = findAttribute(subjectDN, "OID.2.5.4.5");

        if (iin.contains("IIN")) {
            iin = iin.replace("IIN", "");
        }

        return iin.equals(userIin);
    }

    public boolean checkSubjectIIN(String iin, String userIin) {
        return iin.equals(userIin);
    }

    public boolean checkIssuer(X509Certificate certificate) {
        String issuerDN = certificate.getIssuerX500Principal().getName();
        String commonName = findAttribute(issuerDN, "CN");
        return ISSUER.equals(commonName);
    }

    public boolean checkIssuer(String commonName) {
        return ISSUER.equals(commonName);
    }


    private String findAttribute(String dn, String attributeType) {
        var dnSplits = dn.split(",");
        for (String dnSplit : dnSplits) {
            if (dnSplit.contains(attributeType)) {
                var cnSplits = dnSplit.trim().split("=");
                if (cnSplits[1] != null) {
                    return cnSplits[1].trim();
                }
            }
        }
        return "";
    }
}
