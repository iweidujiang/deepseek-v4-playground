package io.github.iweidujiang.dsv4.analyzer.service;

import io.github.iweidujiang.dsv4.analyzer.entity.SourceFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 源码收集业务类
 * <p>
 * Copyright (c) 2026, 苏渡苇. All rights reserved.
 * <p>
 * 作者：苏渡苇
 * 公众号：苏渡苇
 * <a href="https://github.com/iweidujiang">GitHub</a>
 * <p>
 */
@Slf4j
@Service
public class SourceCodeCollector {
    public List<SourceFile> scanJavaFiles(String projectPath) {
        File rootDir = new File(projectPath);
        if (!rootDir.exists() || !rootDir.isDirectory()) {
            throw new IllegalArgumentException("路径无效: " + projectPath);
        }

        List<SourceFile> result = new ArrayList<>();
        Collection<File> javaFiles = FileUtils.listFiles(rootDir, new String[]{"java"}, true);

        for (File f : javaFiles) {
            String absPath = f.getAbsolutePath();
            if (absPath.contains("/target/") || absPath.contains("/test/") || absPath.contains("/build/")) {
                continue;
            }
            try {
                String relative = rootDir.toURI().relativize(f.toURI()).getPath();
                String content = FileUtils.readFileToString(f, StandardCharsets.UTF_8);
                result.add(new SourceFile(relative, content));
                log.debug("已加载: {}", relative);
            } catch (Exception e) {
                log.warn("读取文件失败: {}", f.getPath(), e);
            }
        }
        log.info("扫描完成，共 {} 个 Java 文件", result.size());
        return result;
    }

    public String buildPrompt(List<SourceFile> files, String mode) {
        StringBuilder sb = new StringBuilder();
        sb.append("下面是项目的全部 Java 源码，请分析。\n\n");

        for (SourceFile file : files) {
            sb.append("### 文件: ").append(file.getRelativePath()).append("\n");
            if ("full".equals(mode)) {
                sb.append("```java\n").append(file.getContent()).append("\n```\n\n");
            } else {
                String summary = extractSummary(file.getContent());
                sb.append("摘要:\n").append(summary).append("\n\n");
            }
        }
        return sb.toString();
    }

    private String extractSummary(String content) {
        StringBuilder sb = new StringBuilder();
        String[] lines = content.split("\n");
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("package ") || trimmed.startsWith("import ") ||
                    trimmed.matches(".*\\b(class|interface|enum)\\b.*") ||
                    trimmed.matches(".*\\bpublic\\b.*\\(.*\\).*")) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }
}
