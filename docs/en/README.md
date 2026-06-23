# Apache LDAP API

[中文版本](../../README.md)

Apache LDAP API is a Java library for LDAP protocol handling and directory data models. It provides ASN.1/BER encoding and decoding, LDAP models, LDAP codecs, client APIs, schema data, extended controls, DSML support, and OSGi integration verification.

This repository has migrated to a Gradle-only multi-module project. Maven `pom.xml` files are no longer maintained. Project and dependency versions are managed in `gradle.properties`.

## Scope

- LDAP data model, DN/RDN, LDIF, schema, filters, and message objects.
- ASN.1 and BER encoding and decoding infrastructure.
- LDAP codec core and standalone codec.
- MINA-based LDAP network layer and client API.
- ACI, Trigger, Stored Procedure, extended controls, and extended operations.
- DSML parser and engine.
- OSGi bundle manifests and integration tests.
- Distributable ZIP and TAR archives.

## Requirements

- JDK 8 or a compilation environment compatible with JDK 8.
- The Gradle Wrapper included in this repository. The current Gradle version is 7.6.

Check the environment:

```powershell
.\gradlew.bat -v
```

Linux/macOS:

```bash
./gradlew -v
```

## Quick Start

Full build, test, and packaging:

```powershell
.\gradlew.bat build
```

Compile main code only:

```powershell
.\gradlew.bat compileJava
```

Run tests only:

```powershell
.\gradlew.bat test
```

Build distribution archives:

```powershell
.\gradlew.bat :distribution:assemble
```

Artifacts:

- Module jars: each module's `build/libs/`
- Distribution archives: `distribution/build/distributions/`

## Module Overview

| Gradle module | Directory | Description |
| --- | --- | --- |
| `api-asn1-api` | `asn1/api` | ASN.1 base API |
| `api-asn1-ber` | `asn1/ber` | BER codec implementation |
| `api-i18n` | `i18n` | Internationalized message resources |
| `api-util` | `util` | Common utilities |
| `api-ldap-model` | `ldap/model` | LDAP core model, schema, DN, and LDIF |
| `api-ldap-codec-core` | `ldap/codec/core` | LDAP protocol codec core |
| `api-ldap-codec-standalone` | `ldap/codec/standalone` | Standalone codec adapter |
| `api-ldap-net-mina` | `ldap/net/mina` | MINA-based network layer |
| `api-ldap-client-api` | `ldap/client/api` | LDAP client API |
| `api-ldap-client-all` | `ldap/client/all` | Client aggregation module |
| `api-ldap-schema-data` | `ldap/schema/data` | Built-in LDAP schema data |
| `api-ldap-schema-converter` | `ldap/schema/converter` | Schema conversion tools |
| `api-ldap-extras-*` | `ldap/extras/*` | Extensions such as ACI, codec, trigger, and stored procedure support |
| `api-dsml-parser` | `dsml/parser` | DSML parsing |
| `api-dsml-engine` | `dsml/engine` | DSML engine |
| `api-integ` | `integ` | Integration tests |
| `api-integ-osgi` | `integ-osgi` | OSGi integration tests |
| `api-integ-osgi2` | `integ-osgi2` | Second OSGi integration test group |
| `api-all` | `all` | API aggregation module |
| `distribution` | `distribution` | ZIP/TAR distribution archives |

See `ARCHITECTURE.md` for more module relationships.

## Documentation

- Build and test: `BUILDING.md`
- Module structure: `ARCHITECTURE.md`
- Release preparation: `RELEASE.md`
- Bug reporting: `bug-reporting.md`
- Contributing: `CONTRIBUTING.md`
- Security policy: `SECURITY.md`
- Support: `SUPPORT.md`
- Code of conduct: `CODE_OF_CONDUCT.md`
- Changelog: `CHANGELOG.md`

Chinese is the primary documentation language. English translations live under `docs/en/` and link back to the corresponding Chinese primary documents.

## Feedback

- For reproducible defects, use the GitHub bug report form and follow `bug-reporting.md` when preparing a minimal reproducer.
- Do not file public issues for unfixed security vulnerabilities. Follow `SECURITY.md` and use private vulnerability reporting, or create a placeholder issue without vulnerability details.
- Read `CONTRIBUTING.md` before contributing code, tests, or documentation.

## Version Management

Project coordinates and versions are centralized in `gradle.properties`:

- `projectGroup`
- `projectVersion`
- `versions.*`

When adjusting dependency versions, only update `gradle.properties`; do not hard-code versions in module `build.gradle` files.

## License

This project uses the Apache License 2.0. See `../../LICENSE`.

Third-party license notices are in `../../NOTICE` and `../../licenses/`.

This product includes or uses cryptographic software. Read the export compliance notice in `../../README.txt` before use.
