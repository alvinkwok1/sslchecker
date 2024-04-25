package cn.alvinkwok.sslchecker.api;

import cn.alvinkwok.sslchecker.service.SslCheckService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SslCheckerApi {

    private final SslCheckService sslCheckService;

    public SslCheckerApi(SslCheckService sslCheckService) {
        this.sslCheckService = sslCheckService;
    }

    /**
     * 检查网站证书
     *
     * @param request 表单提交内容
     */
    @RequestMapping("/check")
    public SSLCertCheckResponse checkWebCert(@RequestBody SSLCertCheckRequest request) {
        return sslCheckService.checkWebCert(request);
    }


    /**
     * 订阅证书到期通知
     *
     * @return
     */
    @RequestMapping("/subscribe")
    public String subscribeNotice() {
        return null;
    }

    /**
     * 取消证书导致订阅通知
     *
     * @return
     */
    @RequestMapping("/unsubscribe")
    public String unSubscribeNotice() {
        return null;
    }

}
