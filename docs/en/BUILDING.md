# Build and Test

[中文版本](../BUILDING.md)

This document explains how to build, test, and package Apache LDAP API locally.

## Prerequisites

- JDK 8 or a toolchain compatible with JDK 8.
- Git.
- Access to Maven Central or the dependency mirror configured for the repository.

You do not need to install Gradle separately. The repository provides the Gradle Wrapper.

## Common Commands

Show Gradle version:

```powershell
.\gradlew.bat -v
```

Full build:

```powershell
.\gradlew.bat build
```

Compile all modules:

```powershell
.\gradlew.bat compileJava
```

Run all tests:

```powershell
.\gradlew.bat test
```

Package all modules:

```powershell
.\gradlew.bat assemble
```

Generate distribution archives:

```powershell
.\gradlew.bat :distribution:assemble
```

## Build by Module

Gradle module names use Maven artifactId-style names to avoid coordinate conflicts from repeated child directory names. For example:

```powershell
.\gradlew.bat :api-ldap-model:test
.\gradlew.bat :api-ldap-client-api:jar
.\gradlew.bat :api-integ-osgi:test
```

List all tasks:

```powershell
.\gradlew.bat tasks
```

## Tests

Tests are split between regular unit/integration tests and OSGi integration tests.

- Regular tests run through each Java module's `test` task.
- `api-integ` covers cross-module integration scenarios.
- `api-integ-osgi` and `api-integ-osgi2` use Pax Exam to verify bundle assembly and activation.

When changing OSGi manifests, dependencies, module names, or `META-INF/MANIFEST.MF`, run at least:

```powershell
.\gradlew.bat :api-integ-osgi:test :api-integ-osgi2:test
```

## Version Properties

All versions are centralized in the root `gradle.properties`.

- `projectGroup` controls the publishing group.
- `projectVersion` controls all module project versions.
- `versions.*` controls external dependency versions.

Module scripts should not hard-code dependency versions.

## Generated Sources and Resources

Some modules generate sources or resources during the build:

- ANTLR grammar generates source code.
- The schema data module generates schema index resources.

These generation tasks are already connected to the Gradle build lifecycle. Running `build` or `assemble` is enough.

## Troubleshooting

### Dependency Resolution Failure

First confirm that the network can access Maven Central or the configured dependency mirror. Clear the local Gradle cache and retry if needed.

### OSGi Test Failure

Run the failing module separately and inspect `build/reports/tests/test/index.html`. Common causes include:

- A dependency is not a valid OSGi bundle.
- `Bundle-SymbolicName` or `Import-Package` is incorrect in the manifest.
- Runtime dependencies required by Pax Exam were not copied.

### Compilation Warnings

This repository contains warnings from historical code and generated code. Warnings do not block the build if `build` succeeds. When modifying related modules, avoid adding new warnings.
