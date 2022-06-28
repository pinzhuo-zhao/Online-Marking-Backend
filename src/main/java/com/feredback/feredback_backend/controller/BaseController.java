package com.feredback.feredback_backend.controller;

import com.feredback.feredback_backend.service.ex.*;
import com.feredback.feredback_backend.util.JsonResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpSession;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-03 20:47
 **/

@ControllerAdvice
public class BaseController {

    public static final int OK = 200;
    public static final int ERROR = 400;

    /**Executing this method after exception was thrown
     *
      * @param e
     * @return
     */
    @ExceptionHandler(ServiceException.class)
    public JsonResult handleServiceException(Throwable e) {

        JsonResult result = JsonResult.error();
        if (e instanceof InsertionDuplicatedException) {
            result.code(4000);
            result.message(e.getMessage());
        }
        else if (e instanceof DataModificationException) {
            result.code(4001);
            result.message(e.getMessage());
        }
        else if (e instanceof UserNotFoundException) {
            result.code(4002);
            result.message(e.getMessage());
        }

        return result;
    }

    @ExceptionHandler(CsvException.class)
    public JsonResult handleCsvException(Throwable e) {
        JsonResult result = JsonResult.error();
        if (e instanceof FileTypeException) {
            result.code(4020);
            result.message(e.getMessage());
        }
        else if (e instanceof EmptyFileException) {
            result.code(4031);
            result.message(e.getMessage());
        }
        else if (e instanceof EmptyColumnException) {
            result.code(4032);
            result.message(e.getMessage());
        }

        return result;
    }
}
