# 模块结构

[English version](en/ARCHITECTURE.md)

Apache LDAP API 是一个按协议层、模型层、客户端层和扩展功能拆分的 Gradle 多模块项目。

## 分层概览

```text
api-all
  -> api-ldap-client-api
      -> api-ldap-net-mina
      -> api-ldap-codec-core
      -> api-ldap-model
  -> api-ldap-extras-*
  -> api-ldap-schema-*
  -> api-asn1-*
  -> api-util
```

## 基础层

### `api-i18n`

集中管理错误消息和国际化文本。

### `api-util`

通用工具类，被多个上层模块依赖。

### `api-asn1-api`

ASN.1 基础接口、异常和工具。

### `api-asn1-ber`

BER 编解码实现，依赖 ASN.1 API。

## LDAP 模型层

### `api-ldap-model`

项目核心模型模块，包含：

- DN/RDN 和名称解析。
- LDAP entry、attribute、value。
- LDAP filter。
- LDAP message 模型。
- Schema 对象、registry、parser。
- LDIF 支持。

这是多数 LDAP 模块的核心依赖。

### `api-ldap-schema-data`

内置 schema 数据和加载资源。构建时会生成 schema 索引。

### `api-ldap-schema-converter`

Schema 转换工具，包含 ANTLR 生成代码。

## 编解码和网络层

### `api-ldap-codec-core`

LDAP 协议编解码核心，连接 LDAP 模型和 ASN.1/BER 编解码。

### `api-ldap-codec-standalone`

standalone codec 适配，用于不依赖完整运行环境的编解码场景。

### `api-ldap-net-mina`

基于 Apache MINA 的网络层适配。

## 客户端层

### `api-ldap-client-api`

LDAP 客户端 API，封装连接、请求、响应和客户端交互。

### `api-ldap-client-all`

客户端聚合模块，用于提供更方便的客户端侧依赖入口。

## 扩展模块

扩展模块位于 `ldap/extras/`：

- `api-ldap-extras-aci`：访问控制项解析和模型。
- `api-ldap-extras-codec-api`：扩展 codec API。
- `api-ldap-extras-codec`：扩展控件和扩展操作编解码。
- `api-ldap-extras-sp`：Stored Procedure 支持。
- `api-ldap-extras-trigger`：Trigger 支持。
- `api-ldap-extras-util`：扩展工具类。

## DSML 模块

### `api-dsml-parser`

DSML 解析和请求/响应模型适配。

### `api-dsml-engine`

DSML engine，构建在 parser 和 LDAP client API 之上。

## 测试模块

### `api-integ`

跨模块集成测试。

### `api-integ-osgi` 和 `api-integ-osgi2`

Pax Exam OSGi 集成测试，验证项目 jar 的 manifest、bundle 安装和激活。

## 分发模块

### `distribution`

生成发布 ZIP/TAR 包，包含 release 文件和运行时依赖。

## OSGi manifest

部分模块目录下保留 `META-INF/MANIFEST.MF`。Gradle `jar` 任务会读取这些 manifest，并将必要属性合并到构建产物中。

迁移或新增模块时要注意：

- `Bundle-SymbolicName` 必须稳定。
- `Import-Package` 不应包含未展开的占位符。
- 非 OSGi 依赖不能直接作为 bundle 安装到 Pax Exam。

## 新增模块流程

1. 创建模块目录和 `build.gradle`。
2. 在 `settings.gradle` 的 `moduleDirs` 中注册模块名和目录。
3. 在模块中设置 `ext.mavenArtifactId` 和 `description`。
4. 依赖版本只引用 `rootProject.ext.libs` 或 `rootProject.ext.versions`。
5. 如模块输出 OSGi bundle，提供或生成正确的 manifest。
6. 更新 `README.md` 和本文档。
