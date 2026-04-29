package io.github.iweidujiang.dsv4.analyzer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/*
 * Copyright (c) 2026, 苏渡苇. All rights reserved.
 *
 * 项目名称：deepseek-v4-playground
 * 作者：苏渡苇
 * 公众号：苏渡苇
 * GitHub：https://github.com/iweidujiang
 *
 * 源文件实体类
 */
@Data
@AllArgsConstructor
public class SourceFile {
    private String relativePath;
    private String content;
}
