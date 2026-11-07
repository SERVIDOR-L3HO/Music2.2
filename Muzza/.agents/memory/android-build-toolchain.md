---
name: Android build toolchain
description: Replit-specific Java and Android SDK versions required to build this native app.
---

Use Java 21 for Gradle builds. The available Replit Java module `java-graalvm22.3` provides Java 19, while the project requests a Java 21 toolchain; add the `jdk21` system package.

Android API 37 is exposed by Google's SDK Manager as `platforms;android-37.0` on channel 3. The stable `platforms;android-37` package was not available. Build Tools 36.0.0 and platform-tools were available on stable.

**Why:** This Replit package index did not include an Android SDK, and the default Java module was too old for the Gradle toolchain.

**How to apply:** Use the official Google command-line tools, install API 37 from channel 3, and set `ANDROID_HOME` or `local.properties` to `.cache/android-sdk`.
