package io.github.iweidujiang.dsv4.dto;

import lombok.Builder;
import lombok.Data;

/*
 * Copyright (c) 2026, 苏渡苇. All rights reserved.
 *
 * 代码生成响应类
 *
 * 项目名称：deepseek-coding-assistant
 * 创建时间：2026-04-27 23:38
 * 作者：苏渡苇
 * 公众号：苏渡苇
 * GitHub：https://github.com/iweidujiang
 */
@Data
@Builder
public class CodeGenerateResponse {
     private String code;
     private String explanation;
     private String language;
     private long tokensUsed;
}
