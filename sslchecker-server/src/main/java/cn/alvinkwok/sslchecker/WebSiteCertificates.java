package cn.alvinkwok.sslchecker;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.security.auth.x500.X500Principal;
import java.io.IOException;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

public class WebSiteCertificates {
    public static void main(String[] args) {
        String url = "https://www.cloudflare.com"; // 替换为你要获取证书信息的域名

        try {
            URL serverUrl = new URL(url);
            HttpsURLConnection connection = (HttpsURLConnection) serverUrl.openConnection();
            connection.connect();
            // 获取服务器证书
            Certificate[] certificates = connection.getServerCertificates();
            for (Certificate certificate : certificates) {
                String type = certificate.getType();
                System.out.println(type);

//                System.out.println("Certificate: " + certificate);
                // 计算证书的剩余有效期
                if (certificate instanceof X509Certificate) {
                    X509Certificate x509Certificate = (X509Certificate) certificate;
                    System.out.println("Version: " + x509Certificate.getVersion());
                    System.out.println("Serial Number: " + x509Certificate.getSerialNumber());
                    X500Principal subjectPrincipal = x509Certificate.getSubjectX500Principal();
                    System.out.println("Subject DN: " + subjectPrincipal.getName());
                    X500Principal issuerPrincipal = x509Certificate.getIssuerX500Principal();
                    System.out.println("Issuer DN: " + issuerPrincipal.getName());
                    System.out.println("Not Before: " + x509Certificate.getNotBefore());
                    System.out.println("Not After: " + x509Certificate.getNotAfter());
                    System.out.println("Algorithm: " + x509Certificate.getSigAlgName());

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
