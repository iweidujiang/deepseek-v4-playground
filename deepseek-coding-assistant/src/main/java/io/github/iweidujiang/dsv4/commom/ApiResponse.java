package io.github.iweidujiang.dsv4.commom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Copyright (c) 2026, 苏渡苇. All rights reserved.
 *
 * 统一响应格式
 *
 * 项目名称：deepseek-coding-assistant
 * 创建时间：2026-04-27 22:38
 * 作者：苏渡苇
 * 公众号：苏渡苇
 * GitHub：https://github.com/iweidujiang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;
    private long timestamp;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "success", data, System.currentTimeMillis());
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null, System.currentTimeMillis());
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(500, message, null, System.currentTimeMillis());
    }
}
