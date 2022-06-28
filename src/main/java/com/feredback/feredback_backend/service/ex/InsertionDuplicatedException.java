package com.feredback.feredback_backend.service.ex;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-04 00:41
 **/
public class InsertionDuplicatedException extends ServiceException{

    public InsertionDuplicatedException() {
        super();
    }

    public InsertionDuplicatedException(String message) {
        super(message);
    }

    public InsertionDuplicatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsertionDuplicatedException(Throwable cause) {
        super(cause);
    }

    protected InsertionDuplicatedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
