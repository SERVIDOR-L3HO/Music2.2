---
name: Android app identity
description: Project rule for display rebrands and application ID changes.
---

For display-name branding work, preserve the existing application ID and package namespace unless the user requests a new Android app identity.

**Why:** Changing the application ID makes Android treat the app as a separate install and can affect updates, store listings, and app data.

**How to apply:** Update the visible name, icons, and launch visuals separately; ask before migrating the application ID.
