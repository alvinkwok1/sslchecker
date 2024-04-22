package cn.alvinkwok.sslchecker.web;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class SslCheckerController {

    /**
     * 检查网站证书
     * @param url
     * @param port
     * @return
     */
    public String checkWebCert(String url,String port) {
        return "";
    }


}
