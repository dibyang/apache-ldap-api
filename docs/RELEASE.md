# 发布准备

[English version](en/RELEASE.md)

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

当前 Gradle 构建使用 `maven-publish` 和 `signing` 发布到 Maven Central/OSSRH。发布凭据和签名信息来自根目录的 `signing.properties`，该文件只应存在于本地环境，不要提交到 Git。

`signing.properties` 需要提供以下键：

```properties
releasesRepository=...
snapshotsRepository=...
ossrhUsername=...
ossrhPassword=...
signing.keyId=...
signing.password=...
signing.secretKeyRingFile=...
```

发布配置也可以通过 Gradle 属性覆盖，例如 `-PossrhUsername=...`。日志和提交中不要输出这些值。

## 发布任务

检查发布配置：

```powershell
.\gradlew.bat validateCentralPublishConfiguration
```

运行完整发布前门禁：

```powershell
.\gradlew.bat releaseGate
```

该任务会运行完整构建、OSGi 测试和 `publishToMavenLocal`，用于检查 jar、sources jar、javadoc jar、POM 元数据和签名产物是否能本地生成。

发布 `SNAPSHOT` 版本到 snapshot 仓库：

```powershell
.\gradlew.bat publishSnapshotToCentral
```

发布正式版本到 release/staging 仓库：

```powershell
.\gradlew.bat publishReleaseToCentralStaging
```

`publishReleaseToCentralStaging` 要求 `projectVersion` 不是 `-SNAPSHOT`，并只负责上传到 `releasesRepository` 对应的 staging 仓库；它不会自动执行中央仓库可见发布。

## 中央仓库审核流程

1. 将 `gradle.properties` 中的 `projectVersion` 改为正式版本号。
2. 更新 `CHANGELOG.md` 和必要的发布说明。
3. 运行 `.\gradlew.bat releaseGate`。
4. 本地提交发布版本改动，创建并推送 tag。
5. 运行 `.\gradlew.bat publishReleaseToCentralStaging` 上传到 staging。
6. 在 Sonatype/Nexus/Central Portal 中人工检查 artifacts、POM、签名、依赖和许可证信息。
7. 确认无误后，再执行中央仓库 release/publish 操作。

如果远程上传只成功了一部分模块，不要继续使用同一版本号补发。应先确认哪些坐标已经进入远程仓库，再决定废弃该版本或升级到新版本重新发布。
