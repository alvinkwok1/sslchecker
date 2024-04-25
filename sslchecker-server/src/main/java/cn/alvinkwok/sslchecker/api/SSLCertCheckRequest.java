package cn.alvinkwok.sslchecker.api;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Description
 * 证书检查请求
 *
 * @author alvinkwok
 * @since 2024/4/25
 */
@Getter
@Setter
public class SSLCertCheckRequest {
    /**
     * 待检查的主机名
     * 1. 可以是域名
     * 2. 可以是IP地址
     */
    @NotBlank(message = "主机名不能为空")
    private String hostname;

    /**
     * 主机端口
     */
    private Integer port;
}
