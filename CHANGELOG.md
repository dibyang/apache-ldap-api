# 变更记录

本文件记录项目的重要变更。版本号来自 `gradle.properties` 中的 `projectVersion`。

## 未发布

### 变更

- 项目迁移为 Gradle 7.6 多模块构建。
- 项目版本和依赖版本集中维护在 `gradle.properties`。
- 删除 Maven POM，仓库后续作为纯 Gradle 项目维护。
- 补齐开源项目入口文档、贡献说明、安全策略、行为准则、构建说明、架构说明和发布准备说明。
- 补齐开源反馈入口：新增 GitHub Bug 报告表单、Issue contact links、Bug 报告指南、安全报告说明和英文贡献/安全/Bug 报告文档。

### 修复

- 修复 `Strings.toLowerCaseAscii` 处理中文 DN 等非 ASCII 字符时可能抛出数组越界异常的问题。
- 修复 Gradle 构建下 OSGi 测试依赖装配、manifest 合并和 bundle 名称适配问题。
- 稳定 `SubtreeSpecificationParserTest`，避免测试类级别并发 runner 与共享 parser 状态互相影响。

## 1.0.0-M20V20260126

### 说明

- 当前代码基线版本。
- 该版本号保留在 `gradle.properties`，用于 Gradle 构建产物和发布坐标。
