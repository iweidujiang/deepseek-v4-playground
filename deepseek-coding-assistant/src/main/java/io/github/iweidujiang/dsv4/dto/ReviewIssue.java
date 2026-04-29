package io.github.iweidujiang.dsv4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Copyright (c) 2026, 苏渡苇. All rights reserved.
 *
 * 项目名称：deepseek-coding-assistant
 * 创建时间：2026-04-27 23:40
 * 作者：苏渡苇
 * 公众号：苏渡苇
 * GitHub：https://github.com/iweidujiang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewIssue {
    private String type;
    private int line;
    private String description;
    private String suggestion;
}
