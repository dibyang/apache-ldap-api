# Changelog

[中文版本](../../CHANGELOG.md)

This file records important project changes. Version numbers come from `projectVersion` in `gradle.properties`.

## Unreleased

### Changed

- Migrated the project to a Gradle 7.6 multi-module build.
- Centralized project and dependency versions in `gradle.properties`.
- Removed Maven POM files; the repository is now maintained as a Gradle-only project.
- Added Maven Central/OSSRH publishing tasks, including local signing, a `publishToMavenLocal` gate, and staging upload support.
- Added open source entry-point documentation, contributing guidance, security policy, code of conduct, build guide, architecture guide, and release preparation guide.
- Added open source feedback entry points: GitHub bug report form, issue contact links, bug reporting guide, security reporting guidance, and English contributing/security/bug reporting documents.
- Standardized documentation language policy: Chinese is the primary documentation language, English translations live under `docs/en/`, and language switch links are available from both sides.

### Fixed

- Fixed a possible array index error in `Strings.toLowerCaseAscii` when handling Chinese DN values and other non-ASCII characters.
- Fixed Gradle-based OSGi test dependency assembly, manifest merging, and bundle name adaptation.
- Stabilized `SubtreeSpecificationParserTest` by avoiding interference between class-level concurrent runners and shared parser state.

## 1.0.0-M20V20260126

### Notes

- Current code baseline version.
- This version remains in `gradle.properties` for Gradle build artifacts and publishing coordinates.
