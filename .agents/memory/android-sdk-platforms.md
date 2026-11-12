---
name: Android SDK platform labels
description: Google Android SDK package naming for the API 37 platform used by this project.
---

Google publishes Android API 37 as the SDK package `platforms;android-37.0` (with later minor platform packages such as 37.1 and 37.2). Gradle's `compileSdk = 37` remains correct. Searching only for `platforms/android-37` misses the package.

**Why:** the app's InnerTubeX dependency requires compile SDK 37, and the SDK CLI did not find the platform when queried without the `.0` suffix.

**How to apply:** install `platforms/android-37.0` when this app needs API 37. Do not lower `compileSdk` or fake the platform directory to bypass AAR metadata checks.
