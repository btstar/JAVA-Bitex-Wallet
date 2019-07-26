package com.udun_demo.support.config;

import com.udun_demo.support.common.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String RESULT_CODE="code";
    private static final String RESULT_MSG="message";


    /**
     * 全局异常捕捉处理
     * @param ex 异常
     * @return  通用返回
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Map errorHandler(Exception ex) {
        log.error(ex.getMessage(),ex);
        Map<String,Object> map = new HashMap<>();
        map.put(RESULT_CODE, 500);
        map.put(RESULT_MSG, ex.getMessage());
        return map;
    }


    /**
     * 拦截捕捉自定义异常 MyException.class
     * @param ex 异常
     * @return 通用返回
     */
    @ResponseBody
    @ExceptionHandler(value = CommonException.class)
    public Map udunErrorHandler(CommonException ex) {
        log.error(ex.getMessage(),ex);
        Map<String,Object> map = new HashMap<>();
        map.put(RESULT_CODE, ex.getCode());
        map.put(RESULT_MSG, ex.getMessage());
        return map;
    }
}
