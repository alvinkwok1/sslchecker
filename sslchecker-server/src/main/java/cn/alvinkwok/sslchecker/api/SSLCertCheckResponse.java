package cn.alvinkwok.sslchecker.api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Description
 * 证书检查响应
 *
 * @author alvinkwok
 * @since 2024/4/25
 */
@Getter
@Setter
public class SSLCertCheckResponse {

    private List<CertInfo> certInfoList;
}
