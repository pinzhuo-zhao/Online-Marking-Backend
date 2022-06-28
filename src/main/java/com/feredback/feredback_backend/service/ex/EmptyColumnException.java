package com.feredback.feredback_backend.service.ex;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-06 03:04
 **/
public class EmptyColumnException extends CsvException {
    public EmptyColumnException() {
        super();
    }

    public EmptyColumnException(String message) {
        super(message);
    }

    public EmptyColumnException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyColumnException(Throwable cause) {
        super(cause);
    }

    protected EmptyColumnException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
