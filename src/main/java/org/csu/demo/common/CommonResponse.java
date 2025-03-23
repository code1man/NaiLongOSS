package org.csu.demo.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
// springboot中内置fastjson，会根据Get方法自动封装为json格式返回给前端
@JsonInclude(JsonInclude.Include.NON_NULL)
//忽略序列化过程中遇到的null值
public class CommonResponse<T> {
    private int status;
    private String message;
    private T data;

    private CommonResponse(int status,String message) {
        this.status = status;
        this.message = message;
    }

    private CommonResponse(int status,String message,T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T>CommonResponse<T> createForSuccess()
    {
       return new CommonResponse<T>(ResponseCode.SUCCESS.getCode(),ResponseCode.SUCCESS.getDesc());
    }

    public static <T>CommonResponse<T> createForSuccessMessage(String message)
    {
        return new CommonResponse<T>(ResponseCode.SUCCESS.getCode(),message);
    }

    public static <T>CommonResponse<T> createForSuccess(T data)
    {
        return new CommonResponse<T>(ResponseCode.SUCCESS.getCode(),ResponseCode.SUCCESS.getDesc(),data);
    }

    public static <T>CommonResponse<T> createForSuccess(String message, T data)
    {
        return new CommonResponse<T>(ResponseCode.SUCCESS.getCode(), message, data);
    }

    public static <T>CommonResponse<T> createForError()
    {
        return new CommonResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }


    public static <T>CommonResponse<T> createForError(String message)
    {
        return new CommonResponse<T>(ResponseCode.ERROR.getCode(),message);
    }

    public static <T>CommonResponse<T> createForError(int code,String message)
    {
        return new CommonResponse<T>(code,message);
    }

    @JsonIgnore
//    序列化是会自动添加public方法中is开头的属性
    public boolean isSuccess() {
        return this.status == ResponseCode.SUCCESS.getCode();
    }


}
