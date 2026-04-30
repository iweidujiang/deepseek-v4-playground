package io.github.iweidujiang.dsv4.sqloptimizer.controller;

import io.github.iweidujiang.dsv4.sqloptimizer.dto.SqlRequest;
import io.github.iweidujiang.dsv4.sqloptimizer.dto.SqlResponse;
import io.github.iweidujiang.dsv4.sqloptimizer.service.SqlGenerateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 前端控制器
 * <p>
 * Copyright (c) 2026, 苏渡苇. All rights reserved.
 * <p>
 * 作者：苏渡苇
 * 公众号：苏渡苇
 * <a href="https://github.com/iweidujiang">GitHub</a>
 * <p>
 */
@RestController
@RequestMapping("/api/sql")
@RequiredArgsConstructor
public class SqlController {
    private final SqlGenerateService sqlGenerateService;

    @PostMapping("/generate")
    public SqlResponse generate(@RequestBody SqlRequest request) {
        String dialect = request.getDialect();
        // 暂时只支持 mysql，其他方言提示
        if (!"mysql".equalsIgnoreCase(dialect)) {
            return SqlResponse.builder()
                    .error("当前版本仅支持 MySQL 数据库，PostgreSQL 和 Oracle 扩展开发中，欢迎 PR。")
                    .build();
        }
        // 校验 model 是否合法
        String model = request.getModel();
        if (!model.equals("deepseek-v4-pro") && !model.equals("deepseek-v4-flash")) {
            model = "deepseek-v4-pro"; // 默认回退
        }
        return sqlGenerateService.process(
                request.getNaturalLanguage(),
                request.getDialect(),
                model,
                request.isEnableOptimize()
        );
    }
}
