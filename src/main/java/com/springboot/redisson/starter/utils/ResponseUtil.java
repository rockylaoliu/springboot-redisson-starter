package com.springboot.redisson.starter.utils;

import lombok.Data;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author: rocky.liu
 * 2021/8/3 10:37 上午
 */
@Data
public class ResponseUtil<T> {

    public static final int CODE_SUCCESS = 0;
    public static final int CODE_FAILED = 1;

    private int code;

    private Map<String, String> msg;

    private T data;

    public static Map<String, String> msg(String key, String value) {
        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    public ResponseUtil(int code, Map<String, String> msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <K> ResponseUtil<K> of(boolean b) {
        return b ? success() : failed();
    }

    public static <K> ResponseUtil<K> success(Object data) {
        return new ResponseUtil(CODE_SUCCESS, null, data);
    }

    public static <K> ResponseUtil<K> success() {
        return new ResponseUtil(CODE_SUCCESS, null, null);
    }

    public static <K> ResponseUtil<K> success(String key, String value) {
        return success(null, msg(key, value));
    }

    public static <K> ResponseUtil<K> success(K data, Map<String, String> msg) {
        return new ResponseUtil(CODE_SUCCESS, msg, data);
    }
    public static <K> ResponseUtil<K> failed(Map<String, String> msg) {
        return new ResponseUtil(CODE_FAILED, msg, null);
    }

    public static <K> ResponseUtil<K> failed() {
        return new ResponseUtil(CODE_FAILED, null, null);
    }

    public static <K> ResponseUtil<K> failed(Map<String, String> msg, Object bean) {
        return new ResponseUtil(CODE_FAILED, msg, bean);
    }

    public static <K> ResponseUtil<K> failed(Map<String, String> msg, String bean) {
        return new ResponseUtil(CODE_FAILED, msg, bean);
    }

    public static <K> ResponseUtil<K> failed(Errors errors, Object bean) {
        return failed(buildMsg(errors), bean);
    }

    public static <K> ResponseUtil<K> failed(String errorMsg) {
        Map<String, String> map = new HashMap<>();
        map.put("error", errorMsg);

        return failed(map, null);
    }

    public static Map<String, String> buildMsg(Errors errors) {
        List<ObjectError> oe = errors.getAllErrors();
        Map<String, String> map = new HashMap<>();
        for (ObjectError e : oe) {
            map.put(((FieldError) e).getField(), e.getDefaultMessage());
        }
        return map;
    }
}

