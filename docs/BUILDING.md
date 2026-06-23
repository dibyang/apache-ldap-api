# 构建与测试

[English version](en/BUILDING.md)

本文档说明如何在本地构建、测试和打包 Apache LDAP API。

## 前置条件

- JDK 8 或兼容 JDK 8 的工具链。
- Git。
- 能访问 Maven Central 或仓库配置的依赖镜像。

无需单独安装 Gradle，仓库提供 Gradle Wrapper。

## 常用命令

查看 Gradle 版本：

```powershell
.\gradlew.bat -v
```

完整构建：

```powershell
.\gradlew.bat build
```

编译所有模块：

```powershell
.\gradlew.bat compileJava
```

运行所有测试：

```powershell
.\gradlew.bat test
```

打包所有模块：

```powershell
.\gradlew.bat assemble
```

生成分发包：

```powershell
.\gradlew.bat :distribution:assemble
```

## 按模块构建

Gradle 模块名使用 Maven artifactId 风格，避免同名子目录产生坐标冲突。例如：

```powershell
.\gradlew.bat :api-ldap-model:test
.\gradlew.bat :api-ldap-client-api:jar
.\gradlew.bat :api-integ-osgi:test
```

列出全部任务：

```powershell
.\gradlew.bat tasks
```

## 测试说明

测试分为普通单元/集成测试和 OSGi 集成测试。

- 普通测试由各 Java 模块的 `test` 任务执行。
- `api-integ` 覆盖跨模块集成场景。
- `api-integ-osgi` 和 `api-integ-osgi2` 使用 Pax Exam 验证 bundle 装配和激活。

改动 OSGi manifest、依赖、模块命名或 `META-INF/MANIFEST.MF` 时，至少运行：

```powershell
.\gradlew.bat :api-integ-osgi:test :api-integ-osgi2:test
```

## 版本属性

所有版本集中在根目录 `gradle.properties`。

- `projectGroup` 控制发布 group。
- `projectVersion` 控制所有模块项目版本。
- `versions.*` 控制外部依赖版本。

模块脚本不应写死依赖版本。

## 生成源码和资源

部分模块会在构建中生成源码或资源：

- ANTLR grammar 生成源码。
- Schema data 模块生成 schema 索引资源。

这些生成任务已经接入 Gradle 构建生命周期，正常执行 `build` 或 `assemble` 即可。

## 常见问题

### 依赖解析失败

先确认网络可以访问 Maven Central 或项目配置的依赖镜像。必要时可清理本地 Gradle 缓存后重试。

### OSGi 测试失败

先单独运行失败模块，并查看 `build/reports/tests/test/index.html`。常见原因包括：

- 依赖不是合法 OSGi bundle。
- manifest 中 `Bundle-SymbolicName` 或 `Import-Package` 不正确。
- 测试运行时没有复制 Pax Exam 需要的依赖。

### 编译警告

仓库中存在一些历史代码和生成代码警告。只要 `build` 成功，警告本身不阻塞构建。修改相关模块时应尽量避免新增警告。
