package com.jsl.oa.utils;

public class ResultUtil {

    public static BaseResponse success() {
        return new BaseResponse("Success", 200, "操作成功", null);
    }

    public static BaseResponse success(Object data) {
        return new BaseResponse("Success", 200, "操作成功", data);
    }

    public static BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse(errorCode.getOutput(), errorCode.getCode(), errorCode.getMessage());
    }

    public static BaseResponse error(ErrorCode errorCode, Object data) {
        return new BaseResponse(errorCode.getOutput(), errorCode.getCode(), errorCode.getMessage(), data);
    }
}
