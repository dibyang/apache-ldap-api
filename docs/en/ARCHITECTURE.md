# Module Structure

[中文版本](../ARCHITECTURE.md)

Apache LDAP API is a Gradle multi-module project organized by protocol layers, model layers, client functionality, and extensions.

## Layer Overview

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

## Base Layer

### `api-i18n`

Centralized error messages and internationalized text.

### `api-util`

Common utilities used by multiple upper-layer modules.

### `api-asn1-api`

ASN.1 base interfaces, exceptions, and utilities.

### `api-asn1-ber`

BER codec implementation. Depends on the ASN.1 API.

## LDAP Model Layer

### `api-ldap-model`

The core model module, including:

- DN/RDN and name parsing.
- LDAP entries, attributes, and values.
- LDAP filters.
- LDAP message model.
- Schema objects, registries, and parsers.
- LDIF support.

This is the core dependency for most LDAP modules.

### `api-ldap-schema-data`

Built-in schema data and loading resources. The build generates schema index resources.

### `api-ldap-schema-converter`

Schema conversion tools, including ANTLR-generated code.

## Codec and Network Layer

### `api-ldap-codec-core`

LDAP protocol codec core. It connects the LDAP model with ASN.1/BER encoding and decoding.

### `api-ldap-codec-standalone`

Standalone codec adapter for codec scenarios that do not need the full runtime environment.

### `api-ldap-net-mina`

Network layer adapter based on Apache MINA.

## Client Layer

### `api-ldap-client-api`

LDAP client API for connections, requests, responses, and client interactions.

### `api-ldap-client-all`

Client aggregation module that provides a convenient dependency entry point for client-side use.

## Extension Modules

Extension modules are under `ldap/extras/`:

- `api-ldap-extras-aci`: access control item parsing and model.
- `api-ldap-extras-codec-api`: extension codec API.
- `api-ldap-extras-codec`: extended control and extended operation codecs.
- `api-ldap-extras-sp`: Stored Procedure support.
- `api-ldap-extras-trigger`: Trigger support.
- `api-ldap-extras-util`: extension utilities.

## DSML Modules

### `api-dsml-parser`

DSML parsing and request/response model adaptation.

### `api-dsml-engine`

DSML engine built on the parser and LDAP client API.

## Test Modules

### `api-integ`

Cross-module integration tests.

### `api-integ-osgi` and `api-integ-osgi2`

Pax Exam OSGi integration tests that verify project jar manifests, bundle installation, and activation.

## Distribution Module

### `distribution`

Generates release ZIP/TAR archives containing release files and runtime dependencies.

## OSGi Manifest

Some module directories keep `META-INF/MANIFEST.MF`. The Gradle `jar` task reads these manifests and merges required attributes into build artifacts.

When migrating or adding modules:

- `Bundle-SymbolicName` must remain stable.
- `Import-Package` must not contain unresolved placeholders.
- Non-OSGi dependencies must not be installed directly as bundles in Pax Exam.

## Adding a Module

1. Create the module directory and `build.gradle`.
2. Register the module name and directory in `moduleDirs` in `settings.gradle`.
3. Set `ext.mavenArtifactId` and `description` in the module.
4. Reference dependency versions only through `rootProject.ext.libs` or `rootProject.ext.versions`.
5. If the module produces an OSGi bundle, provide or generate the correct manifest.
6. Update `README.md` and this document.
