# Bug Reporting Guide

[中文版本](../bug-reporting.md)

Thank you for helping improve Apache LDAP API. A useful bug report should help maintainers understand the impact quickly and reproduce the issue locally.

## Before Reporting

- Search existing issues to avoid duplicate reports.
- Try the current maintained version or the latest `master` branch when practical.
- Prepare a minimal reproducer, test, LDIF, schema file, LDAP request, or Gradle command.
- Remove credentials, internal addresses, production data, and personal information from logs.

Do not disclose details of an unfixed security vulnerability in a public issue. Read `../../SECURITY.md` and prefer GitHub private vulnerability reporting.

## What to Include

- Project version, dependency coordinates, or commit hash.
- Affected module, such as LDAP model, codec, client API, schema, DSML, OSGi, or Gradle build.
- Usage path, such as direct library usage, LDAP client connection, codec encode/decode, schema or LDIF processing, or framework integration.
- Runtime environment, including JDK, OS, Gradle, LDAP server, and OSGi container when relevant.
- Expected behavior and actual behavior.
- Minimal reproduction steps, ideally as a test that can be added to this repository.
- Relevant stack traces, build logs, or protocol data snippets.
- Workarounds or investigation results you have already tried.

## Minimal Reproducer Tips

- Keep only the dependencies, input data, and code path needed to trigger the issue.
- For parsing, schema, LDIF, DN/RDN, and filter issues, include the complete input text.
- For client or network issues, describe the LDAP server type, connection parameter category, authentication method, and triggering operation, but do not publish passwords or tokens.
- For concurrency or timing issues, include thread counts, loop counts, timeout settings, and failure rate.
- For build issues, include the exact Gradle command and the smallest relevant log excerpt.

## Good Public Issues

- Reproducible functional defects, parsing errors, compatibility problems, or build failures.
- Missing documentation, incorrect examples, or unclear instructions.
- Well-scoped feature requests or improvement proposals.

## Not for Public Issues

- Details of data exposure, arbitrary file access, exploitable deserialization, privilege bypass, remotely triggerable denial of service, or similar security issues.
- Private credentials, internal addresses, production directory data, or user privacy.
- General LDAP operations questions unrelated to this project.

## Working on a Fix

If you want to submit a fix, read `../../CONTRIBUTING.md` first. Bug fixes should include a regression test whenever practical, and pull requests should state which Gradle tasks were run.
