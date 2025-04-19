package org.csu.demo.common;

import lombok.Getter;

@Getter
public enum ResponseCode {
    SUCCESS(0, "Success"),
    ERROR(1, "Error"),
    NEED_LOGIN(10, "NEED_Login"),
    ILLEGAL_ARGUMENT(2, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int code;
    private final String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    // 通过 code 获取 ResponseCode
    public static ResponseCode fromCode(int code) {
        for (ResponseCode responseCode : values()) {
            if (responseCode.code == code) {
                return responseCode;
            }
        }
        return ERROR;
    }
}


