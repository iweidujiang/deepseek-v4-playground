package io.github.iweidujiang.dsv4.analyzer.controller;

import io.github.iweidujiang.dsv4.analyzer.dto.AnalysisReport;
import io.github.iweidujiang.dsv4.analyzer.dto.AnalysisRequest;
import io.github.iweidujiang.dsv4.analyzer.service.ProjectAnalyzerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API Controller
 * <p>
 * Copyright (c) 2026, 苏渡苇. All rights reserved.
 * <p>
 * 作者：苏渡苇
 * 公众号：苏渡苇
 * <a href="https://github.com/iweidujiang">GitHub</a>
 * <p>
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiController {
    private final ProjectAnalyzerService analyzerService;

    @PostMapping("/analyze")
    public AnalysisReport analyze(@RequestBody AnalysisRequest request) throws Exception {
        log.info("分析请求: path={}, mode={}", request.getProjectPath(), request.getMode());
        return analyzerService.analyze(request.getProjectPath(), request.getMode());
    }
}
