# Security Policy

[中文版本](../../SECURITY.md)

## Supported Scope

The `master` branch is the primary maintenance line. Security fixes will be made on `master` first. Backports to older versions depend on impact, risk, and maintenance cost.

## Reporting Vulnerabilities

Do not disclose details of an unfixed security vulnerability in a public issue.

Preferred process:

1. Use GitHub private vulnerability reporting for this repository.
2. If private vulnerability reporting is not enabled, create a public placeholder issue that does not include vulnerability details and ask maintainers for a private communication channel.
3. Include affected versions, trigger conditions, impact, reproduction steps, temporary mitigations, and any credit information you want to make public.

## What Counts as Security Sensitive

Treat the following as security reports and keep details private until coordinated disclosure:

- Data exposure or unintended access to LDAP entries, credentials, configuration, or logs.
- Arbitrary file access or path traversal caused by project code.
- Exploitable deserialization, parser, or codec behavior that can cross a trust boundary.
- Authentication, authorization, or privilege bypass in supported client or integration flows.
- Remotely triggerable denial of service with practical impact.
- Dependency vulnerabilities that are reachable through this project's supported usage.

## Response Expectations

Maintainers will try to acknowledge reports promptly, evaluate impact, and coordinate disclosure after a fix or mitigation exists. Security issues may require several rounds of reproduction and impact analysis before confirmation.

## Out of Scope

The following are usually not considered security vulnerabilities:

- Build failures that only affect a local test environment.
- Public dependency vulnerabilities that are not reachable or exploitable through this project.
- Scenarios that require a fully trusted caller to intentionally execute malicious code.

If you are unsure whether an issue is a vulnerability, treat it as security-sensitive and report it privately first.
