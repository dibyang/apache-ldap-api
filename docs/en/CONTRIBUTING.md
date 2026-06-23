# Contributing Guide

[中文版本](../../CONTRIBUTING.md)

Thank you for improving Apache LDAP API. This repository is a Gradle-only multi-module Java project. Please keep builds, tests, and documentation aligned with your changes.

## Feedback Channels

- Reproducible bugs: use the GitHub bug report form and follow `bug-reporting.md`.
- Security vulnerabilities: do not open a public issue with exploit details. Follow `SECURITY.md`.
- Documentation fixes and small code changes: open a focused pull request.
- Design or compatibility changes: open an issue first so maintainers can discuss the direction.

## Development Environment

- Use JDK 8 or a compilation environment compatible with JDK 8.
- Use the Gradle Wrapper included in the repository; a locally installed Gradle is not required.
- Use `.\gradlew.bat` on Windows and `./gradlew` on Linux or macOS.

## Branches and Commits

- Create a working branch from the latest `master`.
- Keep each commit focused on one topic. Avoid mixing formatting, IDE configuration, or unrelated refactoring.
- Use a short imperative commit message, for example `Fix LDAP filter parsing`.
- Do not commit `build/`, `.gradle/`, local IDE configuration, or temporary files.

## Build and Test

Before submitting, run at least:

```powershell
.\gradlew.bat build
```

For a module-only change, you may start with a narrower task:

```powershell
.\gradlew.bat :api-ldap-model:test
```

If the change touches OSGi manifests, dependencies, module boundaries, or bundle behavior, also run:

```powershell
.\gradlew.bat :api-integ-osgi:test :api-integ-osgi2:test
```

Documentation-only changes do not require the full Gradle build, but you should inspect the Markdown diff and run `git diff --check`.

## Code Guidelines

- Keep source code compatible with Java 8.
- Java source compilation uses `ISO-8859-1`; Javadocs use UTF-8.
- Prefer existing package structure, naming, and exception handling patterns.
- Avoid incompatible public API changes unless migration guidance is documented.
- Bug fixes should include a regression test whenever practical.

## Gradle Guidelines

- Project and dependency versions are maintained only in `gradle.properties`.
- Module `build.gradle` files should reference `rootProject.ext.libs` or `rootProject.ext.versions`.
- Do not add Maven POM files; this repository no longer maintains a Maven reactor.
- When adding a module, update `settings.gradle`, the root `README.md`, and `docs/ARCHITECTURE.md`.

## Documentation Guidelines

- Update documentation when user-visible behavior, build commands, module boundaries, or release workflow change.
- Chinese is the primary documentation language.
- English documentation belongs under `docs/en/`; avoid mixing Chinese and English in the same body section.

## Pull Request Checklist

- The change is focused and contains no unrelated files.
- Relevant Gradle tasks were run and the results are included in the pull request.
- New or changed behavior is covered by tests, or the pull request explains why tests were not added.
- Documentation, version properties, and release notes were updated when needed.
- No local keys, credentials, private repository URLs, or temporary build outputs are included.
