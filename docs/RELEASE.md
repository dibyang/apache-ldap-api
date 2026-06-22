# 发布准备

本文档描述本仓库的发布准备流程。当前仓库已经迁移为纯 Gradle 项目，不再使用 Maven POM 作为发布入口。

## 发布前检查

确认工作区干净：

```powershell
git status --short
```

确认版本号：

```powershell
Get-Content gradle.properties
```

需要重点检查：

- `projectVersion`
- 依赖版本 `versions.*`
- `CHANGELOG.md`
- `README.md`
- `NOTICE`
- `LICENSE`

## 构建验证

发布前运行完整构建：

```powershell
.\gradlew.bat clean build
```

如果只做发布包验证：

```powershell
.\gradlew.bat clean :distribution:assemble
```

发布前建议单独确认 OSGi 测试：

```powershell
.\gradlew.bat :api-integ-osgi:test :api-integ-osgi2:test
```

## 本地产物

模块 jar：

```text
<module>/build/libs/
```

分发包：

```text
distribution/build/distributions/
```

## 发布内容检查

检查分发包至少包含：

- `LICENSE`
- `NOTICE`
- `licenses/`
- `lib/` 下的项目 jar 和运行时依赖

## 版本变更

发布版本时只在 `gradle.properties` 中修改 `projectVersion`。不要在模块脚本中写死版本号。

更新版本时同步维护：

- `CHANGELOG.md`
- 需要时更新 `README.md` 中的兼容性说明
- 需要时更新发布说明

## 发布到远程仓库

当前 Gradle 构建包含 `maven-publish` 基础配置，但未在开源文档中定义远程发布凭据和目标仓库。

在正式启用远程发布前，需要先补充：

- 发布仓库地址。
- 凭据注入方式。
- 签名策略。
- staging 或审核流程。
- 回滚和重发策略。

在这些信息明确前，不要把本地构建命令等同于正式发布。
