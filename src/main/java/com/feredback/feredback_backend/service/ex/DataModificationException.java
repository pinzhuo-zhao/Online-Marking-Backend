package com.feredback.feredback_backend.service.ex;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-04 00:38
 **/
public class DataModificationException extends ServiceException{

    public DataModificationException() {
        super();
    }

    public DataModificationException(String message) {
        super(message);
    }

    public DataModificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataModificationException(Throwable cause) {
        super(cause);
    }

    protected DataModificationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
