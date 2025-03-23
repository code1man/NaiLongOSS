package org.csu.demo.common;

import lombok.Getter;

@Getter
public enum ResponseCode {

    SUCCESS(0,"success"),
    ERROR(1,"error");

    private final int code;
    private final String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}

