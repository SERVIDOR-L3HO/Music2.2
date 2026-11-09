---
name: Android theme overrides
description: Avoid implicit parent inference when overriding styles in resource qualifier directories.
---

When a style is redefined in a qualified resource folder such as `values-v31`, give it an explicit parent that exists in every resource configuration. For version-specific additions, keep common attributes in a separately named base style and inherit from that base.

**Why:** A dotted style name without a parent can infer a parent from its prefix; an override of `Theme.Muzza` then looked for a nonexistent `Theme` and failed AAPT resource linking.

**How to apply:** Check every qualified style override for an explicit valid parent, especially when adding Android-version-specific splash-screen attributes.
