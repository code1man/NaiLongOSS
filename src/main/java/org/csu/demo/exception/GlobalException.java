package org.csu.demo.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.csu.demo.common.CommonResponse;
import org.csu.demo.common.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalException {

    private static final Logger logger = LoggerFactory.getLogger(GlobalException.class);


    /**
     * 处理请求参数缺失异常（400）
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public CommonResponse<Object> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        logger.error("缺少参数: {}", e.getMessage());
        return CommonResponse.createForError(ResponseCode.BAD_REQUEST.getCode(), "缺少参数: " + e.getParameterName());
    }

    /**
     * 处理参数校验失败异常（400）
     */
/*    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public CommonResponse<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // 获取所有字段错误信息
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        logger.error("参数校验失败: {}", errorMessage);
        return CommonResponse.createForError(ResponseCode.BAD_REQUEST.getCode(), errorMessage);
    }*/

    /**
     * 处理请求参数格式错误异常（400）
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public CommonResponse<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        logger.error("参数类型错误: {}", e.getMessage());
        return CommonResponse.createForError(ResponseCode.BAD_REQUEST.getCode(), "参数类型错误: " + e.getName());
    }

    /**
     * 处理未授权异常（401）
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public CommonResponse<Object> handleAuthenticationException(AuthenticationException e) {
        logger.error("身份验证失败: {}", e.getMessage());
        return CommonResponse.createForError(ResponseCode.UNAUTHORIZED.getCode(), "身份验证失败，请重新登录");
    }

    /**
     * 处理权限不足异常（403）
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public CommonResponse<Object> handleAccessDeniedException(AccessDeniedException e) {
        logger.error("权限不足: {}", e.getMessage());
        return CommonResponse.createForError(ResponseCode.FORBIDDEN.getCode(), "权限不足，无法访问");
    }

    /**
     * 处理找不到资源（404）
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public CommonResponse<Object> handleNoHandlerFoundException(NoHandlerFoundException e) {
        logger.error("请求的资源不存在: {}", e.getRequestURL());
        return CommonResponse.createForError(ResponseCode.NOT_FOUND.getCode(), "嗯？奶龙没有这种东西的哦");
    }

    /**
     * 405 错误的方式访问资源
     * */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public CommonResponse<Object> handleMethodNotAllowed(HttpRequestMethodNotSupportedException e) {
        return CommonResponse.createForError(405, "请求方式错误，请使用正确的 HTTP 方法");
    }

    /**
     * 处理客户端请求超时异常（408）
     */
    @ExceptionHandler(SocketTimeoutException.class)
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    @ResponseBody
    public CommonResponse<Object> handleSocketTimeoutException(SocketTimeoutException e) {
        logger.error("请求超时: {}", e.getMessage());
        return CommonResponse.createForError(408, "世界上最遥远的距离，莫过于你与奶龙之间少了互联网的桥梁");
    }

    /**
     * 处理网络问题导致的服务器无法连接（503）
     */
    @ExceptionHandler(UnknownHostException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ResponseBody
    public CommonResponse<Object> handleUnknownHostException(UnknownHostException e) {
        logger.error("无法连接服务器: {}", e.getMessage());
        return CommonResponse.createForError(503, "世界上最遥远的距离，莫过于你与奶龙之间少了互联网的桥梁");
    }

    /**
     * 处理数据库约束异常（500）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CommonResponse<Object> handleConstraintViolationException(ConstraintViolationException e) {
        logger.error("数据库约束异常: {}", e.getMessage());
        return CommonResponse.createForError(ResponseCode.INTERNAL_SERVER_ERROR.getCode(), "操作失败，请检查数据约束");
    }

    /**
     * 处理 HTTP 客户端错误（400-499）
     */
    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public CommonResponse<Object> handleHttpClientErrorException(HttpClientErrorException e) {
        logger.error("HTTP 客户端错误: {}", e.getMessage());
        return CommonResponse.createForError(e.getStatusCode().value(), "客户端请求错误");
    }

    @ExceptionHandler(LoginException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public CommonResponse<Object> handleLoginException(LoginException e) {
        logger.error("登录异常: {} - {}", e.getErrorCode(), e.getErrorMessage());
        return CommonResponse.createForError(400, e.getErrorMessage());
    }

     /**
     * 处理通用异常（500）
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public CommonResponse<Object> handleException(Exception e) {
        logger.error("系统异常: {}", e.getMessage());
        return CommonResponse.createForError(408, "奶龙家里着火啦，请不要揭穿他，偷偷告诉小七就好啦");
    }
}