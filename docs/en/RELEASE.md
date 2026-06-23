# Release Preparation

[中文版本](../RELEASE.md)

This document describes the release preparation workflow for this repository. The repository has migrated to a Gradle-only project and no longer uses Maven POM files as the release entry point.

## Pre-Release Checks

Confirm that the working tree is clean:

```powershell
git status --short
```

Confirm the version:

```powershell
Get-Content gradle.properties
```

Check these items carefully:

- `projectVersion`
- Dependency versions under `versions.*`
- `CHANGELOG.md`
- `README.md`
- `NOTICE`
- `LICENSE`

## Build Verification

Run the full build before release:

```powershell
.\gradlew.bat clean build
```

For distribution-only validation:

```powershell
.\gradlew.bat clean :distribution:assemble
```

Also verify OSGi tests before release:

```powershell
.\gradlew.bat :api-integ-osgi:test :api-integ-osgi2:test
```

## Local Artifacts

Module jars:

```text
<module>/build/libs/
```

Distribution archives:

```text
distribution/build/distributions/
```

## Release Content Checks

Distribution archives should include at least:

- `LICENSE`
- `NOTICE`
- `licenses/`
- Project jars and runtime dependencies under `lib/`

## Version Changes

When preparing a release, update `projectVersion` only in `gradle.properties`. Do not hard-code versions in module scripts.

Update these documents with the version change:

- `CHANGELOG.md`
- Compatibility notes in `README.md` when needed
- Release notes when needed

## Publishing to Remote Repositories

The Gradle build uses `maven-publish` and `signing` for Maven Central/OSSRH publishing. Publishing credentials and signing information come from the root `signing.properties` file. This file is local-only and must not be committed.

`signing.properties` must provide these keys:

```properties
releasesRepository=...
snapshotsRepository=...
ossrhUsername=...
ossrhPassword=...
signing.keyId=...
signing.password=...
signing.secretKeyRingFile=...
```

Publishing configuration can also be overridden through Gradle properties, such as `-PossrhUsername=...`. Do not print these values in logs or commits.

## Publishing Tasks

Validate publishing configuration:

```powershell
.\gradlew.bat validateCentralPublishConfiguration
```

Run the full release gate:

```powershell
.\gradlew.bat releaseGate
```

This task runs the full build, OSGi tests, and `publishToMavenLocal`. It checks that jars, source jars, javadoc jars, POM metadata, and signing artifacts can be generated locally.

Publish a `SNAPSHOT` version to the snapshot repository:

```powershell
.\gradlew.bat publishSnapshotToCentral
```

Upload a release version to the release/staging repository:

```powershell
.\gradlew.bat publishReleaseToCentralStaging
```

`publishReleaseToCentralStaging` requires `projectVersion` to be a non-`-SNAPSHOT` version. It only uploads to the staging repository configured by `releasesRepository`; it does not automatically publish artifacts to a Central-visible release state.

## Central Repository Review Flow

1. Change `projectVersion` in `gradle.properties` to the release version.
2. Update `CHANGELOG.md` and any required release notes.
3. Run `.\gradlew.bat releaseGate`.
4. Commit the release changes locally, then create and push the tag.
5. Run `.\gradlew.bat publishReleaseToCentralStaging` to upload to staging.
6. Review artifacts, POM metadata, signatures, dependencies, and license information in Sonatype/Nexus/Central Portal.
7. Only after review, perform the final Central release/publish action.

If remote upload succeeds for only part of the modules, do not continue publishing the same version. First identify which coordinates reached the remote repository, then decide whether to abandon that version or release again with a new version.
