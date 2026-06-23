# Apache LDAP API

Apache LDAP API 是一个面向 Java 的 LDAP 协议与目录数据模型库，提供 ASN.1/BER 编解码、LDAP 模型、LDAP 编解码、客户端 API、Schema 数据、扩展控件、DSML 支持以及 OSGi 集成验证。

本仓库已经迁移为纯 Gradle 多模块项目，不再维护 Maven `pom.xml`。项目版本和依赖版本统一维护在 `gradle.properties`。

## 功能范围

- LDAP 数据模型、DN/RDN、LDIF、Schema、过滤器和消息对象。
- ASN.1 与 BER 编解码基础设施。
- LDAP codec core 与 standalone codec。
- 基于 MINA 的 LDAP 网络层和客户端 API。
- ACI、Trigger、Stored Procedure、扩展控件和扩展操作支持。
- DSML parser 与 engine。
- OSGi bundle manifest 与集成测试。
- 可分发 ZIP/TAR 包。

## 环境要求

- JDK 8 或兼容的 JDK 8 编译环境。
- 使用仓库内置 Gradle Wrapper，当前 Gradle 版本为 7.6。

检查环境：

```powershell
.\gradlew.bat -v
```

Linux/macOS:

```bash
./gradlew -v
```

## 快速开始

完整构建、测试和打包：

```powershell
.\gradlew.bat build
```

仅编译主代码：

```powershell
.\gradlew.bat compileJava
```

仅运行测试：

```powershell
.\gradlew.bat test
```

生成分发包：

```powershell
.\gradlew.bat :distribution:assemble
```

产物位置：

- 模块 Jar：各模块的 `build/libs/`
- 分发包：`distribution/build/distributions/`

## 模块概览

| Gradle 模块 | 目录 | 说明 |
| --- | --- | --- |
| `api-asn1-api` | `asn1/api` | ASN.1 基础 API |
| `api-asn1-ber` | `asn1/ber` | BER 编解码实现 |
| `api-i18n` | `i18n` | 国际化消息资源 |
| `api-util` | `util` | 通用工具类 |
| `api-ldap-model` | `ldap/model` | LDAP 核心模型、Schema、DN、LDIF |
| `api-ldap-codec-core` | `ldap/codec/core` | LDAP 协议编解码核心 |
| `api-ldap-codec-standalone` | `ldap/codec/standalone` | standalone codec 适配 |
| `api-ldap-net-mina` | `ldap/net/mina` | 基于 MINA 的网络层 |
| `api-ldap-client-api` | `ldap/client/api` | LDAP 客户端 API |
| `api-ldap-client-all` | `ldap/client/all` | 客户端聚合模块 |
| `api-ldap-schema-data` | `ldap/schema/data` | 内置 LDAP Schema 数据 |
| `api-ldap-schema-converter` | `ldap/schema/converter` | Schema 转换工具 |
| `api-ldap-extras-*` | `ldap/extras/*` | ACI、codec、trigger、stored procedure 等扩展 |
| `api-dsml-parser` | `dsml/parser` | DSML 解析 |
| `api-dsml-engine` | `dsml/engine` | DSML engine |
| `api-integ` | `integ` | 集成测试 |
| `api-integ-osgi` | `integ-osgi` | OSGi 集成测试 |
| `api-integ-osgi2` | `integ-osgi2` | OSGi 集成测试第二组 |
| `api-all` | `all` | API 聚合模块 |
| `distribution` | `distribution` | ZIP/TAR 分发包 |

更多模块关系见 `docs/ARCHITECTURE.md`。

## 文档

- 构建与测试：`docs/BUILDING.md`
- 模块结构：`docs/ARCHITECTURE.md`
- 发布准备：`docs/RELEASE.md`
- Bug 报告：`docs/bug-reporting.md`
- 贡献指南：`CONTRIBUTING.md`
- 安全披露：`SECURITY.md`
- 获取支持：`SUPPORT.md`
- 行为准则：`CODE_OF_CONDUCT.md`
- 变更记录：`CHANGELOG.md`
- English contribution guide: `docs/en/CONTRIBUTING.md`
- English security policy: `docs/en/SECURITY.md`
- English bug reporting guide: `docs/en/bug-reporting.md`

## 反馈入口

- 可复现的缺陷请使用 GitHub 的 Bug 报告表单，并参考 `docs/bug-reporting.md` 准备最小复现。
- 未修复安全漏洞不要提交公开 Issue，请按 `SECURITY.md` 使用私有安全报告或创建不含漏洞细节的占位 Issue。
- 贡献代码、测试或文档前请阅读 `CONTRIBUTING.md`。

## 版本管理

项目坐标与版本集中在 `gradle.properties`：

- `projectGroup`
- `projectVersion`
- `versions.*`

调整依赖版本时只修改 `gradle.properties`，不要在各模块 `build.gradle` 中写死版本号。

## 许可证

本项目使用 Apache License 2.0，详见 `LICENSE`。

第三方许可和声明见 `NOTICE` 与 `licenses/`。

本项目包含或使用加密相关软件。使用前请阅读 `README.txt` 中的出口合规说明。
