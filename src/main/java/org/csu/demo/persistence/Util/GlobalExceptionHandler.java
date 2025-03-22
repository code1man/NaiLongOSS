package org.csu.demo.persistence.Util;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.csu.demo.common.CommonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(code= HttpStatus.BAD_REQUEST)    //400
//   若不设置，在异常被正常处理的情况下，状态码为200
    @ResponseBody
//   封装为Json对象返回
    public CommonResponse<Object> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        logger.error(e.getMessage());
        return CommonResponse.createForError("缺少参数");
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code= HttpStatus.BAD_REQUEST)     //400
    @ResponseBody
    public CommonResponse<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error(e.getMessage());
//        return CommonResponse.createForError("对象校验失败");
        return CommonResponse.createForError(e.getMessage());
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(code= HttpStatus.INTERNAL_SERVER_ERROR)      //500
    @ResponseBody
    public CommonResponse<Object> handleConstraintViolationException(ConstraintViolationException e) {
        logger.error(e.getMessage());
//        return CommonResponse.createForError("参数校验失败");
        return CommonResponse.createForError(e.getMessage());
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(code= HttpStatus.INTERNAL_SERVER_ERROR)      //500
    @ResponseBody
    public CommonResponse<Object> handleException(Exception e) {
        logger.error(e.getMessage());
        return CommonResponse.createForError("系统异常");
    }
}
