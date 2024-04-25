package cn.alvinkwok.sslchecker.service;

import cn.alvinkwok.sslchecker.api.*;
import cn.alvinkwok.sslchecker.exception.SSLCheckerException;
import cn.alvinkwok.sslchecker.util.DateUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.net.ssl.*;
import javax.security.auth.x500.X500Principal;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * Description
 * 证书检查服务
 *
 * @author alvinkwok
 * @since 2024/4/25
 */
@Service
public class SslCheckService implements InitializingBean {


    public SSLCertCheckResponse checkWebCert(SSLCertCheckRequest request) {
        // 打开链接，请求证书
        Certificate[] certs = this.getCerts(request);

        // 对证书进行解析
        List<CertInfo> certInfoList = parseCerts(certs);

        // 返回信息
        SSLCertCheckResponse response = new SSLCertCheckResponse();
        response.setCertInfoList(certInfoList);
        return response;
    }


    private Certificate[] getCerts(SSLCertCheckRequest request) {
        try {
            // 拼装完整的url地址
            // TODO 需要做一些校验
            String address = "https://" + request.getHostname() + ":" + request.getPort();
            URL serverUrl = new URL(address);
            // 打开链接，请求证书
            HttpsURLConnection connection = (HttpsURLConnection) serverUrl.openConnection();
            connection.connect();
            return connection.getServerCertificates();
        } catch (SocketTimeoutException e) {
            throw new SSLCheckerException("连接超时", e);
        } catch (Exception e) {
            throw new SSLCheckerException("获取证书失败", e);
        }
    }

    private List<CertInfo> parseCerts(Certificate[] certs) {
        List<CertInfo> certInfoList = new ArrayList<>();
        for (Certificate cert : certs) {
            if (cert instanceof X509Certificate) {
                X509Certificate x509Cert = (X509Certificate) cert;
                try {
                    CertInfo certInfo = parseCert(x509Cert);
                    certInfoList.add(certInfo);
                } catch (Exception e) {
                    throw new SSLCheckerException("解析证书失败", e);
                }
            }
        }
        return certInfoList;
    }

    private CertInfo parseCert(X509Certificate x509Cert) throws Exception {
        CertInfo certInfo = new CertInfo();
        certInfo.setSubject(parseSubject(x509Cert));
        certInfo.setIssuer(parseIssuer(x509Cert));
        certInfo.setNotBefore(DateUtil.formatNormalTime(x509Cert.getNotBefore()));
        certInfo.setNotAfter(DateUtil.formatNormalTime(x509Cert.getNotAfter()));
        certInfo.setSigAlgName(x509Cert.getSigAlgName());
        certInfo.setVersion(String.valueOf(x509Cert.getVersion()));
        certInfo.setSerialNumber(x509Cert.getSerialNumber().toString());
        certInfo.setPublicKey(convertPublicKeyToPEM(x509Cert.getPublicKey()));
        if (Objects.nonNull(x509Cert.getIssuerAlternativeNames())) {
            certInfo.setSubjectAlternativeNames(x509Cert.getSubjectAlternativeNames().toString());
        }
        certInfo.setFingerprint(getCertificateFingerprint(x509Cert));
        return certInfo;
    }

    private String convertPublicKeyToPEM(PublicKey publicKey) {
        String publicKeyPEM = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        publicKeyPEM = "-----BEGIN PUBLIC KEY-----\n" + publicKeyPEM + "\n-----END PUBLIC KEY-----";
        return publicKeyPEM;
    }

    private CertIssuerInfo parseIssuer(X509Certificate x509Cert) {
        CertIssuerInfo issuer = new CertIssuerInfo();
        X500Principal principal = x509Cert.getIssuerX500Principal();
        String issuerStr = principal.getName();
        String[] parts = issuerStr.split(",");
        Map<String, String> issuerMap = new HashMap<>();
        for (String part : parts) {
            String[] keyValue = part.trim().split("=");
            if (keyValue.length == 2) {
                issuerMap.put(keyValue[0], keyValue[1]);
            }
        }
        issuer.setCountry(issuerMap.get("C"));
        issuer.setState(issuerMap.get("ST"));
        issuer.setLocality(issuerMap.get("L"));
        issuer.setOrganization(issuerMap.get("O"));
        issuer.setOrganizationUnit(issuerMap.get("OU"));
        issuer.setCommonName(issuerMap.get("CN"));
        return issuer;
    }


    private CertSubjectInfo parseSubject(X509Certificate x509Cert) {
        CertSubjectInfo subject = new CertSubjectInfo();
        X500Principal principal = x509Cert.getSubjectX500Principal();
        String subjectStr = principal.getName();
        String[] parts = subjectStr.split(",");
        Map<String, String> subjectMap = new HashMap<>();
        for (String part : parts) {
            String[] keyValue = part.trim().split("=");
            if (keyValue.length == 2) {
                subjectMap.put(keyValue[0], keyValue[1]);
            }
        }
        subject.setCountry(subjectMap.get("C"));
        subject.setState(subjectMap.get("ST"));
        subject.setLocality(subjectMap.get("L"));
        subject.setOrganization(subjectMap.get("O"));
        subject.setOrganizationUnit(subjectMap.get("OU"));
        subject.setCommonName(subjectMap.get("CN"));
        return subject;
    }

    private String getCertificateFingerprint(Certificate cert) {
        try {
            byte[] encodedCert = cert.getEncoded();
            MessageDigest sha1Digest = MessageDigest.getInstance("SHA-1");
            byte[] digest = sha1Digest.digest(encodedCert);
            return bytesToHex(digest);
        } catch (Exception e) {
            throw new RuntimeException("Error computing certificate fingerprint", e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 3 - 1];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 3] = hexArray[v >>> 4];
            hexChars[j * 3 + 1] = hexArray[v & 0x0F];
            if (j < bytes.length - 1) {
                hexChars[j * 3 + 2] = ':';
            }
        }
        return new String(hexChars);
    }

    /**
     * 初始化的时候信任所有证书，因为我们不是为了通讯，而是为了获取证书信息
     *
     * @throws Exception 异常
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] arg0, String arg1) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] arg0, String arg1) {
                    }
                }
        };
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());

        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        // Create all-trusting host name verifier
        HostnameVerifier validHosts = (arg0, arg1) -> true;
        // All hosts will be valid
        HttpsURLConnection.setDefaultHostnameVerifier(validHosts);
    }
}
