package cn.alvinkwok.sslchecker;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import java.io.IOException;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

public class WebSiteCertificates {
    public static void main(String[] args) {
        String url = "https://blog.alvinkwok.cn"; // 替换为你要获取证书信息的域名

        try {
            URL serverUrl = new URL(url);
            HttpsURLConnection connection = (HttpsURLConnection) serverUrl.openConnection();
            connection.connect();
            // 获取服务器证书
            Certificate[] certificates = connection.getServerCertificates();
            for (Certificate certificate : certificates) {
                System.out.println("Certificate: " + certificate);
                // 计算证书的剩余有效期
                if (certificate instanceof X509Certificate) {
                    X509Certificate x509Certificate = (X509Certificate) certificate;
                    System.out.println("Subject DN: " + x509Certificate.getSubjectDN());
                    System.out.println("Issuer DN: " + x509Certificate.getIssuerDN());
                    System.out.println("Not Before: " + x509Certificate.getNotBefore());
                    System.out.println("Not After: " + x509Certificate.getNotAfter());
                }
            }
            connection.disconnect();
        } catch (SSLPeerUnverifiedException e) {
            System.err.println("未验证对等身份证书: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("发生 I/O 错误: " + e.getMessage());
        }
    }
}
