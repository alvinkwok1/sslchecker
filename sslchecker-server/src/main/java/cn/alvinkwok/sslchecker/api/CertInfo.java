package cn.alvinkwok.sslchecker.api;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class CertInfo {
    /**
     * 证书主题
     */
    private CertSubjectInfo subject;
    /**
     * 证书颁发者
     */
    private CertIssuerInfo issuer;
    /**
     * 证书生效时间
     */
    private String notBefore;
    /**
     * 证书失效时间
     */
    private String notAfter;
    /**
     * 证书签名算法
     */
    private String sigAlgName;
    /**
     * 证书版本
     */
    private String version;
    /**
     * 证书序列号
     */
    private String serialNumber;
    /**
     * 证书公钥
     */
    private String publicKey;
    /**
     * 证书公钥算法
     */
    private String subjectAlternativeNames;

    /**
     * 证书指纹
     */
    private String fingerprint;
}