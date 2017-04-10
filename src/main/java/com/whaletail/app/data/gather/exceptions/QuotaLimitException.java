package com.whaletail.app.data.gather.exceptions;

/**
 * @author Whaletail
 */
public class QuotaLimitException extends RuntimeException {
    public QuotaLimitException() {
    }

    public QuotaLimitException(String message) {
        super(message);
    }

    public QuotaLimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuotaLimitException(Throwable cause) {
        super(cause);
    }

    public QuotaLimitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
