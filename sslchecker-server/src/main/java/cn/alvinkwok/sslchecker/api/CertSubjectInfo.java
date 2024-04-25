package cn.alvinkwok.sslchecker.api;

import lombok.Getter;
import lombok.Setter;

/**
 * Description
 * 证书主体信息
 * @author alvinkwok
 * @since 2024/4/26
 */
@Getter
@Setter
public class CertSubjectInfo {
    /**
     * 原始信息
     */
    private String origin;

    /**
     * CN--证书颁发者的名称
     */
    private String commonName;
    /**
     * O--证书颁发者的组织
     */
    private String organization;
    /**
     * OU--证书颁发者的部门
     */
    private String organizationUnit;

    /**
     * L--证书颁发者的城市
     */
    private String locality;

    /**
     * ST--证书颁发者的省份
     */
    private String state;

    /**
     * C--证书颁发者的国家
     */
    private String country;
}
