---
name: Java toolchain
description: Environment constraint for building Java services in this workspace.
---

Use the workspace Java Tools module with Maven for Java services; the available runtime may report a newer GraalVM-provided Java version, while Maven projects should keep their requested Java compatibility in the POM.

**Why:** The workspace does not include Java or Maven until the Java toolchain module is installed, and the installed GraalVM runtime can differ from the project's target release.

**How to apply:** Check the Java module before the first Maven build, install it through the package-management flow when missing, and verify the Maven compiler target separately from the runtime version.