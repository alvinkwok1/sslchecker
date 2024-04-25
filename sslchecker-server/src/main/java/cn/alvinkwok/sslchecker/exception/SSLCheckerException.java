package cn.alvinkwok.sslchecker.exception;

/**
 * Description
 * 证书检查异常
 * @author alvinkwok
 * @since 2024/4/25
 */
public class SSLCheckerException extends RuntimeException {
    private static final long serialVersionUID = -1067826566770434435L;

    public SSLCheckerException() {
    }

    public SSLCheckerException(String message) {
        super(message);
    }

    public SSLCheckerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SSLCheckerException(Throwable cause) {
        super(cause);
    }

    public SSLCheckerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
