package com.feredback.feredback_backend.util;

import com.feredback.feredback_backend.controller.BaseController;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: FE-Redback
 * @description:
 * @author: Pinzhuo Zhao, StudentID:1043915
 * @create: 2022-04-03 20:19
 **/
@Data
public class JsonResult implements Serializable {

    private Boolean success;

    private Integer code;

    private String message;

    private Map<String, Object> data = new HashMap<String, Object>();

    private JsonResult() {
    }

    public static JsonResult ok() {
        JsonResult r = new JsonResult();
        r.setSuccess(true);
        r.setCode(BaseController.OK);
        r.setMessage("success");
        return r;
    }

    public static JsonResult error() {
        JsonResult r = new JsonResult();
        r.setSuccess(false);
        return r;
    }
    public JsonResult success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public JsonResult message(String message){
        this.setMessage(message);
        return this;
    }

    public JsonResult code(Integer code){
        this.setCode(code);
        return this;
    }

    public JsonResult data(String key, Object value){
        this.data.put(key, value);
        return this;
    }

    public JsonResult data(Map<String, Object> map){
        this.setData(map);
        return this;
    }

}
