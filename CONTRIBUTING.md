# 贡献指南

感谢你愿意改进 Apache LDAP API。这个仓库是一个纯 Gradle 多模块 Java 项目，贡献时请保持构建、测试和文档同步。

## 开发环境

- 使用 JDK 8 或兼容 JDK 8 的编译环境。
- 使用仓库内置 Gradle Wrapper，不要求本机预装 Gradle。
- Windows 使用 `.\gradlew.bat`，Linux/macOS 使用 `./gradlew`。

## 分支和提交

- 从最新 `master` 创建工作分支。
- 每个提交聚焦一个主题，避免混入格式化、IDE 配置或无关重构。
- 提交信息使用简短祈使句，例如 `Fix LDAP filter parsing`。
- 不提交 `build/`、`.gradle/`、IDE 本地配置或临时文件。

## 构建和测试

提交前至少运行：

```powershell
.\gradlew.bat build
```

如果只改了某个模块，可以先运行更窄范围的任务：

```powershell
.\gradlew.bat :api-ldap-model:test
```

涉及 OSGi manifest、依赖、模块边界或 bundle 行为时，还要运行：

```powershell
.\gradlew.bat :api-integ-osgi:test :api-integ-osgi2:test
```

## 代码规范

- 源码兼容 Java 8。
- Java 编译编码为 `ISO-8859-1`，Javadoc 使用 UTF-8。
- 优先沿用现有包结构、命名和异常处理方式。
- 不在公共 API 上做不兼容变更，除非同步说明迁移方式。
- 修复 bug 时尽量补充能复现问题的测试。

## Gradle 规范

- 项目版本和依赖版本只维护在 `gradle.properties`。
- 模块 `build.gradle` 应引用 `rootProject.ext.libs` 或 `rootProject.ext.versions`。
- 不新增 Maven POM；本仓库不再维护 Maven reactor。
- 新增模块时同步更新 `settings.gradle`、根 `README.md` 和 `docs/ARCHITECTURE.md`。

## 文档规范

- 用户可见行为、构建命令、模块边界或发布流程改变时，同步更新文档。
- 中文是当前主文档语言。
- 如果需要英文文档，请放在 `docs/en/`，不要把中英文说明混在同一正文段落中。

## Pull Request 检查清单

- 变更范围聚焦，没有包含无关文件。
- 已运行相关 Gradle 任务，并在 PR 中说明结果。
- 新增或变更行为有测试覆盖，或说明无法补测的原因。
- 文档、版本属性和发布说明已按需更新。
- 没有提交本地密钥、凭据、私有仓库地址或临时构建产物。
